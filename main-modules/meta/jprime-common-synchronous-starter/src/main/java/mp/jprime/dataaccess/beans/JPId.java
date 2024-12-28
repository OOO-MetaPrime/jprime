package mp.jprime.dataaccess.beans;

import java.util.Objects;

/**
 * Идентификатор объекта
 */
public class JPId {
  public static final String DELIMETER = "@";
  private final String jpClass; // Кодовое имя метаописания класса
  private final Object id;

  /**
   * Конструктор
   *
   * @param jpClass Кодовое имя метаописания класса
   * @param id      Идентификатор объекта
   */
  public JPId(String jpClass, Object id) {
    this.jpClass = jpClass;
    this.id = id;
  }

  /**
   * Конструктор
   *
   * @param jpClass Кодовое имя метаописания класса
   * @param id      Идентификатор объекта
   */
  public static JPId get(String jpClass, Object id) {
    return new JPId(jpClass, id);
  }

  /**
   * Кодовое имя метаописания класса
   *
   * @return Кодовое имя метаописания класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  public Comparable getId() {
    return (Comparable) id;
  }

  @Override
  public String toString() {
    return jpClass + DELIMETER + id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPId jpId = (JPId) o;
    return Objects.equals(jpClass, jpId.jpClass) &&
        Objects.equals(id, jpId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jpClass, id);
  }
}
