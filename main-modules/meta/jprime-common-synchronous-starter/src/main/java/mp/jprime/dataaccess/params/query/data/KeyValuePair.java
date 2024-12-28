package mp.jprime.dataaccess.params.query.data;

/**
 * Запись ключ-значение
 */
public final class KeyValuePair implements Comparable<KeyValuePair> {
  /**
   * Ключ
   */
  private final String key;
  /**
   * Значение
   */
  private final Comparable value;

  private KeyValuePair(String key, Comparable value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Получить ключ
   *
   * @return Ключ
   */
  public String getKey() {
    return key;
  }

  /**
   * Получить значение
   *
   * @return Значение
   */
  public Comparable getValue() {
    return value;
  }

  /**
   * Создать новую запись
   *
   * @param key   Ключ
   * @param value Значение
   * @return Новая запись
   */
  public static KeyValuePair from(String key, Comparable value) {
    return new KeyValuePair(key, value);
  }

  @Override
  public int compareTo(KeyValuePair o) {
    int cmp = compare(key, o.key);
    return cmp == 0 ? compare(value, o.value) : cmp;
  }

  private int compare(Comparable o1, Comparable o2) {
    if (o1 == null) {
      if (o2 == null) {
        return 0;
      } else {
        return -1;
      }
    } else if (o2 == null) {
      return +1;
    } else {
      return o1.compareTo(o2);
    }
  }
}
