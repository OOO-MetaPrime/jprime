package mp.jprime.json;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.AttrValueBuilder;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.attr.*;
import mp.jprime.dataaccess.params.query.filters.attr.range.*;
import mp.jprime.json.beans.JsonBetween;
import mp.jprime.json.beans.JsonCond;
import mp.jprime.json.beans.JsonContainsKVP;
import mp.jprime.json.beans.JsonExpr;

public final class JsonCondAttrValueUtils extends JsonCondBaseUtils {
  public static Filter toFilter(String attrCode, JsonCond c) {
    if (attrCode == null) {
      return null;
    }
    JsonBetween between = c.getBetween();
    JsonContainsKVP contains = c.getContains();

    AttrValueBuilder builder = Filter.attr(attrCode);
    // TODO сделать красиво
    if (c.getEq() != null) {
      return builder.eq(c.getEq());
    } else if (c.getSoftEq() != null) {
      return builder.softEq(c.getSoftEq());
    } else if (c.getStrictEq() != null) {
      return builder.strictEq(c.getStrictEq());
    } else if (c.getGt() != null) {
      return builder.gt(c.getGt());
    } else if (c.getGte() != null) {
      return builder.gte(c.getGte());
    } else if (c.getNeq() != null) {
      return builder.neq(c.getNeq());
    } else if (c.getLt() != null) {
      return builder.lt(c.getLt());
    } else if (c.getLte() != null) {
      return builder.lte(c.getLte());
    } else if (c.getIn() != null) {
      return builder.in(c.getIn());
    } else if (c.getNotIn() != null) {
      return builder.notIn(c.getNotIn());
    } else if (c.getInQuery() != null) {
      return builder.inQuery(toSubquery(c.getInQuery()));
    } else if (c.getNotInQuery() != null) {
      return builder.notInQuery(toSubquery(c.getNotInQuery()));
    } else if (c.getIsNotNull() != null && c.getIsNotNull()) {
      return builder.isNotNull();
    } else if (c.getIsNull() != null && c.getIsNull()) {
      return builder.isNull();
    } else if (c.getLike() != null) {
      return builder.like(c.getLike());
    } else if (c.getFuzzyLike() != null) {
      return builder.fuzzyLike(c.getFuzzyLike());
    } else if (c.getFuzzyOrderLike() != null) {
      return builder.fuzzyOrderLike(c.getFuzzyOrderLike());
    } else if (c.getStartsWith() != null) {
      return builder.startWith(c.getStartsWith());
    } else if (c.getNotStartsWith() != null) {
      return builder.notStartWith(c.getNotStartsWith());
    } else if (between != null) {
      return builder.between(Pair.from(between.getFrom(), between.getTo()));
    } else if (contains != null) {
      return builder.contains(KeyValuePair.from(contains.getKey(), contains.getValue()));
    } else if (c.getExists() != null) {
      return builder.exists(JsonExpr.toFilter(c.getExists()));
    } else if (c.getNotExists() != null) {
      return builder.notExists(JsonExpr.toFilter(c.getNotExists()));
    } else if (c.getEqYear() != null) {
      return builder.eqYear(c.getEqYear());
    } else if (c.getNeqYear() != null) {
      return builder.neqYear(c.getNeqYear());
    } else if (c.getLtYear() != null) {
      return builder.ltYear(c.getLtYear());
    } else if (c.getLteYear() != null) {
      return builder.lteYear(c.getLteYear());
    } else if (c.getGtYear() != null) {
      return builder.gtYear(c.getGtYear());
    } else if (c.getGteYear() != null) {
      return builder.gteYear(c.getGteYear());
    } else if (c.getEqMonth() != null) {
      return builder.eqMonth(c.getEqMonth());
    } else if (c.getNeqMonth() != null) {
      return builder.neqMonth(c.getNeqMonth());
    } else if (c.getLtMonth() != null) {
      return builder.ltMonth(c.getLtMonth());
    } else if (c.getLteMonth() != null) {
      return builder.lteMonth(c.getLteMonth());
    } else if (c.getGtMonth() != null) {
      return builder.gtMonth(c.getGtMonth());
    } else if (c.getGteMonth() != null) {
      return builder.gteMonth(c.getGteMonth());
    } else if (c.getEqDay() != null) {
      return builder.eqDay(c.getEqDay());
    } else if (c.getNeqDay() != null) {
      return builder.neqDay(c.getNeqDay());
    } else if (c.getLtDay() != null) {
      return builder.ltDay(c.getLtDay());
    } else if (c.getLteDay() != null) {
      return builder.lteDay(c.getLteDay());
    } else if (c.getGtDay() != null) {
      return builder.gtDay(c.getGtDay());
    } else if (c.getGteDay() != null) {
      return builder.gteDay(c.getGteDay());
    } else if (c.getContainsElement() != null) {
      return builder.containsElement(c.getContainsElement());
    } else if (c.getContainsRange() != null) {
      return builder.containsRange(toStringRange(c.getContainsRange()));
    } else if (c.getOverlapsRange() != null) {
      return builder.overlapsRange(toStringRange(c.getOverlapsRange()));
    } else if (c.getEqRange() != null) {
      return builder.eqRange(toStringRange(c.getEqRange()));
    } else if (c.getGtRange() != null) {
      return builder.gtRange(toStringRange(c.getGtRange()));
    } else if (c.getGteRange() != null) {
      return builder.gteRange(toStringRange(c.getGteRange()));
    } else if (c.getNeqRange() != null) {
      return builder.neqRange(toStringRange(c.getNeqRange()));
    } else if (c.getLtRange() != null) {
      return builder.ltRange(toStringRange(c.getLtRange()));
    } else if (c.getLteRange() != null) {
      return builder.lteRange(toStringRange(c.getLteRange()));
    }
    return null;
  }

