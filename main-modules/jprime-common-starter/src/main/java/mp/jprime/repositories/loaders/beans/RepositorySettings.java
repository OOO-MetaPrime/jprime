package mp.jprime.repositories.loaders.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Настройки подключения к хранилищу
 */
public class RepositorySettings {
  // Общие настройки
  private final Map<String, String> settings = new HashMap<>();
  // Алиасы
  private final Map<String, String> aliases = new HashMap<>();

  private RepositorySettings(Map<String, String> settings, Map<String, String> aliases) {
    if (settings != null) {
      this.settings.putAll(settings);
    }
    if (aliases != null) {
      this.aliases.putAll(aliases);
    }
  }

  /**
   * Создание настройки подключения
   *
   * @param settings Общие настройки
   * @param aliases  Алиасы
   * @return Настройки подключения к хранилищу
   */
  public static RepositorySettings getInstance(Map<String, String> settings, Map<String, String> aliases) {
    return new RepositorySettings(settings, aliases);
  }


  /**
   * Возвращает значение свойства
   *
   * @param key Ключ
   * @return Значение
   */
  public String getProp(String key) {
    return settings.get(key);
  }

  /**
   * Возвращает список алиасов
   *
   * @return Список синонимов
   */
  public Collection<String> getAliases() {
    return aliases.keySet();
  }

  /**
   * Возвращает название синонима
   *
   * @param key алиас
   * @return название синонима
   */
  public String getAliasTitle(String key) {
    return aliases.get(key);
  }
}
