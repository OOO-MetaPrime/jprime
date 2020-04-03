package mp.jprime.json.services;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.enums.AnalyticFunction;
import mp.jprime.dataaccess.params.query.enums.BooleanCondition;
import mp.jprime.dataaccess.params.query.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.enums.OrderDirection;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.*;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Заполнение SELECT на основе REST
 */
@Service
public class QueryService implements JsonMapper {
  /**
   * Максимальное количество в выборке по-умолчанию
   */
  private static final int MAX_LIMIT = 50;

  /**
   * Создает описание выборки
   *
   * @param json Строка запроса
   * @return Описание выборки
   */
  public JsonQuery getQuery(String json) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, JsonQuery.class);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание выборки
   *
   * @param query Query
   * @return Описание выборки
   */
  public String toString(JsonQuery query) {
    try {
      return OBJECT_MAPPER.writeValueAsString(query);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
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
  public JPSelect.Builder getSelect(String jpClassCode, JsonQuery query, AuthInfo auth) {
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
   * Создает описание выборки
   *
   * @param select Описание запроса
   * @return Описание выборки
   */
  public JsonQuery getQuery(JPSelect select) {
    JsonQuery query = new JsonQuery();
    query.setLimit(select.getLimit());
    query.setOffset(select.getOffset());
    query.setTotalCount(select.isTotalCount());
    query.setAttrs(select.getAttrs());
    query.setLinkAttrs(select.getAttrs()
        .stream()
        .collect(HashMap::new,
            (m, v) -> {
              Collection<String> attrs = select.getLinkAttrs(v);
              if (attrs != null && !attrs.isEmpty()) {
                m.put(v, StringUtils.join(attrs, ","));
              }
            },
            HashMap::putAll)
    );
    Optional.ofNullable(select.getWhere())
        .ifPresent(x -> query.setFilter(toExp(x)));
    query.setOrders(select.getOrderBy().stream().map(this::toOrder).collect(Collectors.toList()));
    return query;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private String stringValue(Object v) {
    return String.valueOf(v);
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
  private JsonExpr toExp(Filter filter) {
    if (filter instanceof BooleanFilter) {
      BooleanFilter c = (BooleanFilter) filter;
      if (c.getCond() == BooleanCondition.AND) {
        return new JsonExpr().and(c.getFilters().stream().map(this::toExp).collect(Collectors.toList()));
      } else if (c.getCond() == BooleanCondition.OR) {
        return new JsonExpr().or(c.getFilters().stream().map(this::toExp).collect(Collectors.toList()));
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
      if (v.getOper() == FilterOperation.EQ_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).eqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gtYear(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gteYear(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).neqYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).ltYear(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_YEAR) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).lteYear(v.getValue());
      }
    } else if (filter instanceof LocalDateDateFilter) {
      LocalDateDateFilter v = (LocalDateDateFilter) filter;
      if (v.getOper() == FilterOperation.EQ_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).eqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gtDay(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).neqDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).ltDay(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_DAY) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).lteDay(v.getValue());
      } else if (v.getOper() == FilterOperation.EQ_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).eqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GT_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gtMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.GTE_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gteMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).neqMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LT_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).ltMonth(v.getValue());
      } else if (v.getOper() == FilterOperation.LTE_MONTH) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).lteMonth(v.getValue());
      }
    } else if (filter instanceof ValueFilter) {
      ValueFilter v = (ValueFilter) filter;
      if (v.getOper() == FilterOperation.EQ) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).eq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GT) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GTE) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).gte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).neq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LT) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).lt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LTE) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).lte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.IN) {
        IN in = (IN) v;
        cond = JsonCond.newAttrCond(v.getAttrCode()).in(toStrings(in.getValue()));
      } else if (v.getOper() == FilterOperation.NOTIN) {
        NotIN notIn = (NotIN) v;
        cond = JsonCond.newAttrCond(v.getAttrCode()).notIn(toStrings(notIn.getValue()));
      } else if (v.getOper() == FilterOperation.ISNULL) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).isNull(true);
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).isNotNull(true);
      } else if (v.getOper() == FilterOperation.LIKE) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).like(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZYLIKE) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).fuzzyLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZYORDERLIKE) {
        cond = JsonCond.newAttrCond(v.getAttrCode()).fuzzyOrderLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        Pair pair = b.getValue();
        cond = JsonCond.newAttrCond(v.getAttrCode()).between(new JsonBetweenCond(stringValue(pair.getFrom()), stringValue(pair.getTo())));
      }
    } else if (filter instanceof LinkFilter) {
      LinkFilter l = (LinkFilter) filter;
      if (l.getFunction() == AnalyticFunction.EXISTS) {
        cond = JsonCond.newCond().exists(toExp(l.getFilter()));
      } else if (l.getFunction() == AnalyticFunction.NOTEXISTS) {
        cond = JsonCond.newCond().notExists(toExp(l.getFilter()));
      }
    }
    return cond != null ? new JsonExpr(cond) : null;
  }

  /**
   * Учет сортировки
   *
   * @param order сортировка
   * @return сортировка
   */
  private JsonOrder toOrder(mp.jprime.dataaccess.params.query.Order order) {
    if (order.getOrder() == OrderDirection.ASC) {
      return new JsonOrder().asc(order.getAttr());
    } else if (order.getOrder() == OrderDirection.DESC) {
      return new JsonOrder().desc(order.getAttr());
    }
    return null;
  }


  /**
   * Учет условия
   *
   * @param exp Условие
   * @return Filter Условие
   */
  private Filter toFilter(JsonExpr exp) {
    JsonCond c = exp.getCond();
    Collection<JsonExpr> and = exp.getAnd();
    Collection<JsonExpr> or = exp.getOr();
    if (c != null) {
      JsonBetweenCond between = c.getBetween();
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
      } else if (between != null) {
        return Filter.attr(c.getAttr()).between(Pair.from(between.getFrom(), between.getTo()));
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
    try {
      return OBJECT_MAPPER.readValue(json, JsonObjectData.class);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание данных
   *
   * @param json Строка запроса
   * @return Описание данных
   */
  public JsonIdentityData getDeleteData(String json) {
    try {
      return OBJECT_MAPPER.readValue(json, JsonIdentityData.class);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание данных
   *
   * @param data DeleteData
   * @return Описание данных
   */
  public String toString(JsonIdentityData data) {
    try {
      return OBJECT_MAPPER.writeValueAsString(data);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание данных
   *
   * @param data ObjectData
   * @return Описание данных
   */
  public String toString(JsonObjectData data) {
    try {
      return OBJECT_MAPPER.writeValueAsString(data);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
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
    if (query == null) {
      return null;
    }
    JsonObjectData data = new JsonObjectData();
    data.setClassCode(query.getJpClass());
    Optional.ofNullable(query.getData())
        .ifPresent(m -> m.forEach((x, y) -> data.getData().put(x, y)));
    Optional.ofNullable(query.getLinkedData())
        .ifPresent(m -> m.forEach((x, y) -> addCreateWith(x, y, data)));
    return data;
  }

  /**
   * Создает описание удаления
   *
   * @param query Описание запроса
   * @return Описание удаления
   */
  public JsonObjectData toObjectData(JPDelete query) {
    if (query == null) {
      return null;
    }
    JsonObjectData data = new JsonObjectData();
    data.setId(query.getJpId().getId());
    data.setClassCode(query.getJpClass());
    return data;
  }

  private void addCreateWith(String attrCode, Collection<JPCreate> creates, JsonObjectData data) {
    if (data == null || attrCode == null || creates == null || creates.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    creates.forEach(x -> linked.getCreate().add(toObjectData(x)));
  }

  private void addUpdateWith(String attrCode, Collection<JPUpdate> updates, JsonObjectData data) {
    if (data == null || attrCode == null || updates == null || updates.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    updates.forEach(x -> linked.getUpdate().add(toObjectData(x)));
  }

  private void addDeleteWith(String attrCode, Collection<JPDelete> deletes, JsonObjectData data) {
    if (data == null || attrCode == null || deletes == null || deletes.isEmpty()) {
      return;
    }
    JsonObjectLinkedData linked = data.getLinkedData().computeIfAbsent(attrCode, x -> new JsonObjectLinkedData());
    deletes.stream()
        .filter(Objects::nonNull)
        .forEach(x -> linked.getDelete().add(toObjectData(x)));
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
      return getUpdate(getObjectData(json), source, auth);
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
   * @param query Описание запроса
   * @param auth  Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(JsonObjectData query, AuthInfo auth) {
    return getUpdate(query, null, auth);
  }

  /**
   * Создает описание обновления
   *
   * @param query  Описание запроса
   * @param source Источник данных
   * @param auth   Данные аутентификации
   * @return Описание обновления
   */
  public JPUpdate.Builder getUpdate(JsonObjectData query, Source source, AuthInfo auth) {
    try {
      if (query == null) {
        return null;
      }
      if (query.getId() == null) {
        return null;
      }
      JPUpdate.Builder builder = JPUpdate
          .update(JPId.get(query.getClassCode(), query.getId()))
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
   * Создает описание обновления
   *
   * @param query Описание запроса
   * @return Описание обновления
   */
  public JsonObjectData toObjectData(JPUpdate query) {
    JsonObjectData data = new JsonObjectData();
    data.setId(query.getJpId().getId());
    data.setClassCode(query.getJpId().getJpClass());
    Optional.ofNullable(query.getData())
        .ifPresent(m -> m.forEach((x, y) -> data.getData().put(x, y)));
    Optional.ofNullable(query.getLinkedCreate())
        .ifPresent(m -> m.forEach((x, y) -> addCreateWith(x, y, data)));
    Optional.ofNullable(query.getLinkedUpdate())
        .ifPresent(m -> m.forEach((x, y) -> addUpdateWith(x, y, data)));
    Optional.ofNullable(query.getLinkedDelete())
        .ifPresent(m -> m.forEach((x, y) -> addDeleteWith(x, y, data)));
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
    return toString(toObjectData(update));
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
   * @param delete JPDelete
   * @return Описание выборки
   */
  public String toString(JPDelete delete) {
    return toString(toIdentityData(delete));
  }
}
