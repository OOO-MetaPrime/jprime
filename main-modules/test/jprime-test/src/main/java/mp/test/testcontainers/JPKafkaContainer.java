package mp.test.testcontainers;

import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Тестовый контейнер Kafka
 */
public final class JPKafkaContainer {

  private static final String KAFKA_DOCKER_IMAGE = "apache/kafka-native:4.2.0";

  private JPKafkaContainer() {
  }

  public static KafkaContainer init() {
    return init((KAFKA_DOCKER_IMAGE));
  }

  private static KafkaContainer init(String imageName) {
    KafkaContainer container = new KafkaContainer(
        DockerImageName.parse(imageName)
    );

    container.start();
    return container;
  }
}
