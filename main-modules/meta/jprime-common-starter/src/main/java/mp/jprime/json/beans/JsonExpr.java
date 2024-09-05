package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.dataaccess.enums.AnalyticFunction;
import mp.jprime.dataaccess.enums.BooleanCondition;
import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.dataaccess.params.query.filters.range.*;
import mp.jprime.lang.JPRange;
import mp.jprime.lang.JPStringRange;
import mp.jprime.utils.ParseServiceUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonExpr {
  private JsonCond cond;
  private Collection<JsonExpr> or;
  private Collection<JsonExpr> and;

  public JsonExpr() {

  }

  public JsonExpr(JsonCond cond) {
    this.cond = cond;
  }

  public JsonCond getCond() {
    return cond;
  }

  public Collection<JsonExpr> getOr() {
    return or;
  }

  public Collection<JsonExpr> getAnd() {
    return and;
  }

  public JsonExpr or(Collection<JsonExpr> or) {
    this.or = or;
    return this;
  }

  public JsonExpr and(Collection<JsonExpr> and) {
    this.and = and;
    return this;
  }

  /**
   * Учет условия
   *
   * @param exp Условие
   * @return Filter Условие
   */
  public static Filter toFilter(JsonExpr exp) {
    if (exp == null) {
      return null;
    }
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
      return Filter.and(and.stream()
          .map(JsonExpr::toFilter)
          .filter(Objects::nonNull)
          .collect(Collectors.toList())
      );
    } else if (or != null && !or.isEmpty()) {
      return Filter.or(or.stream()
          .map(JsonExpr::toFilter)
          .filter(Objects::nonNull)
          .collect(Collectors.toList())
      );
    }
    return null;
  }

  private static Collection<JsonExpr> clear(Collection<JsonExpr> list) {
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
   * JsonRange to JPStringRange
   *
   * @param v JsonRange
   * @return JPStringRange
   */
  private static JPStringRange toStringRange(JsonStringRange v) {
    return v != null ? JPStringRange.closed(v.getLower(), v.getUpper()) : null;
  }

  /**
   * Учет условия
   *
   * @param filter Условие
   * @return Expr Условие
   */
  public static JsonExpr toJson(Filter filter) {
    return toJson(filter, null, (classCode, refAttrCode) -> null, (classCode, attrCode) -> attrCode);
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
  public static JsonExpr toJson(Filter filter,
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
                .map(f -> toJson(f, classCode, refClassCodeFunc, attrCodeFunc))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
      } else if (c.getCond() == BooleanCondition.OR) {
        return new JsonExpr().or(
            c.getFilters().stream()
                .map(f -> toJson(f, classCode, refClassCodeFunc, attrCodeFunc))
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
      } else if (v.getOper() == FilterOperation.OVERLAPS_RANGE) {
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
            toJson(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      } else if (l.getFunction() == AnalyticFunction.NOTEXISTS) {
        cond = JsonCond.newAttrCond(attrName).notExists(
            toJson(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      }
    }
    return cond != null ? new JsonExpr(cond) : null;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private static JsonStringRange toJsonRange(JPRange<?> v) {
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
  private static Collection<String> toStrings(Collection<? extends Comparable> v) {
    return v.stream()
        .map(JsonExpr::stringValue)
        .collect(Collectors.toList());
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  private static String stringValue(Object v) {
    return ParseServiceUtils.toString(v);
  }
}
