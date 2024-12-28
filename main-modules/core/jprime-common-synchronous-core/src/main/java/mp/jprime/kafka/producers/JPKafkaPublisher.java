package mp.jprime.kafka.producers;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Сервис отправки событий в Kafka
 *
 * @param <E> тип события
 */
public interface JPKafkaPublisher<E> {

  /**
   * Отправить события (с гарантией доставки)
   *
   * @param events события
   */
  void publish(Collection<E> events);

  /**
   * Отправить события без гарантии доставки
   * <p>
   * В случае ошибки отправки события ошибка будет залогирована
   *
   * @param events события
   */
  void publishAsync(Collection<E> events);

  /**
   * Отправить события без гарантии доставки
   * <p>
   * В случае ошибки отправки события будет выполнен переданный {@link Consumer}
   *
   * @param events          события
   * @param failureCallback действие в случае ошибки
   */
  void publishAsync(Collection<E> events, Consumer<Throwable> failureCallback);
}
