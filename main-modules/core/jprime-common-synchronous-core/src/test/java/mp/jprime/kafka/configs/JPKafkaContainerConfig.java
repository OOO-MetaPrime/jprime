package mp.jprime.kafka.configs;

import mp.test.testcontainers.JPKafkaContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;

public class JPKafkaContainerConfig {
  public static KafkaContainer CONTAINER;

  @BeforeAll
  static void setupAll() {
    CONTAINER = JPKafkaContainer.init();
  }

  @DynamicPropertySource
  public static void initProperties(DynamicPropertyRegistry registry) {
    registry.add("jprime.test.kafka.kafkaServers", () -> CONTAINER.getBootstrapServers());
  }
}
