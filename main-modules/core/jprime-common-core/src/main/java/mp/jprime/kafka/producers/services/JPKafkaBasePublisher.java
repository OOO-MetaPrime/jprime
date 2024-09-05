package mp.jprime.kafka.producers.services;

import jakarta.annotation.PostConstruct;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.kafka.producers.JPKafkaPublisher;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.FailureCallback;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Базовый сервис отправки событий в Kafka
 *
 * @param <E> тип события
 * @param <K> тип ключа {@link ProducerRecord}
 * @param <V> тип значения {@link ProducerRecord}
 */
public abstract class JPKafkaBasePublisher<E, K, V> implements JPKafkaPublisher<E> {
  private static final Logger LOG = LoggerFactory.getLogger(JPKafkaBasePublisher.class);

  @Value("${jprime.kafka.producers.timeout:5000}")
  private int timeout;

  @PostConstruct
  private void afterPropertiesSet() {
    if (getKafkaOperations() == null) {
      throw new JPRuntimeException("Not set Kafka Operations in publisher " + this.getClass());
    }
  }

  protected abstract KafkaOperations<K, V> getKafkaOperations();

  @Override
  public void publish(Collection<E> events) {
    if (CollectionUtils.isEmpty(events)) {
      return;
    }
    doSend(toProducerRecord(events));
  }

  @Override
  public void publishAsync(Collection<E> events) {
    if (CollectionUtils.isEmpty(events)) {
      return;
    }
    doSendAsync(toProducerRecord(events));
  }

  @Override
  public void publishAsync(Collection<E> events, FailureCallback failureCallback) {
    if (CollectionUtils.isEmpty(events)) {
      return;
    }
    doSendAsync(toProducerRecord(events), failureCallback);
  }

  private Collection<ProducerRecord<K, V>> toProducerRecord(Collection<E> events) {
    return events.stream()
        .filter(this::validateEvent)
        .map(event -> toProducerRecord(getTopic(event), event))
        .collect(Collectors.toList());
  }

  /**
   * Валидация события перед отправкой в кафку
   *
   * @param e Событие
   *
   * @return Признак успешной валидации
   */
  protected boolean validateEvent(E e) {
    return true;
  }

  private ProducerRecord<K, V> toProducerRecord(String topic, E event) {
    K key = toKey(event);
    V value = toValue(event);
    return new ProducerRecord<>(topic, key, value);
  }

  /**
   * Получить имя топика Kafka
   */
  protected abstract String getTopic(E event);

  /**
   * Конвертируем событие в ключ {@link ProducerRecord}
   *
   * @param event событие
   *
   * @return ключ {@link ProducerRecord}
   */
  protected K toKey(E event) {
    return null;
  }

  /**
   * Конвертируем событие в значение {@link ProducerRecord}
   *
   * @param event событие
   *
   * @return значение {@link ProducerRecord}
   */
  protected abstract V toValue(E event);

  private void doSend(Collection<ProducerRecord<K, V>> events) {
    AtomicReference<Throwable> error = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(events.size());

    KafkaOperations<K, V> kafkaOperations = getKafkaOperations();
    events.forEach(record -> kafkaOperations
        .send(record)
        .whenCompleteAsync((result, e) -> {
          if (e != null) {
            error.set(e);
            LOG.error(e.getMessage(), e);
          }
          latch.countDown();
        })
    );

    kafkaOperations.flush();

    boolean sent;
    try {
      sent = latch.await(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new JPRuntimeException("Failed sending event to topic cause " + e.getMessage(), e);
    }
    Throwable cause = error.get();
    if (cause != null) {
      throw new JPRuntimeException("Failed sending event to topic cause " + cause.getMessage(), cause);
    } else if (!sent) {
      throw new JPRuntimeException("Events sending not confirmed due to timeout=" + timeout);
    }
  }

  private void doSendAsync(Collection<ProducerRecord<K, V>> events) {
    KafkaOperations<K, V> kafkaOperations = getKafkaOperations();
    events.forEach(record -> kafkaOperations
        .send(record)
        .whenCompleteAsync((result, e) -> {
          if (e != null) {
            LOG.error(e.getMessage(), e);
          }
        })
    );
  }

  private void doSendAsync(Collection<ProducerRecord<K, V>> events, FailureCallback failureCallback) {
    KafkaOperations<K, V> kafkaOperations = getKafkaOperations();
    events.forEach(record -> kafkaOperations
        .send(record)
        .whenCompleteAsync((result, e) -> {
          if (e != null) {
            failureCallback.onFailure(e);
          }
        })
    );
  }
}
