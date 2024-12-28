package mp.jprime.dataaccess.params.query.data;

/**
 * С - ПО
 */
public final class Pair implements Comparable<Pair> {
  private final Comparable from;
  private final Comparable to;

  /**
   * Конструктор
   *
   * @param from С
   * @param to   По
   */
  private Pair(Comparable from, Comparable to) {
    this.from = from;
    this.to = to;
  }

  /**
   * Создает пары
   *
   * @param from С
   * @param to   По
   * @return Пара
   */
  public static Pair from(Comparable from, Comparable to) {
    return new Pair(from, to);
  }

  /**
   * С
   *
   * @return С
   */
  public Comparable getFrom() {
    return from;
  }

  /**
   * По
   *
   * @return По
   */
  public Comparable getTo() {
    return to;
  }

  @Override
  public int compareTo(Pair o) {
    int cmp = compare(from, o.from);
    return cmp == 0 ? compare(to, o.to) : cmp;
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
