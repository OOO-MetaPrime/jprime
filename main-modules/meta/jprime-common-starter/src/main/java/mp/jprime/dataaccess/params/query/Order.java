package mp.jprime.dataaccess.params.query;


import mp.jprime.dataaccess.enums.OrderDirection;

import java.util.Objects;

/**
 * Подрядок сортировки атрибута
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
