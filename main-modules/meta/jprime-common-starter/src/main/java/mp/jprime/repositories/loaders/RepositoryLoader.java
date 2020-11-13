package mp.jprime.repositories.loaders;

import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.loaders.beans.RepositorySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.*;

/**
 * Загрузчик репозиториев
 */
public interface RepositoryLoader<T extends JPStorage> {
  Logger LOG = LoggerFactory.getLogger(RepositoryLoader.class);

  /**
   * Префикс настроек репозитория
   */
  String PREFIX = "jprime.storage.";
  /**
   * Блок типа
   */
  String TYPE_BLOCK = "type";
  /**
   * Блок синонимов
   */
  String ALIASES_BLOCK = "aliases";

  /**
   * Возвращает все хранилища
   *
   * @return Все хранилища
   */
  Collection<T> getStorages();

  /**
   * Возвращает настройки для хранилищ с указанным типом
   *
   * @param env       Окружение
   * @param checkType ТИп хранилища
   * @return Список настроек
   */
  default Map<String, RepositorySettings> getPropertiesByType(Environment env, String checkType) {
    Set<String> props = new HashSet<>();

    AbstractEnvironment aEnv = (AbstractEnvironment) env;
    aEnv.getPropertySources().forEach(x -> {
      if (x instanceof MapPropertySource) {
        Map<String, Object> source = ((MapPropertySource) x).getSource();

        props.addAll(source.keySet());
      }
    });

    Map<String, Map<String, String>> preProps = new HashMap<>();
    Map<String, Map<String, String>> preAliases = new HashMap<>();
    for (String key : props) {
      if (!key.startsWith(PREFIX)) {
        continue;
      }
      String[] blocks = key.split("\\.");
      String code = blocks[2];
      String block = blocks[3];
      if (ALIASES_BLOCK.equals(block)) {
        String alias = blocks.length > 4 ? blocks[4] : null;
        if (alias != null) {
          preAliases.computeIfAbsent(code, x -> new HashMap<>(4)).put(alias, env.getProperty(key));
        }
      } else {
        preProps.computeIfAbsent(code, x -> new HashMap<>(4)).put(block, env.getProperty(key));
      }
    }

    Map<String, RepositorySettings> result = new HashMap<>();
    for (Map.Entry<String, Map<String, String>> entry : preProps.entrySet()) {
      String code = entry.getKey();
      Map<String, String> values = entry.getValue();

      if (!checkType.equals(values.get(TYPE_BLOCK))) {
        continue;
      }
      result.put(code, RepositorySettings.getInstance(values, preAliases.get(code)));
    }
    return result;
  }
}
