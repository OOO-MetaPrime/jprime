package mp.jprime.dataaccess.generated;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import mp.jprime.reactor.core.publisher.JPMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Обработчик настраиваемого хранилища для JPClass
 */
public interface GeneratedJPClassStorage {
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
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param select Параметры для выборки
   * @return Объект
   */
  default Mono<JPObject> getAsyncObjectAndLock(JPSelect select) {
    return getAsyncObject(select);
  }

  /**
   * Возвращает объект
   *
   * @param select Параметры для выборки
   * @return Объект
   */
  default JPObject getObject(JPSelect select) {
    Collection<JPObject> result = getList(select);
    return result != null && !result.isEmpty() ? result.iterator().next() : null;
  }

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param select Параметры для выборки
   * @return Объект
   */
  default JPObject getObjectAndLock(JPSelect select) {
    return getObject(select);
  }

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param select     Параметры для выборки
   * @param skipLocked Признак пропуска заблокированных объектов
   * @return Объект
   */
  default JPObject getObjectAndLock(JPSelect select, boolean skipLocked) {
    return getObject(select);
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select Параметры для выборки
   * @return Количество в выборке
   */
  default Mono<Long> getAsyncTotalCount(JPSelect select) {
    return JPMono.fromCallable(() -> getTotalCount(select));
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
  default Flux<JPObject> getAsyncList(JPSelect select) {
    return JPMono.fromCallable(() -> getList(select))
        .flatMapMany(Flux::fromIterable);
  }

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param select Параметры для выборки
   * @return Список объектов
   */
  default Flux<JPObject> getAsyncListAndLock(JPSelect select) {
    return getAsyncList(select);
  }

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
   * @param select Параметры для выборки
   * @return Список объектов
   */
  default Collection<JPObject> getListAndLock(JPSelect select) {
    return getList(select);
  }

  /**
   * Возвращает список объектов и блокирует на время транзакции
   *
   * @param select     Параметры для выборки
   * @param skipLocked Признак пропуска заблокированных объектов
   * @return Список объектов
   */
  default Collection<JPObject> getListAndLock(JPSelect select, boolean skipLocked) {
    return getListAndLock(select);
  }

  /**
   * Возвращает результаты агрегации
   *
   * @param aggr Параметры для выборки
   * @return Список объектов
   */
  default Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return JPMono.fromCallable(() -> getAggregate(aggr));
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
  default Mono<JPId> asyncCreate(JPCreate query) {
    return JPMono.fromCallable(() -> create(query));
  }

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
   * @return Созданные объект
   */
  default Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return JPMono.fromCallable(() -> createAndGet(query));
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
    return JPMono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновляемого объекта
   */
  JPId update(JPUpdate query);

  /**
   * Обновляем объекты
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  default Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return JPMono.fromCallable(() -> update(query));
  }

  /**
   * Обновляем объекты
   *
   * @param query Параметры для обновления
   * @return Количество обновленных объектов
   */
  Long update(JPConditionalUpdate query);

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
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  JPObject updateAndGet(JPUpdate query);

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
   * @return Идентификатор созданного объекта
   */
  JPId patch(JPCreate query);

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
   * Создает или обновляет объект
   * Метод поддерживается только для меты, где определена логика уникального ключа
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  JPObject patchAndGet(JPCreate query);

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
    return JPMono.fromCallable(() -> delete(query));
  }

  /**
   * Удаляет объекты по условию
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  Long delete(JPConditionalDelete query);
}
