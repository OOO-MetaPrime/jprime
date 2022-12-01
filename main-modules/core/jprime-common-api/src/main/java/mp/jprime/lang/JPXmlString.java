package mp.jprime.lang;

/**
 * XML
 */
public final class JPXmlString implements Comparable<JPXmlString> {
  private final String s;

  /**
   * Конструктор
   *
   * @param s Строка
   */
  private JPXmlString(String s) {
    this.s = s;
  }

  /**
   * Создает объект
   *
   * @param s Строка
   * @return JsonString
   */
  public static JPXmlString from(String s) {
    return new JPXmlString(s);
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
  public int compareTo(JPXmlString o) {
    if (s == null) {
      return -1;
    }
    if (o.toString() == null) {
      return 1;
    }
    return s.compareTo(o.toString());
  }
}