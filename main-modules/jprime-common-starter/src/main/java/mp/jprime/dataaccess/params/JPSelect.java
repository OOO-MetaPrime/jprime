package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.Order;
import mp.jprime.dataaccess.params.query.enums.OrderDirection;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос получения данных
 */
public class JPSelect extends JPBaseCRUD {
  private final String jpClass;
  private final Integer offset;
  private final Integer limit;
  private final boolean totalCount;
  private final boolean useDefaultJpAttrs;
  private final Map<String, Collection<String>> select;
  private final Collection<Order> orderBy;
  private final Filter where;
  private final Integer timeout;

  /**
   * Конструктор
   *
   * @param jpClass           Кодовое имя класса
   * @param offset            Смещение начала выборки
   * @param limit             Объектов в выборке
   * @param totalCount        Определение точного кол-во объектов в выборке
   * @param useDefaultJpAttrs Признак использования дефолтных атрибутов ссылочных объектов
   * @param select            Атрибуты в выборке
   * @param where             Условие выборки
   * @param orderBy           Направление сортировки
   * @param auth              Данные аутентификации
   * @param timeout           Время ожидания запроса
   * @param source            Источник данных
   */
  private JPSelect(String jpClass, Integer offset, Integer limit, boolean totalCount, boolean useDefaultJpAttrs,
                   Map<String, Collection<String>> select, Filter where, List<Order> orderBy, AuthInfo auth,
                   Integer timeout, Source source) {
    super(source, auth);
    this.offset = offset;
    this.limit = limit;
    this.totalCount = totalCount;
    this.useDefaultJpAttrs = useDefaultJpAttrs;

    this.jpClass = jpClass;
    this.select = Collections.unmodifiableMap(select == null ? Collections.emptyMap() : select);
    this.where = where;
    this.orderBy = Collections.unmodifiableList(orderBy == null ? Collections.emptyList() : orderBy);

    this.timeout = timeout;
  }

  /**
   * Атрибуты в выборке
   *
   * @return Атрибуты в выборке
   */
  public Collection<String> getAttrs() {
    return select.keySet();
  }

  /**
   * Ссылочные атрибуты в выборке
   *
   * @return attrCode Кодовое имя атрибута
   */
  public Collection<String> getLinkAttrs(String attrCode) {
    return select.get(attrCode);
  }

  /**
   * Условия
   *
   * @return Условия
   */
  public Filter getWhere() {
    return where;
  }

