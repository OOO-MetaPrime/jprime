package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import org.apache.commons.lang3.NotImplementedException;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс создания/изменения объекта
 */
public interface JPObjectRepository extends JPReactiveObjectRepository {
  /**
   * Возвращает объект
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  default JPObject getObject(JPSelect query) {
    Collection<JPObject> result = getList(query);
    return result != null && !result.isEmpty() ? result.iterator().next() : null;
  }

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  default JPObject getObjectAndLock(JPSelect query) {
    return getObject(query);
  }

  /**
   * Возвращает optional результата запроса
   *
   * @param query Параметры для выборки
   * @return optional
   */
  default Optional<JPObject> getOptionalObject(JPSelect query) {
    return Optional.ofNullable(getObject(query));
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param query Параметры для выборки
   * @return Количество в выборке
   */
  default Mono<Long> getAsyncTotalCount(JPSelect query) {
    return Mono.fromCallable(() -> getTotalCount(query));
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param query Параметры для выборки
   * @return Количество в выборке
   */
  Long getTotalCount(JPSelect query);

  /**
   * Возвращает список объектов
   *
   * @param query Параметры для выборки
   * @return Список объектов
   */
  Collection<JPObject> getList(JPSelect query);

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param query Параметры для выборки
   * @return Список объектов
   */
  default Collection<JPObject> getListAndLock(JPSelect query) {
    return getList(query);
  }

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param query      Параметры для выборки
   * @param skipLocked Признак пропуска заблокированных объектов
   * @return Список объектов
   */
  default Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked) {
    return getListAndLock(query);
  }

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  default Mono<JPId> asyncCreate(JPCreate query) {
    return Mono.fromCallable(() -> create(query));
  }

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  JPId create(JPCreate query);

  /**
   * Возвращает результаты агрегации
   *
   * @param aggr Параметры для выборки
   * @return Список объектов
   */
  default Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return Mono.fromCallable(() -> getAggregate(aggr));
  }

  /**
   * Возвращает результаты агрегации
   *
   * @param aggr Параметры для выборки
   * @return Список объектов
   */
  JPData getAggregate(JPAggregate aggr);

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return Mono.fromCallable(() -> createAndGet(query));
  }

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  JPObject createAndGet(JPCreate query);

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновляемого объекта
   */
  default Mono<JPId> asyncUpdate(JPUpdate query) {
    return Mono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновленного объекта
   */
  JPId update(JPUpdate query);

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  default Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return Mono.fromCallable(() -> updateAndGet(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  JPObject updateAndGet(JPUpdate query);

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPDelete query) {
    return Mono.fromCallable(() -> delete(query));
  }

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  Long delete(JPDelete query);

  /**
   * Создает объекты
   * <p>
   * Прямые и обратные ссылки не учитываются
   * {@link JPClassHandler#beforeCreate(JPCreate)} и {@link JPClassHandler#afterCreate(Comparable, JPCreate)} не учитываются
   *
   * @param query Параметры для создания
   * @return Void
   * @throws JPRuntimeException, когда:
   *                             1) queries == null
   *                             2) Между батчами есть отличия в атрибутах
   */
  default Mono<Void> asyncBatch(JPBatchCreate query) {
    return Mono.fromRunnable(() -> batch(query));
  }

  /**
   * Создает объекты
   * <p>
   * Прямые и обратные ссылки не учитываются
   * {@link JPClassHandler#beforeCreate(JPCreate)} и {@link JPClassHandler#afterCreate(Comparable, JPCreate)} не учитываются
   *
   * @param query Параметры для создания
   * @throws JPRuntimeException, когда:
   *                             1) queries == null
   *                             2) Между батчами есть отличия в атрибутах
   */
  default void batch(JPBatchCreate query) {
    throw new NotImplementedException();
  }

  /**
   * Обновляет объекты
   * <p>
   * Прямые и обратные ссылки не учитываются,
   * {@link JPClassHandler#beforeUpdate(JPUpdate)} и {@link JPClassHandler#afterUpdate(JPUpdate)} не учитываются
   *
   * @param query запрос
   */
  default Mono<Void> asyncBatch(JPBatchUpdate query) {
    return Mono.fromRunnable(() -> batch(query));
  }

  /**
   * Обновляет объекты
   * <p>
   * Прямые и обратные ссылки не учитываются,
   * {@link JPClassHandler#beforeUpdate(JPUpdate)} и {@link JPClassHandler#afterUpdate(JPUpdate)} не учитываются
   *
   * @param query запрос
   */
  default void batch(JPBatchUpdate query) {
    throw new NotImplementedException();
  }
}
