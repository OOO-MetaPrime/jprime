package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Интерфейс создания/изменения объекта
 */
public interface JPObjectRepository extends JPReactiveObjectRepository, JPSyncObjectRepository {

  default Flux<JPObject> getAsyncList(JPSelect query) {
    return Mono.fromCallable(() -> getList(query))
        .flatMapMany(Flux::fromIterable)
        .subscribeOn(getReactorScheduler());
  }

  default Flux<JPObject> getAsyncListAndLock(JPSelect query) {
    return Mono.fromCallable(() -> getListAndLock(query))
        .flatMapMany(Flux::fromIterable)
        .subscribeOn(getReactorScheduler());
  }

  default Mono<Long> getAsyncTotalCount(JPSelect query) {
    return Mono.fromCallable(() -> getTotalCount(query))
        .subscribeOn(getReactorScheduler());
  }

  default Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return Mono.fromCallable(() -> getAggregate(aggr))
        .subscribeOn(getReactorScheduler());
  }

  default Mono<JPId> asyncCreate(JPCreate query) {
    return Mono.fromCallable(() -> create(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return Mono.fromCallable(() -> createAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновляемого объекта
   */
  default Mono<JPId> asyncUpdate(JPUpdate query) {
    return Mono.fromCallable(() -> update(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Обновляем объекты по условию
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  default Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return Mono.fromCallable(() -> update(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  default Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return Mono.fromCallable(() -> updateAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  default Mono<JPId> asyncPatch(JPCreate query) {
    return Mono.fromCallable(() -> patch(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return Mono.fromCallable(() -> patchAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPDelete query) {
    return Mono.fromCallable(() -> delete(query))
        .subscribeOn(getReactorScheduler());
  }

  /**
   * Удаляет объекты по условию
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPConditionalDelete query) {
    return Mono.fromCallable(() -> delete(query))
        .subscribeOn(getReactorScheduler());
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
    return Mono.<Void>fromRunnable(() -> batch(query))
        .subscribeOn(getReactorScheduler());
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
    return Mono.<Void>fromRunnable(() -> batch(query))
        .subscribeOn(getReactorScheduler());
  }
}
