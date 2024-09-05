package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс создания/изменения объекта
 */
public interface JPObjectRepositoryService {
  /**
   * Возвращает объект
   *
   * @param select Параметры для выборки
   * @return Объект
   */
  default Mono<JPObject> getAsyncObject(JPSelect select) {
    return getAsyncList(select).singleOrEmpty();
  }

  /**
   * Возвращает объект
   *
   * @param select Параметры для выборки
   * @return Объект
   */
  JPObject getObject(JPSelect select);

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  JPObject getObjectAndLock(JPSelect query);

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param query      Параметры для выборки
   * @param skipLocked Признак пропуска заблокированных объектов
   * @return Объект
   */
  JPObject getObjectAndLock(JPSelect query, boolean skipLocked);

  /**
   * Возвращает optional результата запроса
   *
   * @param select Параметры для выборки
   * @return optional
   */
  default Optional<JPObject> getOptionalObject(JPSelect select) {
    return Optional.ofNullable(getObject(select));
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select Параметры для выборки
   * @return Количество в выборке
   */
  default Mono<Long> getAsyncTotalCount(JPSelect select) {
    return Mono.fromCallable(() -> getTotalCount(select));
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select Параметры для выборки
   * @return Количество в выборке
   */
  Long getTotalCount(JPSelect select);

  /**
   * Возвращает список объектов
   *
   * @param select Параметры для выборки
   * @return Список объектов
   */
  Flux<JPObject> getAsyncList(JPSelect select);

  /**
   * Возвращает список объектов
   *
   * @param select Параметры для выборки
   * @return Список объектов
   */
  Collection<JPObject> getList(JPSelect select);

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param query Параметры для выборки
   * @return Список объектов
   */
  Collection<JPObject> getListAndLock(JPSelect query);

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param query      Параметры для выборки
   * @param skipLocked Признак пропуска заблокированных объектов
   * @return Список объектов
   */
  Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked);

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
   * @return Идентификатор созданного объекта
   */
  JPId create(JPCreate query);

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
   * @return Созданные объект
   */
  JPObject createAndGet(JPCreate query);

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
   * @return Идентификатор обновляемого объекта
   */
  JPId update(JPUpdate query);

  /**
   * Обновляем объекты по условию
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  default Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return Mono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  JPObject updateAndGet(JPUpdate query);

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
   * Обновляем объекты по условию
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  Long update(JPConditionalUpdate query);

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  JPId patch(JPCreate query);

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  default Mono<JPId> asyncPatch(JPCreate query) {
    return Mono.fromCallable(() -> patch(query));
  }

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  JPObject patchAndGet(JPCreate query);

  /**
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  default Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return Mono.fromCallable(() -> patchAndGet(query));
  }

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
   * Удаляет объекты по условию
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  default Mono<Long> asyncDelete(JPConditionalDelete query) {
    return Mono.fromCallable(() -> delete(query));
  }

  /**
   * Удаляет объекты по условию
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  Long delete(JPConditionalDelete query);

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
  Mono<Void> asyncBatch(JPBatchCreate query);

  /**
   * Создает объекты
   * <p>
   * Прямые и обратные ссылки не учитываются
   * {@link JPClassHandler#beforeCreate(JPCreate)} и {@link JPClassHandler#afterCreate(Comparable, JPCreate)} не учитываются
   *
   * @param query Параметры для создания
   * @throws JPRuntimeException, когда:
   *                             1) query == null
   *                             2) Между батчами есть отличия в атрибутах
   */
  void batch(JPBatchCreate query);


  /**
   * Создает объекты и возвращает идентификаторы созданных объектов
   * <p>
   * Прямые и обратные ссылки не учитываются
   * {@link JPClassHandler#beforeCreate(JPCreate)} и {@link JPClassHandler#afterCreate(Comparable, JPCreate)} не учитываются
   *
   * @param query Параметры для создания
   * @throws JPRuntimeException, когда:
   *                             1) query == null
   *                             2) Между батчами есть отличия в атрибутах
   */
  <T> List<T> batchWithKeys(JPBatchCreate query);

  /**
   * Обновляет объекты
   * <p>
   * Прямые и обратные ссылки не учитываются,
   * {@link JPClassHandler#beforeUpdate(JPUpdate)} и {@link JPClassHandler#afterUpdate(JPUpdate)} не учитываются
   *
   * @param query запрос
   */
  Mono<Void> asyncBatch(JPBatchUpdate query);

  /**
   * Обновляет объекты
   * <p>
   * Прямые и обратные ссылки не учитываются,
   * {@link JPClassHandler#beforeUpdate(JPUpdate)} и {@link JPClassHandler#afterUpdate(JPUpdate)} не учитываются
   *
   * @param query запрос
   */
  void batch(JPBatchUpdate query);

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  Mono<JPObject> getAsyncObjectAndLock(JPSelect query);

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param query Параметры для выборки
   * @return Список объектов
   */
  Flux<JPObject> getAsyncListAndLock(JPSelect query);

  /**
   * Возвращает код хранилища для переданного {@code classCode}
   *
   * @param classCode код мета класса
   * @return код хранилища
   */
  Optional<String> getStorageCode(String classCode);
}
