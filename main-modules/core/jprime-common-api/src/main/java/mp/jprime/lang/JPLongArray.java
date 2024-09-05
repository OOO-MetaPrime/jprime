package mp.jprime.lang;

import java.util.Collection;
import java.util.List;

public final class JPLongArray extends JPArray<Long> {
  private JPLongArray(List<Long> values) {
    super(values);
  }

  private JPLongArray(Long[] values) {
    super(values);
  }

  /**
   * Конвертация в массив
   *
   * @return Массив значений
   */
  @Override
  public Long[] toArray() {
    List<Long> values = getValues();
    return values.toArray(new Long[0]);
  }

  /**
   * Создать JPLongArray
   *
   * @param values Список значений
   * @return JPLongArray
   */
  public static JPLongArray of(Collection<Long> values) {
    return new JPLongArray(values != null ? values.stream().toList() : null);
  }

  /**
   * Создать JPLongArray
   *
   * @param values Список значений
   * @return JPLongArray
   */
  public static JPLongArray of(List<Long> values) {
    return new JPLongArray(values);
  }

  /**
   * Создать JPStringArray
   *
   * @param value Значение
   * @return JPStringArray
   */
  public static JPLongArray of(Long value) {
    if (value == null) {
      return null;
    }
    return new JPLongArray(List.of(value));
  }

  /**
   * Создать JPLongArray
   *
   * @param values Массив значений
   * @return JPLongArray
   */
  public static JPLongArray of(Long[] values) {
    return new JPLongArray(values);
  }
}
