package mp.jprime.repositories.services;

import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.repositories.loaders.RepositoryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Описание всех хранилищ системы
 */
@Service
@Primary
public final class RepositoryGlobalCommonStorage implements RepositoryGlobalStorage {
  private final Map<String, JPStorage> storages = new ConcurrentHashMap<>();

  @Override
  public JPStorage getStorage(String code) {
    return code != null ? storages.get(code) : null;
  }

  @Override
  public Collection<JPStorage> getStorages() {
    return storages.values();
  }

  /**
   * Размещает метаописание в хранилище
   */
  private RepositoryGlobalCommonStorage(@Autowired(required = false) Collection<RepositoryLoader<? extends JPStorage>> loaders) {
    if (loaders != null) {
      for (RepositoryLoader<? extends JPStorage> loader : loaders) {
        loader.getStorages().forEach(x -> this.storages.put(x.getCode(), x));
      }
    }
  }
}
