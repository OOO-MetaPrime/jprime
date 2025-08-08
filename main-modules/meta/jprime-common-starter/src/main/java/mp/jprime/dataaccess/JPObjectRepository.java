package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.reactor.core.publisher.JPMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Интерфейс создания/изменения объекта
 */
public interface JPObjectRepository extends JPReactiveObjectRepository, JPSyncObjectRepository {

  default Flux<JPObject> getAsyncList(JPSelect query) {
    return JPMono.fromCallable(() -> getList(query))
        .flatMapMany(Flux::fromIterable);
  }

  default Flux<JPObject> getAsyncListAndLock(JPSelect query) {
    return JPMono.fromCallable(() -> getListAndLock(query))
        .flatMapMany(Flux::fromIterable);
  }

  default Mono<Long> getAsyncTotalCount(JPSelect query) {
    return JPMono.fromCallable(() -> getTotalCount(query));
  }

  default Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return JPMono.fromCallable(() -> getAggregate(aggr));
  }

  default Mono<JPId> asyncCreate(JPCreate query) {
    return JPMono.fromCallable(() -> create(query));
  }

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return JPMono.fromCallable(() -> createAndGet(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновляемого объекта
   */
  default Mono<JPId> asyncUpdate(JPUpdate query) {
    return JPMono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объекты по условию
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  default Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return JPMono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  default Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return JPMono.fromCallable(() -> updateAndGet(query));
  }

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  default Mono<JPId> asyncPatch(JPCreate query) {
    return JPMono.fromCallable(() -> patch(query));
  }

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return JPMono.fromCallable(() -> patchAndGet(query));
  }

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPDelete query) {
    return JPMono.fromCallable(() -> delete(query));
  }

  /**
   * Удаляет объекты по условию
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPConditionalDelete query) {
    return JPMono.fromCallable(() -> delete(query));
  }

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
    return JPMono.<Void>fromRunnable(() -> batch(query));
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
    return JPMono.<Void>fromRunnable(() -> batch(query));
  }
}
