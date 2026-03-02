package mp.jprime.dataaccess.beans;

import mp.jprime.lang.JPMap;
import mp.jprime.lang.JPMutableMap;
import mp.jprime.meta.JPAttr;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Изменяемые данные
 */
public final class JPMutableWrapper implements JPMutableMap {
  private final Map<String, Object> dataMap;

  /**
   * Конструктор
   *
   * @param dataMap Данные
   */
  private JPMutableWrapper(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }

  /**
   * Построитель JPMutableData
   *
   * @param data Данные
   * @return JPMutableData
   */
  public static JPMutableWrapper of(Map<String, Object> data) {
    return new JPMutableWrapper(data);
  }

  @Override
  public boolean containsKey(String field) {
    return dataMap.containsKey(field);
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
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    dataMap.forEach(action);
  }

  @Override
  public Map<String, Object> toMap() {
    return dataMap;
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

  @Override
  public void putAll(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    dataMap.putAll(data);
  }

  @Override
  public void putAll(JPMap data) {
    if (data == null || data.isEmpty()) {
      return;
    }
    data.forEach(dataMap::put);
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
}