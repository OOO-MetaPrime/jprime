package mp.jprime.lang;

/**
 * XML
 */
public class XmlString implements Comparable<XmlString> {
  private final String s;

  /**
   * Конструктор
   *
   * @param s Строка
   */
  private XmlString(String s) {
    this.s = s;
  }

  /**
   * Создает объект
   *
   * @param s Строка
   * @return JsonString
   */
  public static XmlString from(String s) {
    return new XmlString(s);
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
  public int compareTo(XmlString o) {
    if (s == null) {
      return -1;
    }
    if (o.toString() == null) {
      return 1;
    }
    return s.compareTo(o.toString());
  }
}