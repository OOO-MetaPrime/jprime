package mp.jprime.repositories.services;

import mp.jprime.repositories.JPStorage;
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
public final class RepositoryGlobalStorage implements RepositoryStorage<JPStorage> {
  private final Map<String, JPStorage> storages = new ConcurrentHashMap<>();

  /**
   * Возвращает хранилище по его коду
   *
   * @param code Код
   * @return Хранилище
   */
  @Override
  public JPStorage getStorage(String code) {
    return code != null ? storages.get(code) : null;
  }

  /**
   * Возвращает список всех хранилищ
   *
   * @return Список всех хранилищ
   */
  public Collection<JPStorage> getStorages() {
    return storages.values();
  }

  /**
   * Размещает метаописание в хранилище
   */
  private RepositoryGlobalStorage(@Autowired(required = false) Collection<RepositoryLoader<? extends JPStorage>> loaders) {
    if (loaders != null) {
      for (RepositoryLoader<? extends JPStorage> loader : loaders) {
        loader.getStorages().forEach(x -> this.storages.put(x.getCode(), x));
      }
    }
  }
}
