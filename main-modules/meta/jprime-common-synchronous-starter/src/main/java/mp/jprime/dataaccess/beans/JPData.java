package mp.jprime.dataaccess.beans;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.lang.JPMap;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Неизменяемые данные
 */
public final class JPData implements JPAttrData {
  private final static JPData EMPTY = new JPData();

  private final Map<String, Object> data;

  /**
   * Конструктор
   */
  private JPData() {
    this.data = Collections.emptyMap();
  }

  /**
   * Конструктор
   *
   * @param data Данные
   */
  private JPData(Map<String, Object> data) {
    this.data = data == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(data));
  }

  /**
   * Конструктор
   *
   * @param data Данные
   */
  private JPData(JPMap data) {
    this.data = data == null ? Collections.emptyMap() : Collections.unmodifiableMap(data.toMap());
  }

  @Override
  public boolean containsAttr(String attr) {
    return data.containsKey(attr);
  }

  @Override
  public <T> T get(String attr) {
    return (T) data.get(attr);
  }

  @Override
  public boolean isEmpty() {
    return data.isEmpty();
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public Map<String, Object> toMap() {
    return new HashMap<>(data);
  }

  @Override
  public Set<String> keySet() {
    return data.keySet();
  }

  @Override
  public Collection<Object> values() {
    return data.values();
  }

  @Override
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    data.forEach(action);
  }

  /**
   * Построитель JPData
   *
   * @return JPData
   */
  public static JPData empty() {
    return EMPTY;
  }

  /**
   * Построитель JPData
   *
   * @param data Данные
   * @return JPData
   */
  public static JPData of(Map<String, Object> data) {
    return data != null && !data.isEmpty() ? new JPData(data) : empty();
  }

  /**
   * Построитель JPData
   *
   * @param data JPMap
   * @return JPData
   */
  public static JPData of(JPMap data) {
    return data != null && !data.isEmpty() ? new JPData(data) : empty();
  }

  /**
   * Построитель JPData
   *
   * @param key   Ключ
   * @param value Значение
   * @return JPData
   */
  public static JPData of(String key, Object value) {
    Map<String, Object> data = new HashMap<>();
    data.put(key, value);
    return JPData.of(data);
  }

  /**
   * Построитель JPData
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPData
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
    public JPData build() {
      return new JPData(data);
    }


    /**
     * Данные
     *
     * @param data Данные
     * @return Builder
     */
    public Builder data(Map<String, Object> data) {
      this.data = data;
      return this;
    }
  }
}
