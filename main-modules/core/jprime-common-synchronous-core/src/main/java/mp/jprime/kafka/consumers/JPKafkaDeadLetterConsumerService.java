package mp.jprime.kafka.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;

/**
 * Сервис формирования динамических слушателей по паттерну Dead Letter Queue
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public abstract class JPKafkaDeadLetterConsumerService<K, V> extends JPKafkaDeadLetterConsumerBaseService<K, V> {
  protected static final Logger LOG = LoggerFactory.getLogger(JPKafkaDeadLetterConsumerService.class);

  /**
   * Регистрирует слушателя
   *
   * @param kafkaListenerContainerFactory фабрика слушателей
   * @param topic                         топик
   * @param autoStart                     признак автозапуска слушателя
   */
  @Override
  protected void register(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory, String topic, boolean autoStart) {
    JPKafkaDynamicConsumer<K, V> consumer = JPKafkaDynamicBaseConsumer
        .builder(getMessageListener())
        .topic(topic)
        .kafkaListenerContainerFactory(kafkaListenerContainerFactory)
        .autoStart(autoStart)
        .build();
    register(consumer, topic);
  }

  /**
   * Логика обработки события слушателем
   */
  protected abstract MessageListener<K, V> getMessageListener();

  @Override
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(CommonErrorHandler errorHandler, long pollInterval) {
    Map<String, Object> consumersProps = getConsumersProps(pollInterval);
    ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(consumersProps, pollInterval);
    factory.setCommonErrorHandler(errorHandler);
    factory.setMissingTopicsFatal(false);
    return factory;
  }

}
