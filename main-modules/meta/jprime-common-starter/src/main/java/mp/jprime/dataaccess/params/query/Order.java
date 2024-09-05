package mp.jprime.dataaccess.params.query;


import mp.jprime.dataaccess.enums.OrderDirection;

import java.util.Objects;

/**
 * Порядок сортировки атрибута
 */
public class Order {
  private final String attr;
  private final OrderDirection order;

  /**
   * Конструктор
   *
   * @param attr  Кодовое имя атрибута
   * @param order Направление сортировки
   */
  public Order(String attr, OrderDirection order) {
    this.attr = attr;
    this.order = order;
  }

  /**
   * Создание настройки порядка сортировки
   *
   * @param attr  Кодовое имя атрибута
   * @param order Направление сортировки
   */
  public static Order of(String attr, OrderDirection order) {
    return new Order(attr, order);
  }

  /**
   * Создание настройки порядка сортировки (ASC)
   *
   * @param attr Кодовое имя атрибута
   */
  public static Order asc(String attr) {
    return new Order(attr, OrderDirection.ASC);
  }

  /**
   * Создание настройки порядка сортировки (DESC)
   *
   * @param attr Кодовое имя атрибута
   */
  public static Order desc(String attr) {
    return new Order(attr, OrderDirection.DESC);
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getAttr() {
    return attr;
  }

  /**
   * Направление сортировки
   *
   * @return Направление сортировки
   */
  public OrderDirection getOrder() {
    return order;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(attr, order.attr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attr);
  }
}
