package mp.jprime.kafka.consumers;

import mp.jprime.json.services.JPKafkaJsonMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.*;

import java.util.List;
import java.util.Map;

/**
 * Сервис формирования динамических слушателей по паттерну Dead Letter Queue
 *
 * @param <K> тип ключа события
 * @param <V> тип значения события
 */
public abstract class JPKafkaDeadLetterBatchConsumerService<K, V> extends JPKafkaDeadLetterConsumerBaseService<K, V> {
  protected static final Logger LOG = LoggerFactory.getLogger(JPKafkaDeadLetterBatchConsumerService.class);

  protected static final int START_BATCH_INDEX = 0;

  protected JPKafkaJsonMapper jsonMapper;

  @Autowired
  protected void setJsonMapper(JPKafkaJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

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


  private BatchMessageListener<K, V> getMessageListener() {
    return consumerRecord -> {
      try {
        onEvent(consumerRecord);
      } catch (BatchListenerFailedException e) {
        throw e;
      } catch (Exception e) {
        throw new BatchListenerFailedException(e.getMessage(), e, START_BATCH_INDEX);
      }
    };
  }

  /**
   * Логика обработки события слушателем
   */
  protected abstract void onEvent(List<ConsumerRecord<K, V>> consumerRecords);

  @Override
  protected ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(CommonErrorHandler errorHandler, long pollInterval) {
    Map<String, Object> consumersProps = getConsumersProps(pollInterval);
    ConcurrentKafkaListenerContainerFactory<K, V> factory = getKafkaListenerContainerFactory(consumersProps, pollInterval);
    factory.setCommonErrorHandler(errorHandler);
    factory.setBatchListener(true);
    factory.setMissingTopicsFatal(false);
    return factory;
  }

}
