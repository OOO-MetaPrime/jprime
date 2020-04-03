package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class JPMutableData {
  private final Map<String, Object> dataMap;

  /**
   * Конструктор
   *
   * @param dataMap Данные объекта
   */
  public JPMutableData(Map<String, Object> dataMap) {
    this.dataMap = new HashMap<>(dataMap);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public <T> T get(JPAttr attr) {
    return (T) dataMap.get(attr.getCode());
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public <T> T get(String attr) {
    return (T) dataMap.get(attr);
  }

  /**
   * Удаляет данные
   *
   * @return Данные
   */
  public <T> T remove(JPAttr attr) {
    return (T) dataMap.remove(attr.getCode());
  }

  /**
   * Удаляет данные
   *
   * @return Данные
   */
  public <T> T remove(String attr) {
    return (T) dataMap.remove(attr);
  }

  /**
   * Возвращает признак наличия данных
   *
   * @return Да/Нет
   */
  public boolean containsKey(JPAttr attr) {
    return dataMap.containsKey(attr.getCode());
  }

  /**
   * Возвращает признак наличия данных
   *
   * @return Да/Нет
   */
  public boolean containsKey(String attr) {
    return dataMap.containsKey(attr);
  }

  /**
   * Сохраняет данные
   *
   * @param key   Ключ
   * @param value Значение
   * @return Значение
   */
  public <T> T put(String key, Object value) {
    return (T) dataMap.put(key, value);
  }

  /**
   * Сохраняет данные
   *
   * @param attr  JPAttr
   * @param value Значение
   * @return Значение
   */
  public <T> T put(JPAttr attr, Object value) {
    return put(attr.getCode(), value);
  }

  /**
   * Реализация итератора
   *
   * @param action BiConsumer
   */
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    dataMap.forEach(action);
  }

  /**
   * Сохраняет данные
   *
   * @param attr  JPAttr
   * @param value Значение
   * @return Значение
   */
  public Object putIfAbsent(JPAttr attr, Object value) {
    return dataMap.putIfAbsent(attr.getCode(), value);
  }

  /**
   * Сохраняет данные
   *
   * @param key   Ключ
   * @param value Значение
   * @return Значение
   */
  public <T> T putIfAbsent(String key, Object value) {
    return (T) dataMap.putIfAbsent(key, value);
  }

  /**
   * Возвращает entrySet
   *
   * @return entrySet
   */
  public Set<Map.Entry<String, Object>> entrySet() {
    return dataMap.entrySet();
  }
}
