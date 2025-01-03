package mp.jprime.json.services;

import mp.jprime.common.JPOrder;
import mp.jprime.common.JPOrderDirection;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.enums.*;
import mp.jprime.dataaccess.params.*;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.*;
import mp.jprime.lang.JPJsonNode;
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
public class QueryService {
  private JPJsonMapper jpJsonMapper;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
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
      return json;
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
   * Учет условия
   *
   * @param filter Условие
   * @return Expr Условие
   */
  public JsonExpr toExp(Filter filter) {
    return JsonExpr.toJson(filter);
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
    return JsonExpr.toJson(filter, classCode, refClassCodeFunc, attrCodeFunc);
  }

  /**
   * Учет сортировки
   *
   * @param order        сортировка
   * @param classCode    Код класса
   * @param attrCodeFunc Логика маппинга атрибута
   * @return сортировка
   */
  private JsonJPOrder toOrder(JPOrder order,
                              String classCode,
                              BiFunction<String, String, String> attrCodeFunc) {
    String attrName = attrCodeFunc.apply(classCode, order.getAttr());
    if (attrName == null) {
      return null;
    }
    if (order.getOrder() == JPOrderDirection.ASC) {
      return JsonJPOrder.asc(attrName);
    } else if (order.getOrder() == JPOrderDirection.DESC) {
      return JsonJPOrder.desc(attrName);
    }
    return null;
  }

  /**
   * Учет условия
   *
   * @param exp Условие
   * @return Filter Условие
   */
  public Filter toFilter(JsonExpr exp) {
    return JsonExpr.toFilter(exp);
  }

  /**
   * Учет условия
   *
   * @param json Строка с json
   * @return Filter условие
   */
  public Filter toFilter(String json) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    return toFilter(jpJsonMapper.toObject(JsonExpr.class, json));
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
  private void appendOrderBy(JPSelect.Builder builder, JsonJPOrder order) {
    if (order.getAsc() != null) {
      builder.orderBy(order.getAsc(), JPOrderDirection.ASC);
    } else if (order.getDesc() != null) {
      builder.orderBy(order.getDesc(), JPOrderDirection.DESC);
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

  private JsonUpdate toJsonUpdate(JPUpdate update, String fromClassCode, String toClassCode, BiFunction<String, String, String> attrCodeFunc) {
    return toJsonUpdate(update.getData(), fromClassCode, toClassCode, attrCodeFunc);
  }

  private JsonUpdate toJsonUpdate(JPConditionalUpdate update, String fromClassCode, String toClassCode, BiFunction<String, String, String> attrCodeFunc) {
    return toJsonUpdate(update.getData(), fromClassCode, toClassCode, attrCodeFunc);
  }

  private JsonUpdate toJsonUpdate(JPMutableData updateData,
                                  String fromClassCode,
                                  String toClassCode,
                                  BiFunction<String, String, String> attrCodeFunc) {
    JsonUpdate data = new JsonUpdate();
    data.setClassCode(toClassCode);
    Optional.ofNullable(updateData)
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
   * @param query Описание запроса
   * @return Описание обновления
   */
  public JsonUpdate toJsonUpdate(JPConditionalUpdate query) {
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

  /**
   * Создает описание обновления
   *
   * @param update           Описание запроса
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание обновления
   */
  public JsonUpdate toJsonUpdate(JPConditionalUpdate update,
                                 Function<String, String> classCodeFunc,
                                 BiFunction<String, String, String> refClassCodeFunc,
                                 BiFunction<String, String, String> attrCodeFunc) {
    if (update == null) {
      return null;
    }
    String classCode = update.getJpClass();

    JsonUpdate data = toJsonUpdate(update, classCode, classCodeFunc.apply(classCode), attrCodeFunc);
    data.setId(null);

    Optional.ofNullable(update.getWhere())
        .ifPresent(x -> data.setFilter(toExp(x, classCode, refClassCodeFunc, attrCodeFunc)));
    return data;
  }

  /**
   * Создает описание удаления
   *
   * @param delete Описание запроса
   * @return Описание обновления
   */
  public JsonConditionalDelete toJsonConditionalDelete(JPConditionalDelete delete) {
    return toJsonConditionalDelete(delete, CLASS_MAP_FUNCTION, REFCLASS_FUNCTION, ATTR_MAP_FUNCTION);
  }

  /**
   * Создает описание удаления
   *
   * @param delete           Описание запроса
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание обновления
   */
  public JsonConditionalDelete toJsonConditionalDelete(JPConditionalDelete delete,
                                                       Function<String, String> classCodeFunc,
                                                       BiFunction<String, String, String> refClassCodeFunc,
                                                       BiFunction<String, String, String> attrCodeFunc) {
    return JsonConditionalDelete.of(
        classCodeFunc.apply(delete.getJpClass()),
        delete.getWhere() != null ? toExp(delete.getWhere(), delete.getJpClass(), refClassCodeFunc, attrCodeFunc) : null
    );
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
   * @param update JPConditionalUpdate
   * @return Описание выборки
   */
  public String toString(JPConditionalUpdate update) {
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
   * @param update           JPConditionalUpdate
   * @param classCodeFunc    Логика маппинга класса
   * @param refClassCodeFunc Логика маппинга ссылочного класса
   * @param attrCodeFunc     Логика маппинга атрибута
   * @return Описание выборки
   */
  public String toString(JPConditionalUpdate update,
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

  /**
   * Создает описание запроса
   *
   * @param delete JPDelete
   * @return Описание выборки
   */
  public String toString(JPConditionalDelete delete) {
    return toString(toJsonConditionalDelete(delete));
  }

  public String toString(JPConditionalDelete delete,
                         Function<String, String> classCodeFunc,
                         BiFunction<String, String, String> refClassCodeFunc,
                         BiFunction<String, String, String> attrCodeFunc) {
    return toString(toJsonConditionalDelete(delete, classCodeFunc, refClassCodeFunc, attrCodeFunc));
  }
}
