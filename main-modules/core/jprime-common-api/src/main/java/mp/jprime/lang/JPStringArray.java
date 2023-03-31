package mp.jprime.lang;

import java.util.List;

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
}
