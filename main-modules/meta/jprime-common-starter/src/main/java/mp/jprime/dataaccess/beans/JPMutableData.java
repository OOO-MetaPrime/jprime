package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public final class JPMutableData {
  private final Map<String, Object> dataMap;

  /**
   * Конструктор
   *
   * @param dataMap Данные объекта
   */
  private JPMutableData(Map<String, Object> dataMap) {
    this.dataMap = dataMap != null ? new HashMap<>(dataMap) : new HashMap<>();
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
   * @param data Даныне
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
   * Возвращает данные
   *
   * @return Данные
   */
  public Map<String, Object> toMap() {
    return new HashMap<>(dataMap);
  }


  /**
   * Построитель JPMutableData
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPMutableData
   */
  public static final class Builder {
    private Map<String, Object> data;

    private Builder() {

    }

    /**
     * Создаем JPData
     *
     * @return JPData
     */
    public JPMutableData build() {
      return new JPMutableData(data);
    }


    /**
     * Данные объекта
     *
     * @param data Данные объекта
     * @return Builder
     */
    public Builder data(Map<String, Object> data) {
      this.data = data;
      return this;
    }
  }
}