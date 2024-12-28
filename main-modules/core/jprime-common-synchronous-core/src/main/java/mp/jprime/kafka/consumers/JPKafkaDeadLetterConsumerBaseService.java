package mp.jprime.kafka.consumers;

import jakarta.annotation.PostConstruct;
import mp.jprime.exceptions.JPRuntimeException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис формирования динамических слушателей по паттерну Dead Letter Queue
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public abstract class JPKafkaDeadLetterConsumerBaseService<K, V> extends JPKafkaDynamicConsumerBaseService<K, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaDeadLetterConsumerBaseService.class);
  private static final String TOPIC_NUMBER_DELIMITER = "-";
  private static final long NO_TIME_INTERVAL = 0L;
  private static final Collection<Long> DEFAULT_POLL_INTERVALS = Collections.singletonList(NO_TIME_INTERVAL);
  protected static final FixedBackOff NO_RETRY_BACK_OFF = new FixedBackOff(0, 1);

  @Value("${jprime.kafka.dead-letter-consumers.poll-interval:60000}")
  private long defaultInterval;

  @Value("${jprime.kafka.dead-letter-consumers.infinite:false}")
  private boolean infiniteDefaultListeners;

  @PostConstruct
  private void init() {
    Collection<Long> pollIntervals = getPollIntervals();
    if (CollectionUtils.isEmpty(pollIntervals)) {
      LOG.warn("Not specified poll settings for listener's cascade of topic \"{}\"", getTopic());
      pollIntervals = DEFAULT_POLL_INTERVALS;
    }
    int i = 0;
    for (Long pollInterval : pollIntervals) {
      boolean first = i == 0;
      boolean last = i == pollIntervals.size() - 1;
      //Если слушатель - единственный в каскаде, то он восстанавливает события в свой же топик
      String previousRecoveryTopic = first && last ? getTopic() : getTopic(i - 1);
      //Если это последний слушатель каскада, то он восстанавливает события в recoveryTopic предыдущего слушателя
      CommonErrorHandler errorHandler = last ?
          getErrorHandler(previousRecoveryTopic) : getErrorHandler(getTopic(i));
      ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(errorHandler, pollInterval);
      //Если это первый слушатель, то он слушает основной топик,
      //Иначе слушает recoveryTopic предыдущего слушателя
      String topic = first ? getTopic() : previousRecoveryTopic;
      register(factory, topic);
      i++;
    }
    initDefaultListeners();
  }

  private void initDefaultListeners() {
    getAllKafkaTopics().stream()
        .filter(this::matchTopic)
        .filter(topic -> !exists(topic))
        .forEach(topic -> {
          LOG.warn(
              "Topic \"{}\" has no listeners, default will be created with settings: interval={}, infinite={}",
              topic, defaultInterval, infiniteDefaultListeners
          );
          CommonErrorHandler errorHandler = infiniteDefaultListeners ? getErrorHandler(topic) : getErrorHandler(getTopic());
          ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(errorHandler, defaultInterval);
          register(factory, topic);
        });
  }

  private Collection<String> getAllKafkaTopics() {
    Map<String, Object> props = new HashMap<>();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapAddress());
    ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
    listTopicsOptions.listInternal(true);
    try (AdminClient adminClient = AdminClient.create(props)) {
      return adminClient.listTopics(listTopicsOptions).names().get();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private boolean matchTopic(String topic) {
    String regex = "^(" + getTopic() + TOPIC_NUMBER_DELIMITER + ")[\\d+]";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(topic);
    return matcher.matches();
  }

  /**
   * Получает обработчик ошибок, восстанавливающий обработанные с ошибкой события в переданный топик
   *
   * @param recoveryTopic топик для необработанных событий
   * @return {@link CommonErrorHandler обработчик ошибок}
   */
  private CommonErrorHandler getErrorHandler(String recoveryTopic) {
    ConsumerRecordRecoverer recoverer = getRecoverer(getKafkaTemplate(), recoveryTopic);
    return new DefaultErrorHandler(recoverer, NO_RETRY_BACK_OFF);
  }

  protected abstract ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(CommonErrorHandler errorHandler, long pollInterval);

  /**
   * Дополнительные свойства фабрики слушателей
   */
  protected Map<String, Object> getConsumersProps(long pollInterval) {
    Map<String, Object> additionalProps = new HashMap<>();
    additionalProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, getMaxPollInterval(pollInterval));
    return additionalProps;
  }

  private Integer getMaxPollInterval(long pollInterval) {
    int maxPollInterval = getMaxPollInterval();
    if (pollInterval > maxPollInterval) {
      //Добавляем к интервалу опроса топика дополнительное время на обработку события
      long interval = pollInterval + maxPollInterval;
      if (interval >= Integer.MAX_VALUE) {
        maxPollInterval = Integer.MAX_VALUE;
      } else {
        maxPollInterval = (int) interval;
      }
    }
    return maxPollInterval;
  }

  /**
   * Возвращает логику восстановления событий в случае ошибки их обработки
   *
   * @param template      шаблон продюсера восстановления
   * @param recoveryTopic топик, куда будут публиковаться восстановленные события
   */
  protected ConsumerRecordRecoverer getRecoverer(KafkaOperations<K, V> template, String recoveryTopic) {
    return new DeadLetterPublishingRecoverer(
        template,
        (consumerRecord, e) -> {
          LOG.debug("Recover record={} into topic={}, partition={}", consumerRecord.value(), recoveryTopic, consumerRecord.partition());
          return new TopicPartition(recoveryTopic, consumerRecord.partition());
        }
    );
  }

  /**
   * Шаблон продюсера для восстановления событий
   */
  protected abstract KafkaOperations<K, V> getKafkaTemplate();

  /**
   * Интервалы опроса топиков каскада (мс)
   */
  protected abstract Collection<Long> getPollIntervals();

  /**
   * Формирует имя очередного топика восстановления
   */
  private String getTopic(int number) {
    return getTopic() + TOPIC_NUMBER_DELIMITER + number;
  }

}
