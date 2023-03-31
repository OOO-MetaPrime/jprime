package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
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
   * @return Идентификатор обновляемого объекта
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
   * Возвращает код хранилища для переданного {@code classCode}
   *
   * @param classCode код мета класса
   * @return код хранилища
   */
  Optional<String> getStorageCode(String classCode);
}
