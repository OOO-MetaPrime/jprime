package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.query.Aggregate;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.enums.AggregationOperator;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос агрегации данных
 */
public class JPAggregate extends JPBaseParams {
  private final String jpClass;
  private final Collection<Aggregate> aggrs;
  private final Filter where;
  private final Integer timeout;

  /**
   * Конструктор
   *
   * @param jpClass Кодовое имя класса
   * @param aggrs   Агрегации по классу
   * @param where   Условие выборки
   * @param auth    Данные аутентификации
   * @param timeout Время ожидания запроса
   * @param source  Источник данных
   */
  private JPAggregate(String jpClass, Collection<Aggregate> aggrs, Filter where, AuthInfo auth,
                      Integer timeout, Source source) {
    super(source, auth);

    this.jpClass = jpClass;
    this.aggrs = aggrs == null ? Collections.emptyList() : Collections.unmodifiableCollection(aggrs);
    this.where = where;

    this.timeout = timeout;
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
   * Условия
   *
   * @return Условия
   */
  public Filter getWhere() {
    return where;
  }

  /**
   * Агрегации по классу
   *
   * @return Агрегации по классу
   */
  public Collection<Aggregate> getAggrs() {
    return aggrs;
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
   * Построитель JPAggregate
   *
   * @param jpClass Мета класс
   * @return Builder
   */
  public static Builder from(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель JPAggregate
   *
   * @param jpClass Кодовое имя класса
   * @return Builder
   */
  public static Builder from(String jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель JPAggregate
   */
  public static final class Builder {
    private String jpClass;
    private Collection<Aggregate> aggrs = new ArrayList<>();
    private Filter where;
    private AuthInfo auth;
    private Integer timeout;
    private Source source;

    private Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPAggregate
     *
     * @return JPAggregate
     */
    public JPAggregate build() {
      return new JPAggregate(jpClass, aggrs, where, auth, timeout, source);
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
     * Агрегация атрибута
     *
     * @param alias    Алиас выборки
     * @param attr     Кодовое имя атрибута
     * @param operator Оператор
     * @return Builder
     */
    public Builder aggr(String alias, String attr, AggregationOperator operator) {
      if (alias != null && attr != null && operator != null) {
        this.aggrs.add(new Aggregate(alias, attr, operator));
      }
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
