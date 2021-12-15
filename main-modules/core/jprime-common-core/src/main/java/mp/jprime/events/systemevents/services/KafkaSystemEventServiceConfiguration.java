package mp.jprime.events.systemevents.services;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ConditionalOnProperty(value = "jprime.events.systemevents.kafka.enabled", havingValue = "true")
public class KafkaSystemEventServiceConfiguration {
  @Value("${jprime.events.systemevents.kafka.kafkaServers:}")
  private String bootstrapAddress;

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

  @Bean(name = "kafkaSystemEventsTemplate")
  public KafkaTemplate<String, String> getKafkaTemplate() {
    ProducerFactory<String, String> pf = getKafkaSystemEventsProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf);
  }

  private ConsumerFactory<String, String> getKafkaSystemEventsConsumerFactory() {
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

  private ProducerFactory<String, String> getKafkaSystemEventsProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }
}
