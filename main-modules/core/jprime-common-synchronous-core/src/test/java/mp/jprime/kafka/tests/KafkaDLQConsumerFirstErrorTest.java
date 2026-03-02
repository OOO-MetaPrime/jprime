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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Тест эмулирует ошибку которая, проявляется на всех элементах и затем ее устранение.
 * Задается настройка для каждого топика, сколько первых сообщений считать ошибочными
 * <p>
 * Например: пропадает доступ к ресурсу обращения, а затем он восстанавливается
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Tag("manualTests")
class KafkaDLQConsumerFirstErrorTest extends KafkaDLQConsumerBaseTest {

  @Value("${jprime.test.kafka.kafkaServers}")
  private String bootstrapServers;
  @Autowired
  private JPKafkaJsonMapper jsonMapper;
  private KafkaTemplate<String, String> kafkaBatchOperations;

  @Autowired
  @Qualifier(KafkaPublisherConfigTest.KAFKA_BATCH_TEMPLATE)
  private void setKafkaBatchOperations(KafkaTemplate<String, String> kafkaBatchOperations) {
    this.kafkaBatchOperations = kafkaBatchOperations;
  }

  private static final String GROUP_ID = "test-dlq";
  private static final int BATCH_SIZE = 4;
  private static final long TOPIC_MAIN_DELAY = 0L;
  private static final long TOPIC_0_DELAY = 3000L;
  private static final long TOPIC_1_DELAY = 10000L;

  private static final boolean TRACE = false;
  private static int N = 10;
  private static Map<String, Integer> TOPIC_FIRST_ERROR = Map.of();

  @Configuration
  @ComponentScan
      (value = {"mp"}
      )
  public static class Config {
  }

  @Test
  void test_batch() {
    String topicMain = "first-errors-batch-test";
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
    String topicMain = "first-errors-single-test";
    String topic0 = topicMain + "-0";
    String topic1 = topicMain + "-1";

    run_test((eventConsumer, received) -> {
      KafkaDeadLetterTestConsumer consumer = KafkaDeadLetterTestConsumer.newBuilder()
          .kafkaOperations(kafkaBatchOperations)
          .bootstrapAddress(bootstrapServers)
          .logger(TRACE ? this::print : (s) -> { })
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

    N = 100;
    TOPIC_FIRST_ERROR = Map.of(
        topicMain, 20,
        topic0, 10,
        topic1, 5
    );

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
    final Map<String, Integer> topicErrors = new ConcurrentHashMap<>();

    BiConsumer<ConsumerRecord<String, String>, Consumer<String>> doException = (record, logger) -> {
      String msg = "filed on topic " + record.topic() + " and value " + record.value();
      logger.accept(msg);
      throw new BatchListenerFailedException(msg, new NullPointerException(), record);
    };

    BiConsumer<ConsumerRecord<String, String>, Consumer<String>> eventConsumer = (record, logger) -> {
      topicErrors.putIfAbsent(record.topic(), 0);
      topicErrors.put(record.topic(), topicErrors.get(record.topic()) + 1);
      if (topicErrors.get(record.topic()) <= TOPIC_FIRST_ERROR.get(record.topic())) {
        doException.accept(record, logger);
      }
    };

    print(">>>>>> run consumer " + LocalDateTime.now());
    startTime = System.currentTimeMillis();
    consumerStarter.accept(eventConsumer, received);

    // считаем предполагаемое время выполнения
    // оценим как количество сообщений во 2ом или 3ем с худшим сценарием, что сообщения будут считываться по одному
    // добавим 2 сек на все остальное
    long duration = Math.max(TOPIC_FIRST_ERROR.get(topicMain) * TOPIC_0_DELAY, TOPIC_FIRST_ERROR.get(topic0) * TOPIC_1_DELAY) / 1000 + 2;
    print(String.format("Expected execution time on topic {%s}: {%s} sec", topicMain, duration));

    try {
      Awaitility.await()
          .atMost(Duration.ofSeconds(duration))
          .pollInterval(Duration.ofSeconds(1))
          .untilAsserted(() -> {
            int sizeTmain = received.containsKey(topicMain) ? received.get(topicMain).size() : 0;
            int sizeT0 = received.containsKey(topic0) ? received.get(topic0).size() : 0;
            int sizeT1 = received.containsKey(topic1) ? received.get(topic1).size() : 0;

            print(String.format("ts = [%s]; Current counts: main={%s}, T0={%s}, T1={%s}", LocalDateTime.now(), sizeTmain, sizeT0, sizeT1));

            Assertions.assertEquals(N, sizeTmain, "Topic main должен обработать все N сообщений");
            Assertions.assertEquals(TOPIC_FIRST_ERROR.get(topicMain), sizeT0, "Topic 0 должен получить TOPIC_FIRST_ERROR.get(topicMain)");
            Assertions.assertEquals(TOPIC_FIRST_ERROR.get(topic0) + TOPIC_FIRST_ERROR.get(topic1), sizeT1, "Topic 1 должен получить TOPIC_FIRST_ERROR.get(topic0) и восстановленные после ошибки TOPIC_FIRST_ERROR.get(topic1)");
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

}