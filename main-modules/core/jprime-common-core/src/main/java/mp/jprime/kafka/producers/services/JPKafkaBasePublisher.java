package mp.jprime.kafka.producers.services;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.kafka.producers.JPKafkaPublisher;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;
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
  private final ListenableFutureCallback<SendResult<K, V>> ERROR_LOG_CALLBACK = new ListenableFutureCallback<SendResult<K, V>>() {
    @Override
    public void onSuccess(SendResult<K, V> result) {
    }

    @Override
    public void onFailure(Throwable e) {
      LOG.error(e.getMessage(), e);
    }
  };

  @Value("${smev.coordinator.kafka.producers.timeout:5000}")
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
        .map(event -> toProducerRecord(getTopic(event), event))
        .collect(Collectors.toList());
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
   * @return ключ {@link ProducerRecord}
   */
  protected K toKey(E event) {
    return null;
  }

  /**
   * Конвертируем событие в значение {@link ProducerRecord}
   *
   * @param event событие
   * @return значение {@link ProducerRecord}
   */
  protected abstract V toValue(E event);

  private void doSend(Collection<ProducerRecord<K, V>> events) {
    AtomicReference<Throwable> error = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(events.size());

    ListenableFutureCallback<SendResult<K, V>> callback =
        new ListenableFutureCallback<SendResult<K, V>>() {
          @Override
          public void onSuccess(SendResult<K, V> result) {
            latch.countDown();
          }

          @Override
          public void onFailure(Throwable e) {
            error.set(e);
            latch.countDown();
            LOG.error(e.getMessage(), e);
          }
        };

    KafkaOperations<K, V> kafkaOperations = getKafkaOperations();
    events.forEach(record -> kafkaOperations.send(record).addCallback(callback));
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
    events.forEach(record -> kafkaOperations.send(record).addCallback(ERROR_LOG_CALLBACK));
  }

  private void doSendAsync(Collection<ProducerRecord<K, V>> events, FailureCallback failureCallback) {
    KafkaOperations<K, V> kafkaOperations = getKafkaOperations();
    events.forEach(record -> kafkaOperations.send(record).addCallback(r -> {
    }, failureCallback));
  }
}
