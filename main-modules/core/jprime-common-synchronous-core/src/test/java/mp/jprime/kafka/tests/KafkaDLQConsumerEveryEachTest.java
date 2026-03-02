package mp.jprime.kafka.tests;

import mp.jprime.json.services.JPKafkaJsonMapper;
import mp.jprime.kafka.configs.KafkaPublisherConfigTest;
import mp.jprime.kafka.consumers.KafkaDeadLetterBatchTestConsumer;
import mp.jprime.kafka.consumers.KafkaDeadLetterTestConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Тест эмулирует ситуацию когда при прослушивании топика эпизодически, на некоторых возникает
 * ошибка равномерно на всех сообщениях.
 * В топик помещаются числа от 1 до N и задается условие, что если число делится на заданное, то ошибка.
 * Для последнего топика задается число первых ошибочных сообщений для того, чтоб по итогу все сообщения были обработаны
 *
 * Например: в топике различные данные и бизнес логика падает при определенной конфигурации
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Tag("manualTests")
class KafkaDLQConsumerEveryEachTest extends KafkaDLQConsumerBaseTest {

  @Value("${jprime.test.kafka.kafkaServers}")
  private String bootstrapServers;

  private KafkaTemplate<String, String> kafkaBatchOperations;
  @Autowired
  private JPKafkaJsonMapper jsonMapper;

  @Autowired
  @Qualifier(KafkaPublisherConfigTest.KAFKA_BATCH_TEMPLATE)
  private void setKafkaBatchOperations(KafkaTemplate<String, String> kafkaBatchOperations) {
    this.kafkaBatchOperations = kafkaBatchOperations;
  }

  private static final String GROUP_ID = "test-dlq";
  private static final int BATCH_SIZE = 8;
  private static final long TOPIC_MAIN_DELAY = 0L;
  private static final long TOPIC_0_DELAY = 1000L;
  private static final long TOPIC_1_DELAY = 3000L;


  private static final boolean TRACE = false;
  private static final int N = 100;
  private static final int TOPIC_MAIN_ERROR_EACH = 2;
  private static final int TOPIC_0_ERROR_EACH = 3;
  private static final int LAST_TOPIC_ERROR_COUNT = 5;


  @Configuration
  @ComponentScan
      (value = {"mp"}
      )
  public static class Config {
  }

  @Test
  void test_batch() {
    String topicMain = "every-each-batch-test";
    String topic0 = topicMain + "-0";
    String topic1 = topicMain + "-1";

    run_test((eventConsumer, received) -> {
      KafkaDeadLetterBatchTestConsumer consumer = KafkaDeadLetterBatchTestConsumer.newBuilder()
          .jsonMapper(jsonMapper)
          .kafkaOperations(kafkaBatchOperations)
          .bootstrapAddress(bootstrapServers)
          .logger(TRACE ? this::print : (s) -> {})
          .received(received)
          .topic(topicMain)
          .groupId(GROUP_ID)
          .concurrency(1)
          .batchSize(BATCH_SIZE)
          .cascadeIntervals(List.of(TOPIC_MAIN_DELAY, TOPIC_0_DELAY, TOPIC_1_DELAY))
          .eventConsumer(eventConsumer)
          .build();
      consumer.initAndStart();
    }, topicMain, topic0, topic1);
  }

  @Test
  void test_single() {
    String topicMain = "every-each-single-test";
    String topic0 = topicMain + "-0";
    String topic1 = topicMain + "-1";

    run_test((eventConsumer, received) -> {
      KafkaDeadLetterTestConsumer consumer = KafkaDeadLetterTestConsumer.newBuilder()
          .kafkaOperations(kafkaBatchOperations)
          .bootstrapAddress(bootstrapServers)
          .logger(TRACE ? this::print : (s) -> {})
          .received(received)
          .topic(topicMain)
          .groupId(GROUP_ID)
          .concurrency(1)
          .cascadeIntervals(List.of(TOPIC_MAIN_DELAY, TOPIC_0_DELAY, TOPIC_1_DELAY))
          .eventConsumer(eventConsumer)
          .build();
      consumer.initAndStart();
    }, topicMain, topic0, topic1);
  }

