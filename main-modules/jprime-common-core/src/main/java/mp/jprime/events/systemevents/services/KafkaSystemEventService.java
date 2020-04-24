package mp.jprime.events.systemevents.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import mp.jprime.json.services.KafkaJsonMapper;
import mp.jprime.system.services.JavaClassCache;
import mp.jprime.events.systemevents.JPEventInfo;
import mp.jprime.events.systemevents.JPSystemEvent;
import mp.jprime.events.systemevents.json.JPEventData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
@ConditionalOnProperty(value = "jprime.events.systemevents.kafka.enabled", havingValue = "true")
public class KafkaSystemEventService implements KafkaJsonMapper, SystemEventPublisher {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaSystemEventService.class);

  @Value("${jprime.events.systemevents.kafka.kafkaServers:}")
  private String bootstrapAddress;
  @Value("${jprime.events.systemevents.kafka.kafkaTopic:}")
  private String topic;

  private KafkaTemplate<String, String> kafkaTemplate;

  private ApplicationEventPublisher eventPublisher;

  private JavaClassCache javaClassCache;

  @Autowired
  private void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Autowired
  private void setJavaClassCache(JavaClassCache javaClassCache) {
    this.javaClassCache = javaClassCache;
  }

  @Autowired(required = false)
  @Qualifier("kafkaSystemEventsTemplate")
  private void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public ProducerFactory<String, String> getKafkaSystemEventsProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  public ConsumerFactory<String, String> getKafkaSystemEventsConsumerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean(name = "kafkaSystemEventsTemplate")
  public KafkaTemplate<String, String> getKafkaTemplate() {
    ProducerFactory<String, String> pf = getKafkaSystemEventsProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf);
  }

  @Bean(name = "kafkaSystemEventsContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, String> getKafkaSystemEventsContainerFactory() {
    ConsumerFactory<String, String> cf = getKafkaSystemEventsConsumerFactory();
    if (cf == null) {
      return null;
    }
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(cf);
    return factory;
  }

  @KafkaListener(topics = "${jprime.events.systemevents.kafka.kafkaTopic}",
      containerFactory = "kafkaSystemEventsContainerFactory")
  public void listen(String json) {
    try {
      JPSystemEvent event = toEvent(json);
      eventPublisher.publishEvent(event);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  @Override
  public void publishEvent(JPSystemEvent event) {
    if (kafkaTemplate == null) {
      return;
    }
    try {
      String msg = toJSON(event);
      kafkaTemplate
          .send(topic, msg)
          .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
            }

            @Override
            public void onFailure(Throwable e) {
              LOG.error("Unable to send message=[" + msg + "] due to : " + e.getMessage(), e);
            }
          });
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }


  private String toJSON(JPSystemEvent event) throws JsonProcessingException {
    return KAFKA_OBJECT_MAPPER.writeValueAsString(new JPEventData(event.getDate(),
        event.getClass().getName(),
        event.getInfo().getClass().getName(),
        KAFKA_OBJECT_MAPPER.writeValueAsString(event.getInfo())));
  }

  private JPSystemEvent toEvent(String json) throws IOException,
      NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    JPEventData data = KAFKA_OBJECT_MAPPER.readValue(json, JPEventData.class);
    JPEventInfo info = (JPEventInfo) KAFKA_OBJECT_MAPPER.readValue(data.getInfo(), javaClassCache.getClass(data.getInfoClassName()));
    return (JPSystemEvent) javaClassCache
        .getClass(data.getEventClassName())
        .getDeclaredConstructor(LocalDateTime.class, info.getClass())
        .newInstance(data.getDate(), info);
  }
}
