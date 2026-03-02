package mp.jprime.dataaccess.beans;

import mp.jprime.lang.JPMutableMap;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPAttr;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Изменяемые данные
 */
public final class JPMutableData implements JPAttrData, JPMutableMap {
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
   * Конструктор
   */
  private JPMutableData() {
    this.dataMap = new HashMap<>();
  }

  /**
   * Конструктор
   *
   * @param dataMap Данные
   */
  private JPMutableData(JPMap dataMap) {
    this.dataMap = dataMap != null ? new HashMap<>(dataMap.toMap()) : new HashMap<>();
  }

  /**
   * Построитель JPMutableData
   *
   * @return JPMutableData
   */
  public static JPMutableData empty() {
    return new JPMutableData();
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
   * @param data JPMap
   * @return JPMutableData
   */
  public static JPMutableData of(JPMap data) {
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

  @Override
  public <T> T get(String attr) {
    return (T) dataMap.get(attr);
  }

  @Override
  public boolean isEmpty() {
    return dataMap.isEmpty();
  }

  @Override
  public int size() {
    return dataMap.size();
  }

  @Override
  public boolean containsAttr(String attr) {
    return dataMap.containsKey(attr);
  }

  @Override
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    dataMap.forEach(action);
  }

  @Override
  public Map<String, Object> toMap() {
    return new HashMap<>(dataMap);
  }

  @Override
  public Set<String> keySet() {
    return dataMap.keySet();
  }

  @Override
  public Collection<Object> values() {
    return dataMap.values();
  }

  @Override
  public <T> T put(String key, Object value) {
    return (T) dataMap.put(key, value);
  }

  @Override
  public <T> T computeIfAbsent(String key, Function<String, ? extends Object> mappingFunction) {
    return (T) dataMap.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public <T> T putIfAbsent(String key, Object value) {
    return (T) dataMap.putIfAbsent(key, value);
  }

  @Override
  public void putIfAbsent(Map<String, Object> data) {
    if (data == null) {
      return;
    }
    data.forEach(this::putIfAbsent);
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return dataMap.entrySet();
  }

  @Override
  public void putAll(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    dataMap.putAll(data);
  }

  @Override
  public <T> T remove(String attr) {
    return (T) dataMap.remove(attr);
  }


  @Override
  public void putAll(JPMap data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    data.forEach(dataMap::put);
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
   * @param data Данные
   */
  public void putIfAbsent(JPMap data) {
    if (data == null) {
      return;
    }
    data.forEach(this::putIfAbsent);
  }
}