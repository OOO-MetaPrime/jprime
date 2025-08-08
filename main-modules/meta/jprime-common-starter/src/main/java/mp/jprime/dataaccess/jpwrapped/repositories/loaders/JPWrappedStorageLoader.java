package mp.jprime.dataaccess.jpwrapped.repositories.loaders;

import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorage;
import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorageAlias;
import mp.jprime.dataaccess.jpwrapped.repositories.JPWrappedStorageBase;
import mp.jprime.repositories.loaders.RepositoryLoader;
import mp.jprime.repositories.loaders.beans.RepositorySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Загрузчик хранилищ типа Переопределенное
 */
@Service
public final class JPWrappedStorageLoader implements RepositoryLoader<JPWrappedStorage> {
  /**
   * Окружение
   */
  private final Environment env;

  /**
   * Конструктор
   *
   * @param env Environment
   */
  private JPWrappedStorageLoader(@Autowired Environment env) {
    this.env = env;
  }

  /**
   * Возвращает все хранилища
   *
   * @return Все хранилища
   */
  @Override
  public Collection<JPWrappedStorage> getStorages() {
    Map<String, RepositorySettings> props = getPropertiesByType(env, JPWrappedStorage.CODE);
    if (props == null || props.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPWrappedStorage> result = new ArrayList<>(props.size());
    for (Map.Entry<String, RepositorySettings> entry : props.entrySet()) {
      RepositorySettings values = entry.getValue();

      String code = entry.getKey();
      String title = values.getProp("title");

      JPWrappedStorage generatedStorage = new JPWrappedStorageBase(code, title);

      // Основное подключение
      result.add(generatedStorage);
      // Подключение как alias
      for (String alias : values.getAliases()) {
        result.add(new JPWrappedStorageAlias(alias, values.getAliasTitle(alias), generatedStorage));
      }
    }
    return result;
  }
}
