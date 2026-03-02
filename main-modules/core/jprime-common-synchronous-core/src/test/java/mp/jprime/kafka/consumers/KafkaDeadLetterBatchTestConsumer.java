package mp.jprime.kafka.consumers;

import mp.jprime.json.services.JPKafkaJsonMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaOperations;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KafkaDeadLetterBatchTestConsumer extends JPKafkaDeadLetterBatchStringConsumerService {
  private final KafkaOperations<String, String> kafkaOperations;
  private final String bootstrapAddress;
  private final Consumer<String> logger;
  private final Map<String, Collection<String>> received;
  private final String topic;
  private final String groupId;
  private final int concurrency;
  private final int batchSize;
  private final Collection<Long> cascadeIntervals;
  private final BiConsumer<ConsumerRecord<String, String>, Consumer<String>> eventConsumer;

  private KafkaDeadLetterBatchTestConsumer(Builder builder) {
    setJsonMapper(builder.jsonMapper);
    kafkaOperations = builder.kafkaOperations;
    bootstrapAddress = builder.bootstrapAddress;
    logger = builder.logger;
    received = builder.received;
    topic = builder.topic;
    groupId = builder.groupId;
    concurrency = builder.concurrency;
    batchSize = builder.batchSize;
    cascadeIntervals = builder.cascadeIntervals;
    eventConsumer = builder.eventConsumer;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public void initAndStart() {
    init();
    start();
  }

  @Override
  protected void onEvent(List<ConsumerRecord<String, String>> consumerRecords) {
    logger.accept(">>> received batch records: ");
    logger.accept(String.format(
        "ts = [%s]; topic = [%s]; value = [%s]",
        LocalDateTime.now(),
        consumerRecords.stream().map(ConsumerRecord::topic).collect(Collectors.toSet()).toString(),
        String.join(",", consumerRecords.stream().map(ConsumerRecord::value).collect(Collectors.toSet()))
    ));

    for (ConsumerRecord<String, String> cr: consumerRecords) {
      received.computeIfAbsent(cr.topic(), k -> new ConcurrentLinkedQueue<>()).add(cr.value());
      eventConsumer.accept(cr, logger);
    }
  }

  @Override
  protected KafkaOperations<String, String> getKafkaTemplate() {
    return kafkaOperations;
  }

  @Override
  protected Collection<Long> getCascadeIntervals() {
    return cascadeIntervals;
  }

  @Override
  protected String getGroupId() {
    return groupId;
  }

  @Override
  protected String getBootstrapAddress() {
    return bootstrapAddress;
  }

  @Override
  protected int getConsumerConcurrency() {
    return concurrency;
  }

  @Override
  public String getTopic() {
    return topic;
  }

  @Override
  protected int getMaxPollRecords() {
    return batchSize;
  }

  public static final class Builder {
    private JPKafkaJsonMapper jsonMapper;
    private KafkaOperations<String, String> kafkaOperations;
    private String bootstrapAddress;
    private Consumer<String> logger;
    private Map<String, Collection<String>> received;
    private String topic;
    private String groupId;
    private int concurrency;
    private int batchSize;
    private Collection<Long> cascadeIntervals;
    private BiConsumer<ConsumerRecord<String, String>, Consumer<String>> eventConsumer;

    private Builder() {
    }

    public Builder jsonMapper(JPKafkaJsonMapper jsonMapper) {
      this.jsonMapper = jsonMapper;
      return this;
    }

    public Builder kafkaOperations(KafkaOperations<String, String> kafkaOperations) {
      this.kafkaOperations = kafkaOperations;
      return this;
    }

    public Builder bootstrapAddress(String bootstrapAddress) {
      this.bootstrapAddress = bootstrapAddress;
      return this;
    }

    public Builder logger(Consumer<String> logger) {
      this.logger = logger;
      return this;
    }

    public Builder received(Map<String, Collection<String>> received) {
      this.received = received;
      return this;
    }

    public Builder topic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder concurrency(int concurrency) {
      this.concurrency = concurrency;
      return this;
    }

    public Builder batchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
    }

    public Builder cascadeIntervals(Collection<Long> cascadeIntervals) {
      this.cascadeIntervals = cascadeIntervals;
      return this;
    }

    public Builder eventConsumer(BiConsumer<ConsumerRecord<String, String>, Consumer<String>> eventConsumer) {
      this.eventConsumer = eventConsumer;
      return this;
    }

    public KafkaDeadLetterBatchTestConsumer build() {
      return new KafkaDeadLetterBatchTestConsumer(this);
    }
  }
}
