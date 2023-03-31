package mp.jprime.lang;

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
  public static JPIntegerArray of(List<Integer> values) {
    return new JPIntegerArray(values);
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
}
