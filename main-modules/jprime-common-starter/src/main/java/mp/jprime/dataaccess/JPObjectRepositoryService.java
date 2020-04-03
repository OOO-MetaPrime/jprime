package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

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
  default JPObject getObject(JPSelect select) {
    Collection<JPObject> result = getList(select);
    return result != null && !result.isEmpty() ? result.iterator().next() : null;
  }

  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select Параметры для выборки
   * @return Количество в выборке
   */
  default Mono<Long> getAsyncTotalCount(JPSelect select) {
    return Mono.create(x -> {
      try {
        x.success(getTotalCount(select));
      } catch (Exception e) {
        x.error(e);
      }
    });
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
  <T extends JPObject> Collection<T> getList(JPSelect select);

  /**
   * Создает объект
   *
   * @param query Параметры для создания
   * @return Идентификатор созданного объекта
   */
  default Mono<JPId> asyncCreate(JPCreate query) {
    return Mono.create(x -> {
      try {
        x.success(create(query));
      } catch (Exception e) {
        x.error(e);
      }
    });
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
    return Mono.create(x -> {
      try {
        x.success(createAndGet(query));
      } catch (Exception e) {
        x.error(e);
      }
    });
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
    return Mono.create(x -> {
      try {
        x.success(update(query));
      } catch (Exception e) {
        x.error(e);
      }
    });
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
    return Mono.create(x -> {
      try {
        x.success(updateAndGet(query));
      } catch (Exception e) {
        x.error(e);
      }
    });
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
    return Mono.create(x -> {
      try {
        x.success(delete(query));
      } catch (Exception e) {
        x.error(e);
      }
    });
  }

  /**
   * Удаляет объект
   *
   * @param query Парамеры для удаления
   * @return Количество удаленных объектов
   */
  Long delete(JPDelete query);
}
