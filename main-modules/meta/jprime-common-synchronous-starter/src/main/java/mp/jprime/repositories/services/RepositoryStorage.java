package mp.jprime.repositories.services;

import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;

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

  /**
   * Возвращает хранилище по его коду или возвращает exception
   *
   * @param code Код
   * @return Хранилище
   */
  default T getStorageOrThrow(String code) {
    T storage = getStorage(code);

    if (storage == null) {
      throw new JPRepositoryNotFoundException(code);
    }

    return storage;
  }
}