  public static JsonCond toJson(String attrName, AttrValueFilter v) {
    JsonCond attrCond = JsonCond.newAttrCond(attrName);

    JsonCond cond = null;
    if (v instanceof mp.jprime.dataaccess.params.query.filters.attr.YearFilter yf) {
      if (yf.getOper() == FilterOperation.EQ_YEAR) {
        cond = attrCond.eqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GT_YEAR) {
        cond = attrCond.gtYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GTE_YEAR) {
        cond = attrCond.gteYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.NEQ_YEAR) {
        cond = attrCond.neqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LT_YEAR) {
        cond = attrCond.ltYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LTE_YEAR) {
        cond = attrCond.lteYear(yf.getValue());
      }
    } else if (v instanceof mp.jprime.dataaccess.params.query.filters.attr.LocalDateDateFilter df) {
      if (df.getOper() == FilterOperation.EQ_DAY) {
        cond = attrCond.eqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_DAY) {
        cond = attrCond.gtDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_DAY) {
        cond = attrCond.gteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_DAY) {
        cond = attrCond.neqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_DAY) {
        cond = attrCond.ltDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_DAY) {
        cond = attrCond.lteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.EQ_MONTH) {
        cond = attrCond.eqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_MONTH) {
        cond = attrCond.gtMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_MONTH) {
        cond = attrCond.gteMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_MONTH) {
        cond = attrCond.neqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_MONTH) {
        cond = attrCond.ltMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_MONTH) {
        cond = attrCond.lteMonth(df.getValue());
      }
    } else {
      if (v.getOper() == FilterOperation.EQ) {
        cond = attrCond.eq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.SOFT_EQ) {
        cond = attrCond.softEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STRICT_EQ) {
        cond = attrCond.strictEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GT) {
        cond = attrCond.gt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GTE) {
        cond = attrCond.gte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ) {
        cond = attrCond.neq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LT) {
        cond = attrCond.lt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LTE) {
        cond = attrCond.lte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.IN) {
        IN inst = (IN) v;
        cond = attrCond.in(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN) {
        NotIN inst = (NotIN) v;
        cond = attrCond.notIn(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.IN_QUERY) {
        INQuery inst = (INQuery) v;
        cond = attrCond.inQuery(toJsonSubquery(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN_QUERY) {
        NotINQuery inst = (NotINQuery) v;
        cond = attrCond.notInQuery(toJsonSubquery(inst.getValue()));
      } else if (v.getOper() == FilterOperation.ISNULL) {
        cond = attrCond.isNull(true);
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        cond = attrCond.isNotNull(true);
      } else if (v.getOper() == FilterOperation.LIKE) {
        cond = attrCond.like(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_LIKE) {
        cond = attrCond.fuzzyLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_ORDER_LIKE) {
        cond = attrCond.fuzzyOrderLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STARTS_WITH) {
        cond = attrCond.startsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_STARTS_WITH) {
        cond = attrCond.notStartsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_RANGE) {
        ContainsRange inst = (ContainsRange) v;
        cond = attrCond.containsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.OVERLAPS_RANGE) {
        OverlapsRange inst = (OverlapsRange) v;
        cond = attrCond.overlapsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_ELEMENT) {
        cond = attrCond.containsElement(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.EQ_RANGE) {
        EQRange inst = (EQRange) v;
        cond = attrCond.eqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GT_RANGE) {
        GTRange inst = (GTRange) v;
        cond = attrCond.gtRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GTE_RANGE) {
        GTERange inst = (GTERange) v;
        cond = attrCond.gteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ_RANGE) {
        NEQRange inst = (NEQRange) v;
        cond = attrCond.neqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LT_RANGE) {
        LTRange inst = (LTRange) v;
        cond = attrCond.ltRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LTE_RANGE) {
        LTERange inst = (LTERange) v;
        cond = attrCond.lteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        Pair pair = b.getValue();
        cond = attrCond.between(new JsonBetween(stringValue(pair.getFrom()), stringValue(pair.getTo())));
      } else if (v.getOper() == FilterOperation.CONTAINS) {
        ContainsKVP b = (ContainsKVP) v;
        KeyValuePair entry = b.getValue();
        cond = attrCond.contains(JsonContainsKVP.from(stringValue(entry.getKey()), stringValue(entry.getValue())));
      }
    }
    return cond;
  }
}
