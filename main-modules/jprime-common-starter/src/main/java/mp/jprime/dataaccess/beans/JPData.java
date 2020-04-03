package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

import java.util.Collections;
import java.util.Map;

/**
 * Данные объекта
 */
public class JPData {
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
    return data;
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
