package mp.jprime.dataaccess.jpwrapped.repositories.services;

import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Описание Переопределяемых хранилищ системы
 */
@Service
public final class WrappedRepositoryStorage implements RepositoryStorage<JPWrappedStorage> {
  /**
   * Описание всех хранилищ системы
   */
  private RepositoryGlobalStorage repositoryStorage;
  private volatile Collection<JPWrappedStorage> storageList;

  private WrappedRepositoryStorage() {

  }

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  /**
   * Возвращает хранилище по его коду
   *
   * @param code Код
   * @return Хранилище
   */
  @Override
  public JPWrappedStorage getStorage(String code) {
    JPStorage result = code != null ? repositoryStorage.getStorage(code) : null;
    return result instanceof JPWrappedStorage ? (JPWrappedStorage) result : null;
  }

  /**
   * Возвращает все хранилища текущего типа
   *
   * @return Хранилище
   */
  public Collection<JPWrappedStorage> getStorages() {
    if (storageList == null) {
      Collection<JPWrappedStorage> result = repositoryStorage.getStorages()
          .stream()
          .filter(x -> x instanceof JPWrappedStorage)
          .map(x -> (JPWrappedStorage) x)
          .collect(Collectors.toList());
      storageList = result;
      return result;
    }
    return storageList;
  }
}