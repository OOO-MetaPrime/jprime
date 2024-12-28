package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс создания/изменения объекта
 */
public interface JPSyncObjectRepositoryService {
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
  Long getTotalCount(JPSelect select);

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
   * @return Созданные объект
   */
  JPObject createAndGet(JPCreate query);

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
  JPObject updateAndGet(JPUpdate query);

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
   * @return Созданные объект
   */
  JPObject patchAndGet(JPCreate query);

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
  Long delete(JPConditionalDelete query);

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
  void batch(JPBatchUpdate query);

  /**
   * Возвращает код хранилища для переданного {@code classCode}
   *
   * @param classCode код мета класса
   * @return код хранилища
   */
  Optional<String> getStorageCode(String classCode);
}
