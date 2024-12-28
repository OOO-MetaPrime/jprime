package mp.jprime.metastorage;

import mp.jprime.repositories.JPStorage;

/**
 * Сервис работы с хранилищами
 */
public interface JPMetaStorageService {
  /**
   * Возвращает хранилище для метаописания
   *
   * @param classCode Кодовое имя класса
   * @return Хранилище для объектов класса
   */
  JPStorage getJpStorage(String classCode);
}
