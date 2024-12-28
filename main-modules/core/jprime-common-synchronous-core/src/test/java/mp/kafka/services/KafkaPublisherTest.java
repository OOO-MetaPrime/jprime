package mp.kafka.services;

import mp.kafka.configs.KafkaPublisherConfigTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Tag("manualTests")
class KafkaPublisherTest {

  @Value("${jprime.test.kafka.producers.timeout:5000}")
  private int timeout;

  @Value("${jprime.test.kafka.producers.kafkaTopic}")
  private String topic;

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
  void test1000ByOneWithoutCache() {
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> kafkaNoBatchOperations.send(topic, x));
    System.out.println("execute time test1000ByOneWithoutCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Test
  void test1000ByBatch() {
    AtomicBoolean isError = new AtomicBoolean(false);
    long ts = System.currentTimeMillis();

    CountDownLatch latch = new CountDownLatch(DATA.size());
    DATA.forEach(msg -> kafkaBatchOperations
        .send(topic, msg)
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
  void test1000ByOneWithCache() {
    long ts = System.currentTimeMillis();
    DATA.forEach(x -> {
      kafkaBatchOperations.send(topic, x);
      kafkaBatchOperations.flush();
    });
    System.out.println("execute time test1000ByOneWithCache:" + (System.currentTimeMillis() - ts) + " ms");
  }

  @Lazy(value = false)
  @Configuration
  @ComponentScan
      (value = {"mp"}
      )
  public static class Config {
  }
}