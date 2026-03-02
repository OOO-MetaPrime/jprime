package mp.jprime.dataaccess.jpwrapped.repositories.loaders;

import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorage;
import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorageBase;
import mp.jprime.repositories.loaders.RepositoryLoader;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Загрузчик хранилищ типа Переопределенное
 */
@Service
public final class JPWrappedStorageLoader implements RepositoryLoader<JPWrappedStorage> {
  @Override
  public Collection<JPWrappedStorage> getStorages() {
    return List.of(
        new JPWrappedStorageBase(JPWrappedStorage.CODE, "Переопределенное хранилище данных")
    );
  }
}