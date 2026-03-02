package mp.jprime.dataaccess.generated.repositories.loaders;

import mp.jprime.dataaccess.generated.repositories.GeneratedStorage;
import mp.jprime.dataaccess.generated.repositories.GeneratedStorageBase;
import mp.jprime.repositories.loaders.RepositoryLoader;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Загрузчик хранилищ типа Управляемое
 */
@Service
public final class GeneratedStorageLoader implements RepositoryLoader<GeneratedStorage> {
  /**
   * Возвращает все хранилища
   *
   * @return Все хранилища
   */
  @Override
  public Collection<GeneratedStorage> getStorages() {
    return List.of(
        new GeneratedStorageBase(GeneratedStorage.CODE, "Настраиваемое  хранилище данных")
    );
  }
}