  /**
   * Направление сортировки
   *
   * @return Направление сортировки
   */
  public Collection<Order> getOrderBy() {
    return orderBy;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Объектов в выборке
   *
   * @return Объектов в выборке
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Смещение начала выборки
   *
   * @return Смещение начала выборки
   */
  public Integer getOffset() {
    return offset;
  }

  /**
   * Определение точного кол-во объектов в выборке
   *
   * @return Да/Нет
   */
  public boolean isTotalCount() {
    return totalCount;
  }

  /**
   * Признак использования дефолтных атрибутов ссылочных объектов
   *
   * @return Да/Нет
   */
  public boolean isUseDefaultJpAttrs() {
    return useDefaultJpAttrs;
  }

  /**
   * Время ожидания запроса
   *
   * @return Время ожидания запроса
   */
  public Integer getTimeout() {
    return timeout;
  }

  /**
   * Построитель JPSelect
   *
   * @param jpClass Мета класс
   * @return Builder
   */
  public static Builder from(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель JPSelect
   *
   * @param jpClass Кодовое имя класса
   * @return Builder
   */
  public static Builder from(String jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель JPSelect
   */
  public static final class Builder {
    private String jpClass;
    private Integer offset;
    private Integer limit;
    private boolean totalCount;
    private boolean useDefaultJpAttrs;
    private Map<String, Collection<String>> select = new HashMap<>();
    private List<Order> orderBy = new ArrayList<>();
    private Filter where;
    private AuthInfo auth;
    private Integer timeout;
    private Source source;

    private Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPSelect
     *
     * @return JPSelect
     */
    public JPSelect build() {
      return new JPSelect(jpClass, offset, limit, totalCount, useDefaultJpAttrs, select, where, orderBy, auth,
          timeout, source);
    }

    /**
     * Метакласс
     *
     * @param jpClass Метакласс
     * @return Builder
     */
    public Builder jpClass(String jpClass) {
      this.jpClass = jpClass;
      return this;
    }

    /**
     * Смещение начала выборки
     *
     * @param offset Смещение начала выборки
     * @return Builder
     */
    public Builder offset(Integer offset) {
      this.offset = offset;
      return this;
    }

    /**
     * Объектов в выборке
     *
     * @param limit Объектов в выборке
     * @return Builder
     */
    public Builder limit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Определение точного кол-ва объектов в выборке
     *
     * @param totalCount Определение точного кол-ва объектов
     * @return Builder
     */
    public Builder totalCount(boolean totalCount) {
      this.totalCount = totalCount;
      return this;
    }

    /**
     * Определение точного кол-ва объектов
     *
     * @return Определение точного кол-ва объектов
     */
    public boolean isTotalCount() {
      return totalCount;
    }

    /**
     * Признак использования дефолтных атрибутов ссылочных объектов
     *
     * @param useDefaultJpAttrs Признак использования дефолтных атрибутов ссылочных объектов
     * @return Builder
     */
    public Builder useDefaultJpAttrs(boolean useDefaultJpAttrs) {
      this.useDefaultJpAttrs = useDefaultJpAttrs;
      return this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attr атрибут
     * @return Builder
     */
    public Builder attr(JPAttr attr) {
      return attr != null ? attr(attr.getCode()) : this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attr          атрибут
     * @param linkAttrCodes кодовые имена ссылочного класса
     * @return Builder
     */
    public Builder attr(JPAttr attr, String... linkAttrCodes) {
      return attr != null ? attr(attr.getCode(), linkAttrCodes) : this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attr          атрибут
     * @param linkAttrCodes кодовые имена ссылочного класса
     * @return Builder
     */
    public Builder attr(JPAttr attr, Collection<String> linkAttrCodes) {
      return attr != null ? attr(attr.getCode(), linkAttrCodes) : this;
    }


    /**
     * Атрибут в выборке
     *
     * @param attrCode кодовое имя атрибута
     * @return Builder
     */
    public Builder attr(String attrCode) {
      this.select.put(attrCode, null);
      return this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attrCodes кодовые имена атрибутов
     * @return Builder
     */
    public Builder attrs(String... attrCodes) {
      Collection<String> attrs = attrCodes != null ? Arrays.asList(attrCodes) : null;
      this.attrs(attrs);
      return this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attrCode      кодовое имя атрибута
     * @param linkAttrCodes кодовые имена ссылочного класса
     * @return Builder
     */
    public Builder attr(String attrCode, String... linkAttrCodes) {
      Collection<String> linkAttrs = linkAttrCodes != null ? Arrays.asList(linkAttrCodes) : null;
      this.attr(attrCode, linkAttrs);
      return this;
    }

    /**
     * Атрибут в выборке
     *
     * @param attrCode      кодовое имя атрибута
     * @param linkAttrCodes кодовые имена ссылочного класса
     * @return Builder
     */
    public Builder attr(String attrCode, Collection<String> linkAttrCodes) {
      Collection<String> linkAttrs = linkAttrCodes != null ? Collections.unmodifiableCollection(linkAttrCodes) : null;
      this.select.put(attrCode, linkAttrs);
      return this;
    }

    /**
     * Атрибуты в выборке
     *
     * @param attrsCodeList список кодовых имен атрибутов
     * @return Builder
     */
    public Builder attrs(Collection<String> attrsCodeList) {
      if (attrsCodeList != null) {
        attrsCodeList.forEach(this::attr);
      }
      return this;
    }

    /**
     * Признак неуказанной сортировки
     *
     * @return Да/Нет
     */
    public boolean isOrderByEmpty() {
      return orderBy.isEmpty();
    }

    /**
     * Сортировка атрибута
     *
     * @param attr  Атрибут
     * @param order Направление сортировки
     * @return Builder
     */
    public Builder orderBy(JPAttr attr, OrderDirection order) {
      return attr != null ? orderBy(attr.getCode(), order) : this;
    }

    /**
     * Сортировка атрибута  по возрастанию
     *
     * @param attr Атрибут
     * @return Builder
     */
    public Builder orderByAsc(JPAttr attr) {
      return attr != null ? orderByAsc(attr.getCode()) : this;
    }

    /**
     * Сортировка атрибута  по убыванию
     *
     * @param attr Атрибут
     * @return Builder
     */
    public Builder orderByDesc(JPAttr attr) {
      return attr != null ? orderByDesc(attr.getCode()) : this;
    }

    /**
     * Сортировка атрибута
     *
     * @param attrCode кодовое имя атрибута
     * @param order    Направление сортировки
     * @return Builder
     */
    public Builder orderBy(String attrCode, OrderDirection order) {
      this.orderBy.add(new Order(attrCode, order));
      return this;
    }

    /**
     * Сортировка атрибута по возрастанию
     *
     * @param attrCode кодовое имя атрибута
     * @return Builder
     */
    public Builder orderByAsc(String attrCode) {
      this.orderBy.add(new Order(attrCode, OrderDirection.ASC));
      return this;
    }

    /**
     * Сортировка атрибута по убыванию
     *
     * @param attrCode кодовое имя атрибута
     * @return Builder
     */
    public Builder orderByDesc(String attrCode) {
      this.orderBy.add(new Order(attrCode, OrderDirection.DESC));
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder where(Filter where) {
      this.where = where;
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder andWhere(Filter where) {
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.and(this.where, where);
      }
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder orWhere(Filter where) {
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.or(this.where, where);
      }
      return this;
    }

    /**
     * Аутентификация
     *
     * @param auth Аутентификация
     * @return Builder
     */
    public Builder auth(AuthInfo auth) {
      this.auth = auth;
      return this;
    }

    /**
     * Время ожидания запроса
     *
     * @param timeout Время ожидания запроса
     * @return Builder
     */
    public Builder timeout(Integer timeout) {
      this.timeout = timeout;
      return this;
    }

    /**
     * Источник данных
     *
     * @param source Источник данных
     * @return Builder
     */
    public Builder source(Source source) {
      this.source = source;
      return this;
    }
  }
}
