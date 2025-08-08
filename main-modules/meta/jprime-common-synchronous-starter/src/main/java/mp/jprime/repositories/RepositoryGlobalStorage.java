package mp.jprime.repositories;

import mp.jprime.repositories.services.RepositoryStorage;

import java.util.Collection;

/**
 * Описание всех хранилищ системы
 */
public interface RepositoryGlobalStorage extends RepositoryStorage<JPStorage> {
  /**
   * Возвращает список всех хранилищ
   *
   * @return Список всех хранилищ
   */
  Collection<JPStorage> getStorages();
}
