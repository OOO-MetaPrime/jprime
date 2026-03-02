package mp.jprime.dataaccess.generated.repositories.services;

import mp.jprime.dataaccess.generated.repositories.GeneratedStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Описание управляемых хранилищ системы
 */
@Service
public final class GeneratedRepositoryStorage implements RepositoryStorage<GeneratedStorage> {
  /**
   * Описание всех хранилищ системы
   */
  private RepositoryGlobalStorage repositoryStorage;
  private volatile Collection<GeneratedStorage> storageList;

  private GeneratedRepositoryStorage() {

  }

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public GeneratedStorage getStorage(String code) {
    JPStorage result = code != null ? repositoryStorage.getStorage(code) : null;
    return result instanceof GeneratedStorage ? (GeneratedStorage) result : null;
  }

  /**
   * Возвращает все хранилища текущего типа
   *
   * @return Хранилище
   */
  public Collection<GeneratedStorage> getStorages() {
    if (storageList == null) {
      Collection<GeneratedStorage> result = repositoryStorage.getStorages()
          .stream()
          .filter(x -> x instanceof GeneratedStorage)
          .map(x -> (GeneratedStorage) x)
          .collect(Collectors.toList());
      storageList = result;
      return result;
    }
    return storageList;
  }
}