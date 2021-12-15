package mp.jprime.kafka.consumers;

import mp.jprime.exceptions.JPRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Базовая логика динамических слушателей
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public abstract class JPKafkaDynamicConsumerBaseService<K, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaDynamicConsumerBaseService.class);

  private static final int DEFAULT_MAX_POLL_RECORDS = 1;
  private static final String DEFAULT_AUTO_OFFSET_RESET = "earliest";
  private static final int DEFAULT_CONSUMER_CONCURRENCY = 1;

  private final Map<String, JPKafkaDynamicConsumer<K, V>> consumers = new ConcurrentHashMap<>();

  /**
   * Настройка оффсета слушателя
   * <p>
   * По умолчанию - {@link #DEFAULT_AUTO_OFFSET_RESET "earliest"}
   */
  protected static String getAutoOffsetReset() {
    return DEFAULT_AUTO_OFFSET_RESET;
  }

  /**
   * @param additionalProps дополнительные свойства {@link ConsumerFactory},
   *                        либо переопределение свойств
   * @return {@link ConsumerFactory}
   */
  protected ConsumerFactory<K, V> getConsumerFactory(Map<String, Object> additionalProps) {
    String bootstrapAddress = getBootstrapAddress();
    if (StringUtils.isBlank(bootstrapAddress)) {
      throw new JPRuntimeException("jprime.kafka.dynamicListener.bootstrapAddress.notSpecified",
          "Not specified kafka servers for dynamic listener of topic \"" + getTopic() + '\"');
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, getGroupId());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, getKeyDeserializer());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getValueDeserializer());
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, getMaxPollRecords());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, getAutoOffsetReset());
    props.putAll(additionalProps);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory() {
    return getKafkaListenerContainerFactory(Collections.emptyMap());
  }

  /**
   * @param props дополнительные свойства {@link ConsumerFactory},
   *              либо переопределение свойств
   * @return {@link ConcurrentKafkaListenerContainerFactory}
   */
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(Map<String, Object> props) {
    ConsumerFactory<K, V> cf = getConsumerFactory(props);
    ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(cf);
    return factory;
  }

  /**
   * @param pollInterval интервал опроса топика
   * @return {@link ConcurrentKafkaListenerContainerFactory}
   */
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(long pollInterval) {
    return getKafkaListenerContainerFactory(Collections.emptyMap(), pollInterval);
  }

  /**
   * @param props        дополнительные свойства {@link ConsumerFactory},
   *                     либо переопределение свойств
   * @param pollInterval интервал опроса топика
   * @return {@link ConcurrentKafkaListenerContainerFactory}
   */
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(Map<String, Object> props, long pollInterval) {
    ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(props);
    factory.getContainerProperties().setIdleBetweenPolls(pollInterval);
    factory.setConcurrency(getConsumerConcurrency());
    return factory;
  }

  /**
   * Регистрирует слушателя
   *
   * @param kafkaListenerContainerFactory фабрика слушателей
   * @param topic                         топик
   * @param autoStart                     признак автозапуска слушателя
   */
  protected abstract void register(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory, String topic, boolean autoStart);

  /**
   * Регистрирует слушателя
   *
   * @param kafkaListenerContainerFactory фабрика слушателей
   * @param topic                         топик
   */
  protected void register(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory, String topic) {
    register(kafkaListenerContainerFactory, topic, true);
  }

  protected void register(JPKafkaDynamicConsumer<K, V> consumer, String topic) {
    if (consumers.putIfAbsent(topic, consumer) != null) {
      throw new JPRuntimeException("jprime.kafka.dynamicListener.duplicate", "Duplicate listener for topic \"" + topic + '\"');
    }
  }

  @EventListener
  public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
    consumers.values().stream()
        .filter(JPKafkaDynamicConsumer::isAutoStart)
        .forEach(JPKafkaDynamicConsumer::start);
  }

  /**
   * Запускает слушателя топика или бросает исключение, если слушатель не найден
   */
  public void start(String topic) {
    getOrThrow(topic).start();
  }

  /**
   * Останавливает слушателя топика или бросает исключение, если слушатель не найден
   */
  public void stop(String topic) {
    getOrThrow(topic).stop();
  }

  /**
   * Ставит на паузу слушателя топика или бросает исключение, если слушатель не найден
   */
  public void pause(String topic) {
    getOrThrow(topic).pause();
  }

  /**
   * Снимает с паузы слушателя топика или бросает исключение, если слушатель не найден
   */
  public void resume(String topic) {
    getOrThrow(topic).resume();
  }

  private JPKafkaDynamicConsumer<K, V> getOrThrow(String topic) {
    JPKafkaDynamicConsumer<K, V> consumer = consumers.get(topic);
    if (consumer == null) {
      throw new JPRuntimeException("jprime.kafka.dynamicListener.notFound",
          "Not found listener for topic \"" + topic + '\"');
    }
    return consumer;
  }

  /**
   * Проверяет зарегистрирован ли слушатель для переданного топика
   *
   * @param topic проверяемый топик
   * @return Да/Нет
   */
  protected boolean exists(String topic) {
    return consumers.containsKey(topic);
  }

  /**
   * Настройка количества событий, читаемых из топика за раз
   * <p>
   * По умолчанию - {@link #DEFAULT_MAX_POLL_RECORDS 1}
   */
  protected int getMaxPollRecords() {
    return DEFAULT_MAX_POLL_RECORDS;
  }

  /**
   * Идентификатор слушателя
   */
  protected abstract String getGroupId();

  /**
   * Топик
   */
  protected abstract String getTopic();

  /**
   * Адрес кафки
   */
  protected abstract String getBootstrapAddress();

  protected abstract Class<? extends Deserializer<K>> getKeyDeserializer();

  protected abstract Class<? extends Deserializer<V>> getValueDeserializer();

  /**
   * Количество параллельных слушателей топика
   */
  protected int getConsumerConcurrency() {
    return DEFAULT_CONSUMER_CONCURRENCY;
  }
}
