package mp.jprime.events.systemevents.services;

import mp.jprime.system.JPAppProperty;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "jprime.events.globalevents.kafka.enabled", havingValue = "true")
public class KafkaGlobalEventServiceConfiguration {
  @Value("${jprime.events.globalevents.kafka.kafkaServers:}")
  private String bootstrapAddress;

  private JPAppProperty appProperty;

  @Autowired
  private void setAppProperty(JPAppProperty appProperty) {
    this.appProperty = appProperty;
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


  @Bean(name = "kafkaGlobalEventsTemplate")
  public KafkaTemplate<String, String> getKafkaTemplate() {
    ProducerFactory<String, String> pf = getKafkaGlobalEventsProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf);
  }

  private ProducerFactory<String, String> getKafkaGlobalEventsProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  private ConsumerFactory<String, String> getKafkaGlobalEventsConsumerFactory() {
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
}
