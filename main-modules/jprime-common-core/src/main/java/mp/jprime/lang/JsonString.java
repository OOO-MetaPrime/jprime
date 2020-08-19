package mp.jprime.lang;

/**
 * JSON
 */
public class JsonString implements Comparable<JsonString> {
  private final String s;

  /**
   * Конструктор
   *
   * @param s Строка
   */
  private JsonString(String s) {
    this.s = s;
  }

  /**
   * Создает объект
   *
   * @param s Строка
   * @return JsonString
   */
  public static JsonString from(String s) {
    return new JsonString(s);
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
  public int compareTo(JsonString o) {
    if (s == null) {
      return -1;
    }
    if (o.toString() == null) {
      return 1;
    }
    return s.compareTo(o.toString());
  }
}
