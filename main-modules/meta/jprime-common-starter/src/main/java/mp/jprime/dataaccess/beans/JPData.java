package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Данные объекта
 */
public final class JPData {
  private final Map<String, Object> data;

  /**
   * Конструктор
   *
   * @param data Данные объекта
   */
  private JPData(Map<String, Object> data) {
    this.data = Collections.unmodifiableMap(data == null ? Collections.emptyMap() : data);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public Object get(JPAttr attr) {
    return data.get(attr.getCode());
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public Object get(String attr) {
    return data.get(attr);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public Map<String, Object> toMap() {
    return new HashMap<>(data);
  }

  /**
   * Реализация итератора
   *
   * @param action BiConsumer
   */
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    data.forEach(action);
  }


  /**
   * Построитель JPData
   *
   * @return JPData
   */
  public static JPData empty() {
    return new JPData(null);
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
