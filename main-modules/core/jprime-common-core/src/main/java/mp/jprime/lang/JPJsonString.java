package mp.jprime.lang;

/**
 * JSON
 */
public final class JPJsonString implements Comparable<JPJsonString> {
  private final String s;

  /**
   * Конструктор
   *
   * @param s Строка
   */
  private JPJsonString(String s) {
    this.s = s;
  }

  /**
   * Создает объект
   *
   * @param s Строка
   * @return JsonString
   */
  public static JPJsonString from(String s) {
    return new JPJsonString(s);
  }

  /**
   * Возвращает строку
   *
   * @return строку
   */
  public String toString() {
    return s;
  }

  @Override
  public int compareTo(JPJsonString o) {
    if (s == null) {
      return -1;
    }
    if (o.toString() == null) {
      return 1;
    }
    return s.compareTo(o.toString());
  }
}
