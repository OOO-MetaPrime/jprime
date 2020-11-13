package mp.jprime.repositories.services;

import mp.jprime.repositories.JPStorage;

/**
 * Описание всех хранилищ системы
 */
public interface RepositoryStorage<T extends JPStorage> {
  /**
   * Возвращает хранилище по его коду
   *
   * @param code Код
   * @return Хранилище
   */
  T getStorage(String code);
}
