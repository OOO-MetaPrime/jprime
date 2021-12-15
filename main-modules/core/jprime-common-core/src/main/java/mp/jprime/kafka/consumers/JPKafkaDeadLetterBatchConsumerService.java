package mp.jprime.kafka.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.RecoveringBatchErrorHandler;

import java.util.Map;

/**
 * Сервис формирования динамических слушателей по паттерну Dead Letter Queue
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public abstract class JPKafkaDeadLetterBatchConsumerService<K, V> extends JPKafkaDeadLetterConsumerBaseService<K, V, BatchErrorHandler> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaDeadLetterBatchConsumerService.class);

  /**
   * Регистрирует слушателя
   *
   * @param kafkaListenerContainerFactory фабрика слушателей
   * @param topic                         топик
   * @param autoStart                     признак автозапуска слушателя
   */
  @Override
  protected void register(KafkaListenerContainerFactory<?> kafkaListenerContainerFactory, String topic, boolean autoStart) {
    JPKafkaDynamicConsumer<K, V> consumer = JPKafkaDynamicBatchConsumer
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
  protected abstract BatchMessageListener<K, V> getMessageListener();

  /**
   * Получает обработчик ошибок, восстанавливающий обработанные с ошибкой события в переданный топик
   *
   * @param recoveryTopic топик для необработанных событий
   * @return {@link BatchErrorHandler обработчик ошибок}
   */
  @Override
  protected BatchErrorHandler getErrorHandler(String recoveryTopic) {
    ConsumerRecordRecoverer recoverer = getRecoverer(getKafkaTemplate(), recoveryTopic);
    return new RecoveringBatchErrorHandler(recoverer, NO_RETRY_BACK_OFF); //TODO: заменить Deprecated
  }

  @Override
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(BatchErrorHandler errorHandler, long pollInterval) {
    Map<String, Object> consumersProps = getConsumersProps(pollInterval);
    ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(consumersProps, pollInterval);
    factory.setBatchErrorHandler(errorHandler);
    factory.setBatchListener(true);
    factory.setMissingTopicsFatal(false);
    return factory;
  }

}
