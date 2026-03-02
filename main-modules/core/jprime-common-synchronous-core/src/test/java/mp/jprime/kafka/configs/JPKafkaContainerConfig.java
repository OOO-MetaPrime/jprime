package mp.jprime.kafka.configs;

import mp.test.testcontainers.JPKafkaContainer;
import org.junit.ClassRule;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;

public class JPKafkaContainerConfig {
  @ClassRule
  public static KafkaContainer CONTAINER = JPKafkaContainer.init();

  @DynamicPropertySource
  public static void initProperties(DynamicPropertyRegistry registry) {
//    registry.add("jprime.test.kafka.kafkaServers", () -> "172.16.1.171:9092");
    registry.add("jprime.test.kafka.kafkaServers", () -> CONTAINER.getBootstrapServers());
  }
}
