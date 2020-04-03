package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;

import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс для параметров CRUD операций
 */
abstract class JPBaseCRUD {
  private final Source source;
  private final Map<String, String> props = new HashMap<>();

  JPBaseCRUD(Source source) {
    this.source = source;
  }

  /**
   * Источник данных
   *
   * @return Источник данных
   */
  public Source getSource() {
    return source != null ? source : Source.SYSTEM;
  }

  /**
   * Добавляет дополнительное свойство
   *
   * @param key   Ключ
   * @param value Значение
   */
  public void setProperty(String key, String value) {
    props.put(key, value);
  }

  /**
   * Возвращает дополнительное свойство
   *
   * @param key Ключ
   * @return Значение
   */
  public String getProperty(String key) {
    return props.get(key);
  }

  /**
   * Признак заполнености свойства
   *
   * @param key Ключ
   * @return Да/Нет
   */
  public boolean containsProperty(String key) {
    return props.containsKey(key);
  }
}