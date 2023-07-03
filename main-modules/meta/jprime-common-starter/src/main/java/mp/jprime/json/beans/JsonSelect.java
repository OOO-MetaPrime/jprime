package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

/**
 * Модель запроса
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSelect {
  /**
   * Ограничение на количество получаемых объектов
   */
  private Integer limit;
  /**
   * Смещение выборки
   */
  private Integer offset;
  /**
   * Признак подсчет общего количества объектов
   */
  private boolean totalCount;
  /**
   * Признак расчета доступа для каждого объекта
   */
  private boolean access;
  /**
   * Список запрашиваемых атрибутов
   */
  private Collection<String> attrs;
  /**
   * Список запрашиваемых атрибутов для ссылочных классов
   */
  private Map<String, String> linkAttrs = new HashMap<>();
  /**
   * Условие ограничения выборки
   */
  private JsonExpr filter;
  /**
   * Настройка сортировки выборки
   */
  private List<JsonOrder> orders = new ArrayList<>();


  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getOffset() {
    return offset;
  }

  public boolean isTotalCount() {
    return totalCount;
  }

  public void setTotalCount(boolean totalCount) {
    this.totalCount = totalCount;
  }

  public boolean isAccess() {
    return access;
  }

  public void setAccess(boolean access) {
    this.access = access;
  }

  public Collection<String> getAttrs() {
    return attrs;
  }

  public void setAttrs(Collection<String> attrs) {
    this.attrs = attrs;
  }

  public Map<String, String> getLinkAttrs() {
    return linkAttrs;
  }

  public void setLinkAttrs(Map<String, String> linkAttrs) {
    this.linkAttrs = linkAttrs;
  }

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }

  public JsonExpr getFilter() {
    return filter;
  }

  public void setOrders(List<JsonOrder> orders) {
    this.orders = orders;
  }

  public List<JsonOrder> getOrders() {
    return orders;
  }
}