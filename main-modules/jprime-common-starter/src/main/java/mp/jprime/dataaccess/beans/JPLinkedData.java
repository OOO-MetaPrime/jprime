package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

import java.util.Collections;
import java.util.Map;

/**
 * Данные связных объектов
 */
public class JPLinkedData {
  private final Map<String, JPObject> data;

  /**
   * Конструктор
   *
   * @param data Данные объекта
   */
  private JPLinkedData(Map<String, JPObject> data) {
    this.data = Collections.unmodifiableMap(data == null ? Collections.emptyMap() : data);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public JPObject get(JPAttr attr) {
    return data.get(attr.getCode());
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public JPObject get(String attr) {
    return data.get(attr);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public Map<String, JPObject> toMap() {
    return data;
  }

  /**
   * Построитель JPLinkedData
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPLinkedData
   */
  public static final class Builder {
    private Map<String, JPObject> data;

    private Builder() {

    }

    /**
     * Создаем JPLinkedData
     *
     * @return JPLinkedData
     */
    public JPLinkedData build() {
      return new JPLinkedData(data);
    }


    /**
     * Данные объекта
     *
     * @param data Данные объекта
     * @return Builder
     */
    public Builder data(Map<String, JPObject> data) {
      this.data = data;
      return this;
    }
  }
}