  void run_test(BiConsumer<BiConsumer<ConsumerRecord<String, String>, Consumer<String>>, Map<String, Collection<String>>> consumerStarter,
                String topicMain, String topic0, String topic1) {
    print(">>>>>> BootstrapServers: " + bootstrapServers);

    createTopics(bootstrapServers, topicMain, 4);
    createTopics(bootstrapServers, topic0, 4);
    createTopics(bootstrapServers, topic1, 4);

    long startTime = System.currentTimeMillis();
    for (int i = 1; i <= N; i++) {
      kafkaBatchOperations.send(topicMain, Objects.toString(i));
    }
    kafkaBatchOperations.flush();
    print(String.format("Sent %s messages. Total duration: {%s} ms", N, System.currentTimeMillis() - startTime));

    final Map<String, Collection<String>> received = new ConcurrentHashMap<>();
    final AtomicInteger lastTopicErrorCounter = new AtomicInteger(0);

    BiConsumer<ConsumerRecord<String, String>, Consumer<String>> doException = (record, logger) -> {
      String msg = "filed on topic " + record.topic() + " and value " + record.value();
      logger.accept(msg);
      throw new BatchListenerFailedException(msg, new NullPointerException() , record);
    };

    BiConsumer<ConsumerRecord<String, String>, Consumer<String>> eventConsumer = (record, logger) -> {
      int intValue = Integer.parseInt(record.value());
      if (record.topic().equals(topic1)) {
        if (lastTopicErrorCounter.incrementAndGet() <= LAST_TOPIC_ERROR_COUNT) {
          doException.accept(record, logger);
        }
      } else if (record.topic().equals(topic0)) {
        if (intValue % TOPIC_0_ERROR_EACH == 0) {
          doException.accept(record, logger);
        }
      } else {
        if (intValue % TOPIC_MAIN_ERROR_EACH == 0) {
          doException.accept(record, logger);
        }
      }
    };

    print(">>>>>> run consumer " + LocalDateTime.now());
    startTime = System.currentTimeMillis();
    consumerStarter.accept(eventConsumer, received);

    // считаем предполагаемое время выполнения
    // оценим как количество сообщений в 3ем с худшим сценарием, что сообщения будут считываться по одному
    // добавим 2 сек на все остальное
    long duration =  (N / lcm(TOPIC_MAIN_ERROR_EACH, TOPIC_0_ERROR_EACH) + LAST_TOPIC_ERROR_COUNT) * TOPIC_1_DELAY / 1000 + 2 ;
    print(String.format("Expected execution time on topic {%s}: {%s} sec", topicMain, duration));

    try {
    Awaitility.await()
        .atMost(Duration.ofSeconds(duration))
        .pollInterval(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          long sizeTmain = received.containsKey(topicMain) ? received.get(topicMain).size() : 0L;
          long sizeT0 = received.containsKey(topic0) ? received.get(topic0).size() : 0L;
          long sizeT1 = received.containsKey(topic1) ? received.get(topic1).size() : 0L;

          print(String.format("ts = [%s]; Current counts: main={%s}, T0={%s}, T1={%s}", LocalDateTime.now(), sizeTmain, sizeT0, sizeT1));

          Assertions.assertEquals(N, sizeTmain, "Topic main должен обработать все N сообщений");
          Assertions.assertEquals(
              N / TOPIC_MAIN_ERROR_EACH, sizeT0,
              "Topic 0 должен получить N/TOPIC_MAIN_ERROR_EACH чисел"
          );
          Assertions.assertEquals(
              N / lcm(TOPIC_MAIN_ERROR_EACH, TOPIC_0_ERROR_EACH) + LAST_TOPIC_ERROR_COUNT, sizeT1,
              "Topic 1 должен получить N/НОК(TOPIC_MAIN_ERROR_EACH, TOPIC_0_ERROR_EACH) чисел и восстановленные после ошибки (LAST_TOPIC_ERROR_COUNT)"
          );
        });
    } catch (ConditionTimeoutException e) {
      print("!!!! Время ожидания истекло");
      throw e;
    }

    // главный и 0 топик не должны содержать дубликатов
    assertNoDuplicates(received.get(topicMain));
    assertNoDuplicates(received.get(topic0));

    print(String.format("Test finished. Total duration: {%s} ms", System.currentTimeMillis() - startTime));
  }

  // НОД (алгоритм Евклида) - наибольший общий делитель
  private long gcd(long a, long b) {
    while (b != 0) {
      long temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  // НОК (наименьшее общее кратное) через НОД
  private long lcm(long a, long b) {
    return (a / gcd(a, b)) * b; // делим сначала, чтобы избежать переполнения
  }
}