package mp.kafka.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация публикации событий в Kafka
 */
@Configuration
@Lazy(value = false)
public class KafkaPublisherConfigTest {
  /**
   * Максимальный размер события в Kafka (10 МБ)
   */
  private static final int MAX_REQUEST_SIZE = 10485760;
  public static final String KAFKA_BATCH_TEMPLATE = "kafkaBatchTemplate";
  public static final String KAFKA_NOBATCH_TEMPLATE = "kafkaNoBatchTemplate";

  @Value("${jprime.test.kafka.kafkaServers}")
  private String bootstrapAddress;

  private ProducerFactory<String, String> getKafkaGlobalEventsBatchProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, MAX_REQUEST_SIZE);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);
    return new DefaultKafkaProducerFactory<>(props);
  }

  private ProducerFactory<String, String> getKafkaGlobalEventsNoBatchProducerFactory() {
    if (bootstrapAddress == null || bootstrapAddress.isEmpty()) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 0);
    props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, MAX_REQUEST_SIZE);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean(name = KAFKA_BATCH_TEMPLATE)
  public KafkaTemplate<String, String> getKafkaBatchTemplate() {
    ProducerFactory<String, String> pf = getKafkaGlobalEventsBatchProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf);
  }

  @Bean(name = KAFKA_NOBATCH_TEMPLATE)
  public KafkaTemplate<String, String> getKafkaNoBatchTemplate() {
    ProducerFactory<String, String> pf = getKafkaGlobalEventsNoBatchProducerFactory();
    return pf == null ? null : new KafkaTemplate<>(pf, true);
  }
}
