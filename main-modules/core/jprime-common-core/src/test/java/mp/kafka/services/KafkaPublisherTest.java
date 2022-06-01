package mp.kafka.services;

import mp.kafka.configs.KafkaPublisherConfigTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
  private static final Logger LOG = LoggerFactory.getLogger(KafkaPublisherTest.class);

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
    ListenableFutureCallback<SendResult<String, String>> callback =
        new ListenableFutureCallback<SendResult<String, String>>() {
          @Override
          public void onSuccess(SendResult<String, String> result) {
            latch.countDown();
          }

          @Override
          public void onFailure(Throwable ex) {
            isError.set(true);
            latch.countDown();
          }
        };

    DATA.forEach(msg -> kafkaBatchOperations.send(topic, msg).addCallback(callback));
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