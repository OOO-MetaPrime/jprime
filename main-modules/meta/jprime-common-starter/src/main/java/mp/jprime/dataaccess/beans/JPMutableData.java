package mp.jprime.dataaccess.beans;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.meta.JPAttr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Изменяемые данные
 */
public final class JPMutableData implements JPAttrData {
  private final Map<String, Object> dataMap;

  /**
   * Конструктор
   *
   * @param dataMap Данные
   */
  private JPMutableData(Map<String, Object> dataMap) {
    this.dataMap = dataMap != null ? new HashMap<>(dataMap) : new HashMap<>();
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
   * Признак отсутствия данных
   *
   * @return Да/Нет
   */
  @Override
  public boolean isEmpty() {
    return dataMap.isEmpty();
  }

  /**
   * Размер данных
   *
   * @return Размер данных
   */
  @Override
  public int size() {
    return dataMap.size();
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
  public boolean containsAttr(String attr) {
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
   * @param data Данные для добавления
   * @return Значение
   */
  public void putAll(JPData data) {
    if (dataMap == null || dataMap.isEmpty()) {
      return;
    }
    data.forEach(dataMap::put);
  }

  /**
   * Сохраняет данные
   *
   * @param data Данные для добавления
   * @return Значение
   */
  public void putAll(JPMutableData data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    data.forEach(dataMap::put);
  }

  /**
   * Сохраняет данные
   *
   * @param data Даныне
   */
  public void putAll(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    data.forEach(dataMap::put);
  }

  /**
   * Сохраняет данные
   *
   * @param key             Ключ
   * @param mappingFunction Вычисление значения
   * @return Значение
   */
  public <T> T computeIfAbsent(String key, Function<String, ? extends Object> mappingFunction) {
    return (T) dataMap.computeIfAbsent(key, mappingFunction);
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
  @Override
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
   * Сохраняет данные
   *
   * @param data Даныне
   */
  public void putIfAbsent(Map<String, Object> data) {
    if (data == null) {
      return;
    }
    data.forEach(this::putIfAbsent);
  }

  /**
   * Сохраняет данные
   *
   * @param data Данные
   */
  public void putIfAbsent(JPData data) {
    if (data == null) {
      return;
    }
    data.forEach(this::putIfAbsent);
  }

  /**
   * Сохраняет данные
   *
   * @param data Данные
   */
  public void putIfAbsent(JPMutableData data) {
    if (data == null) {
      return;
    }
    data.forEach(this::putIfAbsent);
  }


  /**
   * Возвращает entrySet
   *
   * @return entrySet
   */
  public Set<Map.Entry<String, Object>> entrySet() {
    return dataMap.entrySet();
  }

  /**
   * Построитель JPMutableData
   *
   * @return JPMutableData
   */
  public static JPMutableData empty() {
    return new JPMutableData(null);
  }

  /**
   * Построитель JPMutableData
   *
   * @param data Данные
   * @return JPMutableData
   */
  public static JPMutableData of(Map<String, Object> data) {
    return new JPMutableData(data);
  }

  /**
   * Построитель JPMutableData
   *
   * @param key   Ключ
   * @param value Значение
   * @return JPMutableData
   */
  public static JPMutableData of(String key, Object value) {
    JPMutableData data = JPMutableData.empty();
    data.put(key, value);
    return data;
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public Map<String, Object> toMap() {
    return new HashMap<>(dataMap);
  }
}