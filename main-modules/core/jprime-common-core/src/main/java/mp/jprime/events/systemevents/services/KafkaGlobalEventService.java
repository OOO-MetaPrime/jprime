package mp.jprime.events.systemevents.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.events.systemevents.JPSystemEvent;
import mp.jprime.events.systemevents.json.JsonJPSystemEvent;
import mp.jprime.events.systemevents.json.services.JsonJPSystemEventConvertor;
import mp.jprime.json.services.JPKafkaJsonMapper;
import mp.jprime.system.JPAppProperty;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Публикация глобальных системных событий
 */
@EnableKafka
@Lazy(value = false)
@Configuration
@ConditionalOnProperty(value = "jprime.events.globalevents.kafka.enabled", havingValue = "true")
public class KafkaGlobalEventService implements GlobalEventPublisher {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaGlobalEventService.class);

  @Value("${jprime.events.globalevents.kafka.kafkaServers:}")
  private String bootstrapAddress;
  @Value("${jprime.events.globalevents.kafka.kafkaTopic:}")
  private String topic;

  private KafkaTemplate<String, String> kafkaTemplate;

  private ApplicationEventPublisher eventPublisher;

  private JsonJPSystemEventConvertor jsonJPSystemEventConvertor;

  private JPKafkaJsonMapper jpKafkaJsonMapper;

  private JPAppProperty appProperty;

  @Autowired
  private void setJPKafkaJsonMapper(JPKafkaJsonMapper jpKafkaJsonMapper) {
    this.jpKafkaJsonMapper = jpKafkaJsonMapper;
  }

  @Autowired
  private void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Autowired
  private void setJsonJPSystemEventConvertor(JsonJPSystemEventConvertor jsonJPSystemEventConvertor) {
    this.jsonJPSystemEventConvertor = jsonJPSystemEventConvertor;
  }

  @Autowired(required = false)
  @Qualifier("kafkaGlobalEventsTemplate")
  private void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Autowired
  private void setAppProperty(JPAppProperty appProperty) {
    this.appProperty = appProperty;
  }

  public ProducerFactory<String, String> getKafkaGlobalEventsProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  public ConsumerFactory<String, String> getKafkaGlobalEventsConsumerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, appProperty.systemCode());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean(name = "kafkaGlobalEventsTemplate")
  public KafkaTemplate<String, String> getKafkaTemplate() {
    ProducerFactory<String, String> pf = getKafkaGlobalEventsProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf);
  }

  @Bean(name = "kafkaGlobalEventsContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, String> getKafkaGlobalEventsContainerFactory() {
    ConsumerFactory<String, String> cf = getKafkaGlobalEventsConsumerFactory();
    if (cf == null) {
      return null;
    }
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(cf);
    return factory;
  }

  @KafkaListener(topics = "${jprime.events.globalevents.kafka.kafkaTopic}",
      containerFactory = "kafkaGlobalEventsContainerFactory")
  public void listen(String json) {
    try {
      JPSystemEvent event = toEvent(json);
      String consumerCode = event.getConsumer();
      if (StringUtils.isEmpty(consumerCode) || appProperty.systemCode().equals(consumerCode)) {
        eventPublisher.publishEvent(JPSystemApplicationEvent.from(event));
      }
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
              LOG.error("Unable to send message=[{}] due to : {}", msg, e.getMessage(), e);
            }
          });
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private String toJSON(JPSystemEvent event) throws JsonProcessingException {
    return jpKafkaJsonMapper.getObjectMapper().writeValueAsString(jsonJPSystemEventConvertor.toJsonJPSystemEvent(event));
  }

  private JPSystemEvent toEvent(String string) throws IOException, SecurityException {
    return jsonJPSystemEventConvertor.toJPSystemEvent(jpKafkaJsonMapper.getObjectMapper().readValue(string, JsonJPSystemEvent.class));
  }

}
