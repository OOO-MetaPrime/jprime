package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Интерфейс реактивного создания/изменения объекта
 */
public interface JPReactiveObjectRepository {
  /**
   * Возвращает объект
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  default Mono<JPObject> getAsyncObject(JPSelect query) {
    return getAsyncList(query).singleOrEmpty();
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param query Параметры для выборки
   * @return Количество в выборке
   */
  Mono<Long> getAsyncTotalCount(JPSelect query);

  /**
   * Возвращает список объектов
   *
   * @param query Параметры для выборки
   * @return Список объектов
   */
  Flux<JPObject> getAsyncList(JPSelect query);

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  Mono<JPId> asyncCreate(JPCreate query);

  /**
   * Возвращает результаты агрегации
   *
   * @param aggr Параметры для выборки
   * @return Список объектов
   */
  Mono<JPData> getAsyncAggregate(JPAggregate aggr);

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Созданные объект
   */
  Mono<JPObject> asyncCreateAndGet(JPCreate query);

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Идентификатор обновляемого объекта
   */
  Mono<JPId> asyncUpdate(JPUpdate query);

  /**
   * Обновляем объект
   *
   * @param query Параметры для обновления
   * @return Обновленный объект
   */
  Mono<JPObject> asyncUpdateAndGet(JPUpdate query);

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  Mono<Long> asyncDelete(JPDelete query);
}
