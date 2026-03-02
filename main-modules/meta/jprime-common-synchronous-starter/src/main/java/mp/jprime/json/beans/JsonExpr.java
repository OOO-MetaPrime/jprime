package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.dataaccess.enums.AnalyticFunction;
import mp.jprime.dataaccess.enums.BooleanCondition;
import mp.jprime.dataaccess.params.query.FeatureBuilder;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.dataaccess.params.query.filters.attr.*;
import mp.jprime.dataaccess.params.query.filters.value.*;
import mp.jprime.json.JsonCondAttrValueUtils;
import mp.jprime.json.JsonCondCustomValueUtils;

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
      if (c.getAttr() != null) {
        return JsonCondAttrValueUtils.toFilter(c.getAttr(), c);
      } else if (c.getFeature() != null) {
        FeatureBuilder builder = Filter.feature(c.getFeature());
        if (c.getCheckDay() != null) {
          return builder.check(c.getCheckDay());
        } else if (c.getCheckFromDay() != null && c.getCheckToDay() != null) {
          return builder.check(c.getCheckFromDay(), c.getCheckToDay());
        } else {
          return builder.check();
        }
      } else {
        return JsonCondCustomValueUtils.toFilter(c.getValue(), c);
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
    if (filter instanceof BooleanFilter c) {
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
    if (filter instanceof PeriodFeatureFilter v) {
      cond = JsonCond.newFeatureCond(v.getFeatureCode()).checkFromDay(v.getCheckFromDay()).checkToDay(v.getCheckToDay());
    } else if (filter instanceof DayFeatureFilter v) {
      cond = JsonCond.newFeatureCond(v.getFeatureCode()).checkDay(v.getCheckDay());
    } else if (filter instanceof FeatureFilter v) {
      cond = JsonCond.newFeatureCond(v.getFeatureCode());
    } else if (filter instanceof CustomValueFilter v) {
      cond = JsonCondCustomValueUtils.toJson(v.getCustomValue(), v);
    } else if (filter instanceof AttrValueFilter v) {
      String attrName = attrCodeFunc.apply(classCode, v.getAttrCode());
      cond = JsonCondAttrValueUtils.toJson(attrName, v);
    } else if (filter instanceof LinkFilter l) {
      String attrName = attrCodeFunc.apply(classCode, l.getAttrCode());
      String refClassCode = refClassCodeFunc.apply(classCode, l.getAttrCode());
      JsonCond attrCond = JsonCond.newAttrCond(attrName);

      if (l.getFunction() == AnalyticFunction.EXISTS) {
        cond = attrCond.exists(
            toJson(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      } else if (l.getFunction() == AnalyticFunction.NOTEXISTS) {
        cond = attrCond.notExists(
            toJson(l.getFilter(), refClassCode, refClassCodeFunc, attrCodeFunc)
        );
      }
    }
    return cond != null ? new JsonExpr(cond) : null;
  }
}
