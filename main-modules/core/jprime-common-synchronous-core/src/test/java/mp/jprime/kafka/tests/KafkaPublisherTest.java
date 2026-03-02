package mp.jprime.kafka.tests;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.kafka.configs.JPKafkaContainerConfig;
import mp.jprime.kafka.configs.KafkaPublisherConfigTest;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Tag("manualTests")
class KafkaPublisherTest extends JPKafkaContainerConfig {

  @Value("${jprime.test.kafka.producers.timeout:5000}")
  private int timeout;

  //@Value("${jprime.test.kafka.producers.kafkaTopic}")
  //private String topic;

  private KafkaTemplate<String, String> kafkaBatchOperations;
  private KafkaTemplate<String, String> kafkaNoBatchOperations;


  @Autowired
  @Qualifier(KafkaPublisherConfigTest.KAFKA_BATCH_TEMPLATE)
  private void setKafkaBatchOperations(KafkaTemplate<String, String> kafkaBatchOperations) {
    this.kafkaBatchOperations = kafkaBatchOperations;
  }

  @Autowired
  @Qualifier(KafkaPublisherConfigTest.KAFKA_NOBATCH_TEMPLATE)
  private void setKafkaNoBatchOperations(KafkaTemplate<String, String> kafkaNoBatchOperations) {
    this.kafkaNoBatchOperations = kafkaNoBatchOperations;
  }

  private static final Collection<String> DATA = new ArrayList<>();

  static {
    for (int i = 0; i < 1000; i++) {
      DATA.add("Данные " + i);
    }
  }

  @Test
  void test1000ByOneWithoutCache_partition1() {
    createTopics("test1000ByOneWithoutCache_partition1", 1);
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> kafkaNoBatchOperations.send("test1000ByOneWithoutCache_partition1", x));
    System.out.println("execute time test1000ByOneWithoutCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByOneWithoutCache_partition4() {
    createTopics("test1000ByOneWithoutCache_partition4", 4);
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> kafkaNoBatchOperations.send("test1000ByOneWithoutCache_partition4", x));
    System.out.println("execute time test1000ByOneWithoutCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByBatch_partition1() {
    createTopics("test1000ByBatch_partition1", 1);
    AtomicBoolean isError = new AtomicBoolean(false);
    long ts = System.currentTimeMillis();

    CountDownLatch latch = new CountDownLatch(DATA.size());
    DATA.forEach(msg -> kafkaBatchOperations
        .send("test1000ByBatch_partition1", msg)
        .whenCompleteAsync((result, e) -> {
          if (e != null) {
            isError.set(true);
          }
          latch.countDown();
        })
    );
    kafkaBatchOperations.flush();

    assertDoesNotThrow(() -> latch.await(timeout, TimeUnit.MILLISECONDS));
    assertFalse(isError.get());
    assertEquals(0L, latch.getCount());

    System.out.println("execute time test1000ByBatch:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByBatch_partition4() {
    createTopics("test1000ByBatch_partition4", 4);
    AtomicBoolean isError = new AtomicBoolean(false);
    long ts = System.currentTimeMillis();

    CountDownLatch latch = new CountDownLatch(DATA.size());
    DATA.forEach(msg -> kafkaBatchOperations
        .send("test1000ByBatch_partition4", msg)
        .whenCompleteAsync((result, e) -> {
          if (e != null) {
            isError.set(true);
          }
          latch.countDown();
        })
    );
    kafkaBatchOperations.flush();

    assertDoesNotThrow(() -> latch.await(timeout, TimeUnit.MILLISECONDS));
    assertFalse(isError.get());
    assertEquals(0L, latch.getCount());

    System.out.println("execute time test1000ByBatch:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByOneWithCache_partition1() {
    createTopics("test1000ByOneWithCache_partition1", 1);
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> {
      kafkaBatchOperations.send("test1000ByOneWithCache_partition1", x);
      kafkaBatchOperations.flush();
    });
    System.out.println("execute time test1000ByOneWithCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByOneWithCache_partition4() {
    createTopics("test1000ByOneWithCache_partition4", 4);
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> {
      kafkaBatchOperations.send("test1000ByOneWithCache_partition4", x);
      kafkaBatchOperations.flush();
    });
    System.out.println("execute time test1000ByOneWithCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  private void createTopics(String topic, int partitions) {
    try (AdminClient adminClient = AdminClient.create(Collections.singletonMap(
        "bootstrap.servers", CONTAINER.getBootstrapServers()
    ))) {
      List<NewTopic> topics = List.of(
          new NewTopic(topic, partitions, (short) 1)
      );
      adminClient.createTopics(topics).all().get();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  @Configuration
  @ComponentScan
      (value = {"mp"}
      )
  public static class Config {
  }
}