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


  /**
   * Возвращает признак наличия данных
   *
   * @return Да/Нет
   */
  @Override
  public boolean containsAttr(String attr) {
    return data.containsKey(attr);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  @Override
  public <T> T get(String attr) {
    return (T) data.get(attr);
  }

  /**
   * Признак отсутствия данных
   *
   * @return Да/Нет
   */
  @Override
  public boolean isEmpty() {
    return data.isEmpty();
  }

  /**
   * Размер данных
   *
   * @return Размер данных
   */
  @Override
  public int size() {
    return data.size();
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  @Override
  public Map<String, Object> toMap() {
    return new HashMap<>(data);
  }

  /**
   * Возвращает множество ключей
   *
   * @return Множество ключей
   */
  @Override
  public Set<String> keySet() {
    return data.keySet();
  }

  /**
   * Возвращает коллекцию значений
   *
   * @return Коллекция значений
   */
  @Override
  public Collection<Object> values() {
    return data.values();
  }

  /**
   * Реализация итератора
   *
   * @param action BiConsumer
   */
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
    return new JPData(data);
  }

  /**
   * Построитель JPData
   *
   * @param data JPMap
   * @return JPData
   */
  public static JPData of(JPMap data) {
    return new JPData(data);
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
