package mp.jprime.common;

/**
 * Порядок сортировки атрибута
 */
public interface JPOrder {
  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String getAttr();

  /**
   * Направление сортировки
   *
   * @return Направление сортировки
   */
  JPOrderDirection getOrder();

  record JPOrderRecord(String getAttr, JPOrderDirection getOrder) implements JPOrder {

  }

  /**
   * Создание настройки порядка сортировки
   *
   * @param attr  Кодовое имя атрибута
   * @param order Направление сортировки
   */
  static JPOrder of(String attr, JPOrderDirection order) {
    return new JPOrderRecord(attr, order);
  }

  /**
   * Создание настройки порядка сортировки (ASC)
   *
   * @param attr Кодовое имя атрибута
   */
  static JPOrder asc(String attr) {
    return new JPOrderRecord(attr, JPOrderDirection.ASC);
  }

  /**
   * Создание настройки порядка сортировки (DESC)
   *
   * @param attr Кодовое имя атрибута
   */
  static JPOrder desc(String attr) {
    return new JPOrderRecord(attr, JPOrderDirection.DESC);
  }
}
