package mp.jprime.json.services;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.enums.*;
import mp.jprime.dataaccess.params.*;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.dataaccess.params.query.filters.range.*;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.*;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPRange;
import mp.jprime.lang.JPStringRange;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис трансформации запросов и условий
 */
@Service
public class QueryService implements ParserServiceAware {
  private JPJsonMapper jpJsonMapper;
  private ParserService parserService;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  /**
   * Максимальное количество в выборке по умолчанию
   */
  public static final int MAX_LIMIT = 50;

  private final Function<String, String> CLASS_MAP_FUNCTION = (classCode) -> classCode;
  private final BiFunction<String, String, String> REFCLASS_FUNCTION = (classCode, refAttrCode) -> null;
  private final BiFunction<String, String, String> ATTR_MAP_FUNCTION = (classCode, attrCode) -> attrCode;

  /**
   * Создает описание выборки
   *
   * @param json Строка запроса
   * @return Описание выборки
   */
  public JsonSelect getQuery(String json) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    return jpJsonMapper.toObject(JsonSelect.class, json);
  }

  /**
   * Создает описание выборки агрегации
   *
   * @param json Строка запроса
   * @return Описание выборки агрегации
   */
  public JsonAggrQuery getAggrQuery(String json) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    return jpJsonMapper.toObject(JsonAggrQuery.class, json);
  }

  /**
   * Создает описание выборки
   *
   * @param query Query
   * @return Описание выборки
   */
  public String toString(JsonSelect query) {
    return jpJsonMapper.toString(query);
  }

  /**
   * Создает описание выборки агрегации
   *
   * @param query Query
   * @return Описание выборки агрегации
   */
  public String toString(JsonAggrQuery query) {
    return jpJsonMapper.toString(query);
  }

  /**
   * Создает описание выборки
   *
   * @param select JPSelect
   * @return Описание выборки
   */
  public String toString(JPSelect select) {
    return toString(getQuery(select));
  }

  /**
   * Создает описание выборки
   *
   * @param json Строка запроса
   * @return Описание выборки
   */
  public JPSelect.Builder getSelect(String json) {
    return getSelect(null, json, null);
  }

  /**
   * Создает описание выборки
   *
   * @param jpClassCode Метаописание класса
   * @param json        Строка запроса
   * @param auth        Данные аутентификации
   * @return Описание выборки
   */
  public JPSelect.Builder getSelect(String jpClassCode, String json, AuthInfo auth) {
    try {
      return getSelect(jpClassCode, getQuery(json), auth);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание выборки
   *
   * @param jpClassCode Метаописание класса
   * @param query       Описание запроса
   * @param auth        Данные аутентификации
   * @return Описание выборки
   */
  public JPSelect.Builder getSelect(String jpClassCode, JsonSelect query, AuthInfo auth) {
    try {
      JPSelect.Builder builder = JPSelect
          .from(jpClassCode)
          .offset(0)
          .auth(auth)
          .limit(MAX_LIMIT);
      if (query != null) {
        builder
            .offset(query.getOffset() != null ? query.getOffset() : 0)
            .totalCount(query.isTotalCount())
            .limit(query.getLimit() != null ? query.getLimit() : MAX_LIMIT);
        Optional.ofNullable(query.getAttrs())
            .ifPresent(x -> x.forEach(builder::attr));
        Optional.ofNullable(query.getLinkAttrs())
            .ifPresent(x -> x.forEach((k, v) -> builder.attr(k, v != null && !v.isEmpty() ? v.split(",") : null)));
        Optional.ofNullable(query.getFilter())
            .ifPresent(x -> builder.where(toFilter(x)));
        Optional.ofNullable(query.getOrders())
            .ifPresent(x -> x.forEach(y -> appendOrderBy(builder, y)));
      }
      builder.useDefaultJpAttrs(Boolean.TRUE);
      return builder;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание агрегации
   *
   * @param jpClassCode Метаописание класса
   * @param json        Строка запроса
   * @param auth        Данные аутентификации
   * @return Описание агрегации
   */
  public JPAggregate.Builder getAggregate(String jpClassCode, String json, AuthInfo auth) {
    try {
      return getAggregate(jpClassCode, getAggrQuery(json), auth);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание агрегации
   *
   * @param jpClassCode Метаописание класса
   * @param query       Описание запроса
   * @param auth        Данные аутентификации
   * @return Описание агрегации
   */
  public JPAggregate.Builder getAggregate(String jpClassCode, JsonAggrQuery query, AuthInfo auth) {
    try {
      JPAggregate.Builder builder = JPAggregate
          .from(jpClassCode)
          .auth(auth);
      if (query != null) {
        Optional.ofNullable(query.getAggrs())
            .ifPresent(x -> x.forEach(y -> appendAggr(builder, y)));
        Optional.ofNullable(query.getFilter())
            .ifPresent(x -> builder.where(toFilter(x)));
      }
      return builder;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Учет агрегации
   *
   * @param builder Построитель JPAggregate
   * @param aggr    агрегация
   */
  private void appendAggr(JPAggregate.Builder builder, JsonAggregate aggr) {
    String alias = aggr.getAlias();
    String attr = aggr.getAttr();
    String oper = aggr.getOperator();
    AggregationOperator operator = oper != null ? AggregationOperator.getOperator(oper) : null;
    if (alias != null && attr != null && operator != null) {
      builder.aggr(alias, attr, operator);
    }
  }

  /**
   * Учет агрегации
   *
   * @param aggr         агрегация
   * @param classCode    Код класса
   * @param attrCodeFunc Логика маппинга атрибута
   * @return агрегация
   */
  private JsonAggregate toAggr(mp.jprime.dataaccess.params.query.Aggregate aggr,
                               String classCode,
                               BiFunction<String, String, String> attrCodeFunc) {
    String attr = aggr.getAttr();
    attr = attr != null ? attrCodeFunc.apply(classCode, attr) : null;
    String alias = aggr.getAlias();
    AggregationOperator oper = aggr.getOperator();
    if (alias != null && attr != null && oper != null) {
      JsonAggregate json = new JsonAggregate();
      json.setAlias(alias);
      json.setAttr(attr);
      json.setOperator(oper.getCode());
    }
    return null;
  }


  /**
   * Создает описание выборки агрегации
   *
   * @param aggregate Описание запроса
   * @return Описание выборки
   */
  public JsonAggrQuery getAggrQuery(JPAggregate aggregate) {
    return getAggrQuery(aggregate, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Создает описание выборки агрегации
   *
   * @param aggregate        Описание запроса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание выборки
   */
  public JsonAggrQuery getAggrQuery(JPAggregate aggregate,
                                    BiFunction<String, String, String> refClassCodeFunc,
                                    BiFunction<String, String, String> attrCodeFunc) {
    String classCode = aggregate.getJpClass();

    JsonAggrQuery query = new JsonAggrQuery();
    Optional.ofNullable(aggregate.getWhere())
        .ifPresent(x -> query.setFilter(toExp(x, classCode, refClassCodeFunc, attrCodeFunc)));
    query.setAggrs(
        aggregate.getAggrs()
            .stream()
            .map(aggr -> toAggr(aggr, classCode, attrCodeFunc))
            .collect(Collectors.toList())
    );
    return query;
  }

  /**
   * Создает описание выборки
   *
   * @param select Описание запроса
   * @return Описание выборки
   */
  public JsonSelect getQuery(JPSelect select) {
    return getQuery(select, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Создает описание выборки
   *
   * @param select           Описание запроса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание выборки
   */
  public JsonSelect getQuery(JPSelect select,
                             BiFunction<String, String, String> refClassCodeFunc,
                             BiFunction<String, String, String> attrCodeFunc) {
    String classCode = select.getJpClass();

    JsonSelect query = new JsonSelect();
    query.setLimit(select.getLimit());
    query.setOffset(select.getOffset());
    query.setTotalCount(select.isTotalCount());
    query.setAttrs(
        select.getAttrs()
            .stream()
            .map(attrCode -> attrCodeFunc.apply(classCode, attrCode))
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
    );
    query.setLinkAttrs(
        select.getAttrs()
            .stream()
            .collect(HashMap::new,
                (m, linkAttr) -> {
                  Collection<String> attrs = select.getLinkAttrs(linkAttr);
                  if (attrs != null && !attrs.isEmpty()) {
                    attrs = attrs.stream()
                        .map(attrCode -> attrCodeFunc.apply(refClassCodeFunc.apply(classCode, linkAttr), attrCode))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                    m.put(attrCodeFunc.apply(classCode, linkAttr), StringUtils.join(attrs, ","));
                  }
                },
                HashMap::putAll)
    );
    Optional.ofNullable(select.getWhere())
        .ifPresent(x -> query.setFilter(toExp(x, classCode, refClassCodeFunc, attrCodeFunc)));
    query.setOrders(
        select.getOrderBy()
            .stream()
            .map(order -> this.toOrder(order, classCode, attrCodeFunc))
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
    );
    return query;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private String stringValue(Object v) {
    return parserService.parseTo(String.class, v);
  }

  /**
   * JsonRange to JPStringRange
   *
   * @param v JsonRange
   * @return JPStringRange
   */
  private JPStringRange toStringRange(JsonStringRange v) {
    return v != null ? JPStringRange.closed(v.getLower(), v.getUpper()) : null;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private JsonStringRange toJsonRange(JPRange<?> v) {
    JsonStringRange result = new JsonStringRange();
    result.setLower(stringValue(v.lower()));
    result.setUpper(stringValue(v.upper()));
    return result;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private Collection<String> toStrings(Collection<? extends Comparable> v) {
    return v.stream().map(this::stringValue).collect(Collectors.toList());
  }


  /**
   * Учет условия
   *
   * @param filter Условие
   * @return Expr Условие
   */
  public JsonExpr toExp(Filter filter) {
    return toExp(filter, null, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Учет условия
   *
   * @param filter           Условие
   * @param classCode        Кодовое имя класса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Expr Условие
   */
  public JsonExpr toExp(Filter filter,
                        String classCode,
                        BiFunction<String, String, String> refClassCodeFunc,
                        BiFunction<String, String, String> attrCodeFunc) {
    if (filter == null) {
      return null;
    }
    if (filter instanceof BooleanFilter) {
      BooleanFilter c = (BooleanFilter) filter;
      if (c.getCond() == BooleanCondition.AND) {
        return new JsonExpr().and(
            c.getFilters().stream()
                .map(f -> toExp(f, classCode, refClassCodeFunc, attrCodeFunc))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
      } else if (c.getCond() == BooleanCondition.OR) {
        return new JsonExpr().or(
            c.getFilters().stream()
                .map(f -> toExp(f, classCode, refClassCodeFunc, attrCodeFunc))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
      }
    }

    JsonCond cond = null;
    if (filter instanceof PeriodFeatureFilter) {
      PeriodFeatureFilter v = (PeriodFeatureFilter) filter;
      cond = JsonCond.newFeatureCond(v.getFeatureCode()).checkFromDay(v.getCheckFromDay()).checkToDay(v.getCheckToDay());
    } else if (filter instanceof DayFeatureFilter) {
      DayFeatureFilter v = (DayFeatureFilter) filter;
      cond = JsonCond.newFeatureCond(v.getFeatureCode()).checkDay(v.getCheckDay());
    } else if (filter instanceof FeatureFilter) {
      FeatureFilter v = (FeatureFilter) filter;
      cond = JsonCond.newFeatureCond(v.getFeatureCode());
    } else if (filter instanceof YearFilter) {
      YearFilter v = (YearFilter) filter;
      String attrName = attrCodeFunc.apply(classCode, v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ_YEAR) {
        cond = JsonCond.newAttrCond(attrName).eqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_YEAR) {
        cond = JsonCond.newAttrCond(attrName).gtYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_YEAR) {
        cond = JsonCond.newAttrCond(attrName).gteYear(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_YEAR) {
        cond = JsonCond.newAttrCond(attrName).neqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_YEAR) {
        cond = JsonCond.newAttrCond(attrName).ltYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_YEAR) {
        cond = JsonCond.newAttrCond(attrName).lteYear(v.getValue());
      }
    } else if (filter instanceof LocalDateDateFilter) {
      LocalDateDateFilter v = (LocalDateDateFilter) filter;
      String attrName = attrCodeFunc.apply(classCode, v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ_DAY) {
        cond = JsonCond.newAttrCond(attrName).eqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_DAY) {
        cond = JsonCond.newAttrCond(attrName).gtDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_DAY) {
        cond = JsonCond.newAttrCond(attrName).gteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_DAY) {
        cond = JsonCond.newAttrCond(attrName).neqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_DAY) {
        cond = JsonCond.newAttrCond(attrName).ltDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_DAY) {
        cond = JsonCond.newAttrCond(attrName).lteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.EQ_MONTH) {
        cond = JsonCond.newAttrCond(attrName).eqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_MONTH) {
        cond = JsonCond.newAttrCond(attrName).gtMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_MONTH) {
        cond = JsonCond.newAttrCond(attrName).gteMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_MONTH) {
        cond = JsonCond.newAttrCond(attrName).neqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_MONTH) {
        cond = JsonCond.newAttrCond(attrName).ltMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_MONTH) {
        cond = JsonCond.newAttrCond(attrName).lteMonth(v.getValue());
      }
    } else if (filter instanceof ValueFilter) {
      ValueFilter v = (ValueFilter) filter;
      String attrName = attrCodeFunc.apply(classCode, v.getAttrCode());
      if (v.getOper() == FilterOperation.EQ) {
        cond = JsonCond.newAttrCond(attrName).eq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GT) {
        cond = JsonCond.newAttrCond(attrName).gt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GTE) {
        cond = JsonCond.newAttrCond(attrName).gte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ) {
        cond = JsonCond.newAttrCond(attrName).neq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LT) {
        cond = JsonCond.newAttrCond(attrName).lt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LTE) {
        cond = JsonCond.newAttrCond(attrName).lte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.IN) {
        IN inst = (IN) v;
        cond = JsonCond.newAttrCond(attrName).in(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN) {
        NotIN inst = (NotIN) v;
        cond = JsonCond.newAttrCond(attrName).notIn(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.ISNULL) {
        cond = JsonCond.newAttrCond(attrName).isNull(true);
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        cond = JsonCond.newAttrCond(attrName).isNotNull(true);
      } else if (v.getOper() == FilterOperation.LIKE) {
        cond = JsonCond.newAttrCond(attrName).like(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_LIKE) {
        cond = JsonCond.newAttrCond(attrName).fuzzyLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_ORDER_LIKE) {
        cond = JsonCond.newAttrCond(attrName).fuzzyOrderLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STARTS_WITH) {
        cond = JsonCond.newAttrCond(attrName).startsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_STARTS_WITH) {
        cond = JsonCond.newAttrCond(attrName).notStartsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_RANGE) {
        ContainsRange inst = (ContainsRange) v;
        cond = JsonCond.newAttrCond(attrName).containsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.OVERLAPSRANGE) {
        OverlapsRange inst = (OverlapsRange) v;
        cond = JsonCond.newAttrCond(attrName).overlapsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_ELEMENT) {
        cond = JsonCond.newAttrCond(attrName).containsElement(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.EQ_RANGE) {
        EQRange inst = (EQRange) v;
        cond = JsonCond.newAttrCond(attrName).eqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GT_RANGE) {
        GTRange inst = (GTRange) v;
        cond = JsonCond.newAttrCond(attrName).gtRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GTE_RANGE) {
        GTERange inst = (GTERange) v;
        cond = JsonCond.newAttrCond(attrName).gteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ_RANGE) {
        NEQRange inst = (NEQRange) v;
        cond = JsonCond.newAttrCond(attrName).neqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LT_RANGE) {
        LTRange inst = (LTRange) v;
        cond = JsonCond.newAttrCond(attrName).ltRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LTE_RANGE) {
        LTERange inst = (LTERange) v;
        cond = JsonCond.newAttrCond(attrName).lteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        Pair pair = b.getValue();
        cond = JsonCond.newAttrCond(attrName).between(new JsonBetween(stringValue(pair.getFrom()), stringValue(pair.getTo())));
      } else if (v.getOper() == FilterOperation.CONTAINS) {
        ContainsKVP b = (ContainsKVP) v;
        KeyValuePair entry = b.getValue();
        cond = JsonCond.newAttrCond(attrName).contains(JsonContainsKVP.from(stringValue(entry.getKey()), stringValue(entry.getValue())));
      }
    } else if (filter instanceof LinkFilter) {
      LinkFilter l = (LinkFilter) filter;
      String attrName = attrCodeFunc.apply(classCode, l.getAttrCode());
      String refClassCode = refClassCodeFunc.apply(classCode, l.getAttrCode());
      if (l.getFunction() == AnalyticFunction.EXISTS) {
        cond = JsonCond.newAttrCond(attrName).exists(
            toExp(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      } else if (l.getFunction() == AnalyticFunction.NOTEXISTS) {
        cond = JsonCond.newAttrCond(attrName).notExists(
            toExp(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      }
    }
    return cond != null ? new JsonExpr(cond) : null;
  }

  /**
   * Учет сортировки
   *
   * @param order        сортировка
   * @param classCode    Код класса
   * @param attrCodeFunc Логика маппинга атрибута
   * @return сортировка
   */
  private JsonOrder toOrder(mp.jprime.dataaccess.params.query.Order order,
                            String classCode,
                            BiFunction<String, String, String> attrCodeFunc) {
    String attrName = attrCodeFunc.apply(classCode, order.getAttr());
    if (attrName == null) {
      return null;
    }
    if (order.getOrder() == OrderDirection.ASC) {
      return new JsonOrder().asc(attrName);
    } else if (order.getOrder() == OrderDirection.DESC) {
      return new JsonOrder().desc(attrName);
    }
    return null;
  }


  private Collection<JsonExpr> clear(Collection<JsonExpr> list) {
    if (list == null) {
      return null;
    }
    list.removeIf(Objects::isNull);
    list.removeIf(
        e -> e.getCond() == null && clear(e.getOr()) == null && clear(e.getAnd()) == null
    );
    return list.isEmpty() ? null : list;
  }

  /**
   * Учет условия
   *
   * @param exp Условие
   * @return Filter Условие
   */
  public Filter toFilter(JsonExpr exp) {
    JsonCond c = exp.getCond();
    Collection<JsonExpr> and = clear(exp.getAnd());
    Collection<JsonExpr> or = clear(exp.getOr());
    if (c != null) {
      JsonBetween between = c.getBetween();
      JsonContainsKVP contains = c.getContains();
      // TODO сделать красиво
      if (c.getEq() != null) {
        return Filter.attr(c.getAttr()).eq(c.getEq());
      } else if (c.getGt() != null) {
        return Filter.attr(c.getAttr()).gt(c.getGt());
      } else if (c.getGte() != null) {
        return Filter.attr(c.getAttr()).gte(c.getGte());
      } else if (c.getNeq() != null) {
        return Filter.attr(c.getAttr()).neq(c.getNeq());
      } else if (c.getLt() != null) {
        return Filter.attr(c.getAttr()).lt(c.getLt());
      } else if (c.getLte() != null) {
        return Filter.attr(c.getAttr()).lte(c.getLte());
      } else if (c.getIn() != null) {
        return Filter.attr(c.getAttr()).in(c.getIn());
      } else if (c.getNotIn() != null) {
        return Filter.attr(c.getAttr()).notIn(c.getNotIn());
      } else if (c.getIsNotNull() != null && c.getIsNotNull()) {
        return Filter.attr(c.getAttr()).isNotNull();
      } else if (c.getIsNull() != null && c.getIsNull()) {
        return Filter.attr(c.getAttr()).isNull();
      } else if (c.getLike() != null) {
        return Filter.attr(c.getAttr()).like(c.getLike());
      } else if (c.getFuzzyLike() != null) {
        return Filter.attr(c.getAttr()).fuzzyLike(c.getFuzzyLike());
      } else if (c.getFuzzyOrderLike() != null) {
        return Filter.attr(c.getAttr()).fuzzyOrderLike(c.getFuzzyOrderLike());
      } else if (c.getStartsWith() != null) {
        return Filter.attr(c.getAttr()).startWith(c.getStartsWith());
      } else if (c.getNotStartsWith() != null) {
        return Filter.attr(c.getAttr()).notStartWith(c.getNotStartsWith());
      } else if (between != null) {
        return Filter.attr(c.getAttr()).between(Pair.from(between.getFrom(), between.getTo()));
      } else if (contains != null) {
        return Filter.attr(c.getAttr()).contains(KeyValuePair.from(contains.getKey(), contains.getValue()));
      } else if (c.getExists() != null) {
        return Filter.attr(c.getAttr()).exists(toFilter(c.getExists()));
      } else if (c.getNotExists() != null) {
        return Filter.attr(c.getAttr()).notExists(toFilter(c.getNotExists()));
      } else if (c.getEqYear() != null) {
        return Filter.attr(c.getAttr()).eqYear(c.getEqYear());
      } else if (c.getNeqYear() != null) {
        return Filter.attr(c.getAttr()).neqYear(c.getNeqYear());
      } else if (c.getLtYear() != null) {
        return Filter.attr(c.getAttr()).ltYear(c.getLtYear());
      } else if (c.getLteYear() != null) {
        return Filter.attr(c.getAttr()).lteYear(c.getLteYear());
      } else if (c.getGtYear() != null) {
        return Filter.attr(c.getAttr()).gtYear(c.getGtYear());
      } else if (c.getGteYear() != null) {
        return Filter.attr(c.getAttr()).gteYear(c.getGteYear());
      } else if (c.getEqMonth() != null) {
        return Filter.attr(c.getAttr()).eqMonth(c.getEqMonth());
      } else if (c.getNeqMonth() != null) {
        return Filter.attr(c.getAttr()).neqMonth(c.getNeqMonth());
      } else if (c.getLtMonth() != null) {
        return Filter.attr(c.getAttr()).ltMonth(c.getLtMonth());
      } else if (c.getLteMonth() != null) {
        return Filter.attr(c.getAttr()).lteMonth(c.getLteMonth());
      } else if (c.getGtMonth() != null) {
        return Filter.attr(c.getAttr()).gtMonth(c.getGtMonth());
      } else if (c.getGteMonth() != null) {
        return Filter.attr(c.getAttr()).gteMonth(c.getGteMonth());
      } else if (c.getEqDay() != null) {
        return Filter.attr(c.getAttr()).eqDay(c.getEqDay());
      } else if (c.getNeqDay() != null) {
        return Filter.attr(c.getAttr()).neqDay(c.getNeqDay());
      } else if (c.getLtDay() != null) {
        return Filter.attr(c.getAttr()).ltDay(c.getLtDay());
      } else if (c.getLteDay() != null) {
        return Filter.attr(c.getAttr()).lteDay(c.getLteDay());
      } else if (c.getGtDay() != null) {
        return Filter.attr(c.getAttr()).gtDay(c.getGtDay());
      } else if (c.getGteDay() != null) {
        return Filter.attr(c.getAttr()).gteDay(c.getGteDay());
      } else if (c.getContainsElement() != null) {
        return Filter.attr(c.getAttr()).containsElement(c.getContainsElement());
      } else if (c.getContainsRange() != null) {
        return Filter.attr(c.getAttr()).containsRange(toStringRange(c.getContainsRange()));
      } else if (c.getOverlapsRange() != null) {
        return Filter.attr(c.getAttr()).overlapsRange(toStringRange(c.getOverlapsRange()));
      } else if (c.getEqRange() != null) {
        return Filter.attr(c.getAttr()).eqRange(toStringRange(c.getEqRange()));
      } else if (c.getGtRange() != null) {
        return Filter.attr(c.getAttr()).gtRange(toStringRange(c.getGtRange()));
      } else if (c.getGteRange() != null) {
        return Filter.attr(c.getAttr()).gteRange(toStringRange(c.getGteRange()));
      } else if (c.getNeqRange() != null) {
        return Filter.attr(c.getAttr()).neqRange(toStringRange(c.getNeqRange()));
      } else if (c.getLtRange() != null) {
        return Filter.attr(c.getAttr()).ltRange(toStringRange(c.getLtRange()));
      } else if (c.getLteRange() != null) {
        return Filter.attr(c.getAttr()).lteRange(toStringRange(c.getLteRange()));
      } else if (c.getFeature() != null) {
        if (c.getCheckDay() != null) {
          return Filter.feature(c.getFeature()).check(c.getCheckDay());
        } else if (c.getCheckFromDay() != null && c.getCheckToDay() != null) {
          return Filter.feature(c.getFeature()).check(c.getCheckFromDay(), c.getCheckToDay());
        } else {
          return Filter.feature(c.getFeature()).check();
        }
      }
    } else if (and != null && !and.isEmpty()) {
      return Filter.and(and.stream().map(this::toFilter).filter(Objects::nonNull).collect(Collectors.toList()));
    } else if (or != null && !or.isEmpty()) {
      return Filter.or(or.stream().map(this::toFilter).filter(Objects::nonNull).collect(Collectors.toList()));
    }
    return null;
  }

  /**
   * Учет условия
   *
   * @param jsonNode Обёртка
   * @return Filter условие
   */
  public Filter toFilter(JPJsonNode jsonNode) {
    if (jsonNode == null) {
      return null;
    }
    return toFilter(jpJsonMapper.toObject(JsonExpr.class, jsonNode));
  }

  /**
   * Учет сортировки
   *
   * @param builder Построитель JPSelect
   * @param order   Условие сортировки
   */
  private void appendOrderBy(JPSelect.Builder builder, JsonOrder order) {
    if (order.getAsc() != null) {
      builder.orderBy(order.getAsc(), OrderDirection.ASC);
    } else if (order.getDesc() != null) {
      builder.orderBy(order.getDesc(), OrderDirection.DESC);
    }
  }

  /**
   * Создает описание данных
   *
   * @param json Строка запроса
   * @return Описание данных
   */
  public JsonObjectData getObjectData(String json) {
    return jpJsonMapper.toObject(JsonObjectData.class, json);
  }

  /**
   * Создает описание данных
   *
   * @param json Строка запроса
   * @return Описание данных
   */
  public JsonUpdate getUpdate(String json) {
    return jpJsonMapper.toObject(JsonUpdate.class, json);
  }

  /**
   * Создает запрос значений по умолчанию
   *
   * @param json Строка запроса
   * @return Описание запроса
   */
  public JsonDefValuesQuery getDefValuesQuery(String json) {
    return jpJsonMapper.toObject(JsonDefValuesQuery.class, json);
  }

  /**
   * Создает запрос на пополнение значений
   *
   * @param json Строка запроса
   * @return Описание запроса
   */
  public JsonApplyValuesQuery getApplyValuesQuery(String json) {
    return jpJsonMapper.toObject(JsonApplyValuesQuery.class, json);
  }

  /**
   * Создает описание данных
   *
   * @param json Строка запроса
   * @return Описание данных
   */
  public JsonIdentityData getIdentityData(String json) {
    return jpJsonMapper.toObject(JsonIdentityData.class, json);
  }

  /**
   * Создает описание данных
   *
   * @param json Строка запроса
   * @return Описание данных
   */
  public JsonIdentityData getDeleteData(String json) {
    return jpJsonMapper.toObject(JsonIdentityData.class, json);
  }

  /**
   * Создает описание данных
   *
   * @param data DeleteData
   * @return Описание данных
   */
  public String toString(JsonIdentityData data) {
    return jpJsonMapper.toString(data);
  }

  /**
   * Создает описание данных
   *
   * @param data ObjectData
   * @return Описание данных
   */
  public String toString(JsonUpdate data) {
    return jpJsonMapper.toString(data);
  }

  /**
   * Создает описание создания
   *
   * @param query Описание запроса
   * @param auth  Данные аутентификации
   * @return Описание создания
   */
  public JPCreate.Builder getCreate(JsonObjectData query, AuthInfo auth) {
    return getCreate(query, null, auth);
  }

  /**
   * Создает описание создания
   *
   * @param query  Описание запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание создания
   */
  public JPCreate.Builder getCreate(JsonObjectData query, Source source, AuthInfo auth) {
    if (query == null) {
      return null;
    }
    try {
      JPCreate.Builder builder = JPCreate
          .create(query.getClassCode())
          .source(source)
          .auth(auth);
      Optional.ofNullable(query.getData())
          .ifPresent(m -> m.forEach(builder::set));
      Optional.ofNullable(query.getLinkedData())
          .ifPresent(m -> m.forEach((x, y) -> addWith(x, y, builder, source, auth)));
      return builder;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание создания
   *
   * @param query Описание запроса
   * @return Описание создания
   */
  public JsonObjectData toObjectData(JPCreate query) {
    return toObjectData(query, CLASS_MAP_FUNCTION, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Создает описание создания
   *
   * @param query            Описание запроса
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга ссылочного класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание создания
   */
  public JsonObjectData toObjectData(JPCreate query,
                                     Function<String, String> classCodeFunc,
                                     BiFunction<String, String, String> refClassCodeFunc,
                                     BiFunction<String, String, String> attrCodeFunc) {
    if (query == null) {
      return null;
    }
    String fromClassCode = query.getJpClass();
    String toClassCode = classCodeFunc.apply(fromClassCode);

    JsonObjectData data = toJsonObjectData(query, fromClassCode, toClassCode, attrCodeFunc);

    Optional.ofNullable(query.getLinkedData())
        .ifPresent(m -> m.forEach((x, y) -> {
          String attr = attrCodeFunc.apply(fromClassCode, x);
          if (attr != null) {
            addCreateWith(attr, classCodeFunc, refClassCodeFunc, attrCodeFunc, y, data);
          }
        }));
    return data;
  }

  private JsonObjectData toJsonObjectData(JPSave query,
                                          String fromClassCode,
                                          String toClassCode,
                                          BiFunction<String, String, String> attrCodeFunc) {
    JsonObjectData data = new JsonObjectData();
    data.setClassCode(toClassCode);
    Optional.ofNullable(query.getData())
        .ifPresent(m -> m.forEach((x, y) -> {
          String attr = attrCodeFunc.apply(fromClassCode, x);
          if (attr != null) {
            data.getData().put(attr, y);
          }
        }));
    return data;
  }

  private JsonUpdate toJsonUpdate(JPUpdate update,
                                  String fromClassCode,
                                  String toClassCode,
                                  BiFunction<String, String, String> attrCodeFunc) {
    JsonUpdate data = new JsonUpdate();
    data.setClassCode(toClassCode);
    Optional.ofNullable(update.getData())
        .ifPresent(m -> m.forEach((x, y) -> {
          String attr = attrCodeFunc.apply(fromClassCode, x);
          if (attr != null) {
            data.getData().put(attr, y);
          }
        }));
    return data;
  }


  /**
   * Создает описание удаления
   *
   * @param query         Описание запроса
   * @param classCodeFunc Логика маппинга класса
   * @return Описание удаления
   */
  public JsonObjectData toObjectData(JPDelete query,
                                     Function<String, String> classCodeFunc) {
    if (query == null) {
      return null;
    }
    String classCode = classCodeFunc.apply(query.getJpId().getJpClass());

    JsonObjectData data = new JsonObjectData();
    data.setId(query.getJpId().getId());
    data.setClassCode(classCode);
    return data;
  }

  private void addCreateWith(String attrCode,
                             Function<String, String> classCodeFunc,
                             BiFunction<String, String, String> refClassCodeFunc,
                             BiFunction<String, String, String> attrCodeFunc,
                             Collection<JPCreate> creates,
                             JsonObjectData data) {
    if (data == null || attrCode == null || creates == null || creates.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    creates.forEach(x -> linked.getCreate().add(toObjectData(x, classCodeFunc, refClassCodeFunc, attrCodeFunc)));
  }

  private void addUpdateWith(String attrCode,
                             Function<String, String> classCodeFunc,
                             BiFunction<String, String, String> refClassCodeFunc,
                             BiFunction<String, String, String> attrCodeFunc,
                             Collection<JPUpdate> updates,
                             JsonObjectData data) {
    if (data == null || attrCode == null || updates == null || updates.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    updates.forEach(x -> linked.getUpdate().add(toJsonUpdate(x, classCodeFunc, refClassCodeFunc, attrCodeFunc)));
  }

  private void addDeleteWith(String attrCode,
                             Function<String, String> classCodeFunc,
                             Collection<JPDelete> deletes,
                             JsonObjectData data) {
    if (data == null || attrCode == null || deletes == null || deletes.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    deletes.stream()
        .filter(Objects::nonNull)
        .forEach(x -> linked.getDelete().add(toObjectData(x, classCodeFunc)));
  }

  /**
   * Создает описание создания
   *
   * @param json Строка запроса
   * @param auth Данные аутентификации
   * @return Описание создания
   */
  public JPCreate.Builder getCreate(String json, AuthInfo auth) {
    return getCreate(json, null, auth);
  }

  /**
   * Создает описание создания
   *
   * @param json   Строка запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание создания
   */
  public JPCreate.Builder getCreate(String json, Source source, AuthInfo auth) {
    try {
      return getCreate(getObjectData(json), source, auth);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание обновления
   *
   * @param json Строка запроса
   * @param auth Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(String json, AuthInfo auth) {
    return getUpdate(json, null, auth);
  }

  /**
   * Создает описание обновления
   *
   * @param json   Строка запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(String json, Source source, AuthInfo auth) {
    try {
      return getUpdate(getUpdate(json), source, auth);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }


  /**
   * Создает описание удаления
   *
   * @param json Строка запроса
   * @param auth Данные аутентификации
   * @return Описание удаления
   */
  public JPDelete.Builder getDelete(String json, AuthInfo auth) {
    try {
      return getDelete(getDeleteData(json), auth);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание удаления
   *
   * @param query Описание запроса
   * @param auth  Данные аутентификации
   * @return Описание удаления
   */
  public JPDelete.Builder getDelete(JsonIdentityData query, AuthInfo auth) {
    return getDelete(query, null, auth);
  }


  /**
   * Создает описание удаления
   *
   * @param query  Описание запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание удаления
   */
  public JPDelete.Builder getDelete(JsonIdentityData query, Source source, AuthInfo auth) {
    if (query == null) {
      return null;
    }
    return JPDelete
        .delete(JPId.get(query.getClassCode(), query.getId()))
        .source(source)
        .auth(auth);
  }

  /**
   * Создает описание удаления
   *
   * @param query Описание запроса
   * @return Описание удаления
   */
  public JsonIdentityData toIdentityData(JPDelete query) {
    JsonIdentityData data = new JsonIdentityData();
    data.setId(query.getJpId().getId());
    data.setClassCode(query.getJpId().getJpClass());
    return data;
  }

  /**
   * Создает описание обновления
   *
   * @param update Описание запроса
   * @param auth   Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(JsonUpdate update, AuthInfo auth) {
    return getUpdate(update, null, auth);
  }

  /**
   * Создает описание обновления
   *
   * @param update Описание запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(JsonUpdate update, Source source, AuthInfo auth) {
    try {
      if (update == null) {
        return null;
      }
      if (update.getId() == null) {
        return null;
      }
      JPUpdate.Builder builder = JPUpdate
          .update(JPId.get(update.getClassCode(), update.getId()))
          .source(source)
          .auth(auth);
      Optional.ofNullable(update.getData())
          .ifPresent(m -> m.forEach(builder::set));
      Optional.ofNullable(update.getLinkedData())
          .ifPresent(m -> m.forEach((x, y) -> addWith(x, y, builder, source, auth)));
      Optional.ofNullable(update.getFilter())
          .ifPresent(x -> builder.where(toFilter(x)));
      return builder;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }


  /**
   * Создает описание обновления
   *
   * @param query Описание запроса
   * @return Описание обновления
   */
  public JsonUpdate toJsonUpdate(JPUpdate query) {
    return toJsonUpdate(query, CLASS_MAP_FUNCTION, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Создает описание обновления
   *
   * @param update           Описание запроса
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание обновления
   */
  public JsonUpdate toJsonUpdate(JPUpdate update,
                                 Function<String, String> classCodeFunc,
                                 BiFunction<String, String, String> refClassCodeFunc,
                                 BiFunction<String, String, String> attrCodeFunc) {
    if (update == null) {
      return null;
    }
    String classCode = update.getJpId().getJpClass();

    JsonUpdate data = toJsonUpdate(update, classCode, classCodeFunc.apply(update.getJpId().getJpClass()), attrCodeFunc);
    data.setId(update.getJpId().getId());

    Optional.ofNullable(update.getLinkedCreate())
        .ifPresent(m -> m.forEach((x, y) -> addCreateWith(
            attrCodeFunc.apply(classCode, x),
            classCodeFunc,
            refClassCodeFunc,
            attrCodeFunc,
            y,
            data))
        );
    Optional.ofNullable(update.getLinkedUpdate())
        .ifPresent(m -> m.forEach((x, y) -> addUpdateWith(
            attrCodeFunc.apply(classCode, x),
            classCodeFunc,
            refClassCodeFunc,
            attrCodeFunc,
            y,
            data)));
    Optional.ofNullable(update.getLinkedDelete())
        .ifPresent(m -> m.forEach((x, y) -> addDeleteWith(
            attrCodeFunc.apply(classCode, x),
            classCodeFunc,
            y,
            data)));
    Optional.ofNullable(update.getWhere())
        .ifPresent(x -> data.setFilter(toExp(x, classCode, refClassCodeFunc, attrCodeFunc)));
    return data;
  }

  private void addWith(String attrName, JsonObjectLinkedData data, JPCreate.Builder builder,
                       Source source, AuthInfo auth) {
    if (data == null || attrName == null) {
      return;
    }
    Optional.ofNullable(data.getCreate())
        .ifPresent(m -> m.stream()
            .filter(Objects::nonNull)
            .forEach(x -> builder.addWith(attrName, getCreate(x, source, auth).build()))
        );
  }

  private void addWith(String attrName, JsonObjectLinkedData data, JPUpdate.Builder builder,
                       Source source, AuthInfo auth) {
    if (data == null || attrName == null) {
      return;
    }
    Optional.ofNullable(data.getCreate())
        .ifPresent(m -> m.stream()
            .filter(Objects::nonNull)
            .forEach(x -> builder.addWith(attrName, getCreate(x, source, auth).build()))
        );
    Optional.ofNullable(data.getUpdate())
        .ifPresent(m -> m.stream()
            .filter(Objects::nonNull)
            .forEach(x -> builder.addWith(attrName, getUpdate(x, source, auth).build()))
        );
    Optional.ofNullable(data.getDelete())
        .ifPresent(m -> m.stream()
            .filter(Objects::nonNull)
            .forEach(x -> builder.addWith(attrName, getDelete(x, source, auth).build()))
        );
  }

  /**
   * Создает описание запроса
   *
   * @param update JPUpdate
   * @return Описание выборки
   */
  public String toString(JPUpdate update) {
    return toString(toJsonUpdate(update));
  }

  /**
   * Создает описание запроса
   *
   * @param update           JPUpdate
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга ссылочного класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание выборки
   */
  public String toString(JPUpdate update,
                         Function<String, String> classCodeFunc,
                         BiFunction<String, String, String> refClassCodeFunc,
                         BiFunction<String, String, String> attrCodeFunc) {
    return toString(toJsonUpdate(update, classCodeFunc, refClassCodeFunc, attrCodeFunc));
  }

  /**
   * Создает описание запроса
   *
   * @param create JPCreate
   * @return Описание выборки
   */
  public String toString(JPCreate create) {
    return toString(toObjectData(create));
  }

  /**
   * Создает описание запроса
   *
   * @param create           JPCreate
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга ссылочного класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание выборки
   */
  public String toString(JPCreate create,
                         Function<String, String> classCodeFunc,
                         BiFunction<String, String, String> refClassCodeFunc,
                         BiFunction<String, String, String> attrCodeFunc) {
    return toString(toObjectData(create, classCodeFunc, refClassCodeFunc, attrCodeFunc));
  }

  /**
   * Создает описание запроса
   *
   * @param delete JPDelete
   * @return Описание выборки
   */
  public String toString(JPDelete delete) {
    return toString(toIdentityData(delete));
  }
}
