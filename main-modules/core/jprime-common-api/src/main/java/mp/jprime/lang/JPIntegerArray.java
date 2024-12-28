package mp.jprime.lang;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public final class JPIntegerArray extends JPArray<Integer> {
  private JPIntegerArray(List<Integer> values) {
    super(values);
  }

  private JPIntegerArray(Integer[] values) {
    super(values);
  }

  /**
   * Конвертация в массив
   *
   * @return Массив значений
   */
  @Override
  public Integer[] toArray() {
    List<Integer> values = getValues();
    return values.toArray(new Integer[0]);
  }

  /**
   * Создать JPIntegerArray
   *
   * @param values Список значений
   * @return JPIntegerArray
   */
  public static JPIntegerArray of(Collection<Integer> values) {
    return new JPIntegerArray(values != null ? values.stream().toList() : null);
  }

  /**
   * Создать JPIntegerArray
   *
   * @param values Список значений
   * @return JPIntegerArray
   */
  public static JPIntegerArray of(List<Integer> values) {
    return new JPIntegerArray(values);
  }

  /**
   * Создать JPIntegerArray
   *
   * @param value Значение
   * @return JPIntegerArray
   */
  public static JPIntegerArray of(Integer value) {
    if (value == null) {
      return null;
    }
    return new JPIntegerArray(List.of(value));
  }

  /**
   * Создать JPIntegerArray
   *
   * @param values Массив значений
   * @return JPIntegerArray
   */
  public static JPIntegerArray of(Integer[] values) {
    return new JPIntegerArray(values);
  }

  /**
   * Создать JPIntegerArray
   *
   * @param values Массив значений
   * @return JPIntegerArray
   */
  public static JPIntegerArray of(JPIntegerArray... values) {
    if (values == null) {
      return new JPIntegerArray(Collections.emptyList());
    }
    Collection<Integer> result = new LinkedHashSet<>();
    for (JPIntegerArray value : values) {
      if (value == null) {
        continue;
      }
      result.addAll(value.toList());
    }
    return JPIntegerArray.of(result);
  }
}
