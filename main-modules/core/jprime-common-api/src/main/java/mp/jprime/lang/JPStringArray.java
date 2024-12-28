package mp.jprime.lang;

import java.util.*;

public final class JPStringArray extends JPArray<String> {
  private JPStringArray(List<String> values) {
    super(values);
  }

  private JPStringArray(String[] values) {
    super(values);
  }

  /**
   * Конвертация в массив
   *
   * @return Массив значений
   */
  @Override
  public String[] toArray() {
    List<String> values = getValues();
    return values.toArray(new String[0]);
  }

  /**
   * Создать JPStringArray
   *
   * @param values Список значений
   * @return JPStringArray
   */
  public static JPStringArray of(Collection<String> values) {
    return new JPStringArray(values != null ? values.stream().toList() : null);
  }

  /**
   * Создать JPStringArray
   *
   * @param value Значение
   * @return JPStringArray
   */
  public static JPStringArray of(String value) {
    if (value == null) {
      return null;
    }
    return new JPStringArray(List.of(value));
  }

  /**
   * Создать JPStringArray
   *
   * @param values Список значений
   * @return JPStringArray
   */
  public static JPStringArray of(List<String> values) {
    return new JPStringArray(values);
  }

  /**
   * Создать JPStringArray
   *
   * @param values Массив значений
   * @return JPStringArray
   */
  public static JPStringArray of(String[] values) {
    return new JPStringArray(values);
  }

  /**
   * Создать JPStringArray
   *
   * @param values Массив значений
   * @return JPStringArray
   */
  public static JPStringArray of(JPStringArray... values) {
    if (values == null) {
      return new JPStringArray(Collections.emptyList());
    }
    Collection<String> result = new LinkedHashSet<>();
    for (JPStringArray value : values) {
      if (value == null) {
        continue;
      }
      result.addAll(value.toList());
    }
    return JPStringArray.of(result);
  }
}
