package mp.jprime.json;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.AttrValueBuilder;
import mp.jprime.dataaccess.params.query.CustomValueBuilder;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.value.*;
import mp.jprime.dataaccess.params.query.filters.value.range.*;
import mp.jprime.json.beans.JsonBetween;
import mp.jprime.json.beans.JsonCond;
import mp.jprime.json.beans.JsonContainsKVP;

public final class JsonCondCustomValueUtils extends JsonCondBaseUtils {
  public static Filter toFilter(Object customValue, JsonCond c) {
    JsonBetween between = c.getBetween();
    JsonContainsKVP contains = c.getContains();

    CustomValueBuilder builder = Filter.value(customValue);
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

  public static Filter toAttrFilter(String attrCode, CustomValueFilter v) {
    if (attrCode == null) {
      return null;
    }
    AttrValueBuilder builder = Filter.attr(attrCode);
    if (v instanceof YearFilter yf) {
      if (yf.getOper() == FilterOperation.EQ_YEAR) {
        return builder.eqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GT_YEAR) {
        return builder.gtYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GTE_YEAR) {
        return builder.gteYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.NEQ_YEAR) {
        return builder.neqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LT_YEAR) {
        return builder.ltYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LTE_YEAR) {
        return builder.lteYear(yf.getValue());
      }
    } else if (v instanceof LocalDateDateFilter df) {
      if (df.getOper() == FilterOperation.EQ_DAY) {
        return builder.eqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_DAY) {
        return builder.gtDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_DAY) {
        return builder.gteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_DAY) {
        return builder.neqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_DAY) {
        return builder.ltDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_DAY) {
        return builder.lteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.EQ_MONTH) {
        return builder.eqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_MONTH) {
        return builder.gtMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_MONTH) {
        return builder.gteMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_MONTH) {
        return builder.neqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_MONTH) {
        return builder.ltMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_MONTH) {
        return builder.lteMonth(df.getValue());
      }
    } else {
      if (v.getOper() == FilterOperation.EQ) {
        return builder.eq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.SOFT_EQ) {
        return builder.softEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STRICT_EQ) {
        return builder.strictEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GT) {
        return builder.gt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GTE) {
        return builder.gte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ) {
        return builder.neq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LT) {
        return builder.lt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LTE) {
        return builder.lte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.IN) {
        IN inst = (IN) v;
        return builder.in(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN) {
        NotIN inst = (NotIN) v;
        return builder.notIn(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.IN_QUERY) {
        INQuery inst = (INQuery) v;
        return builder.inQuery(inst.getValue());
      } else if (v.getOper() == FilterOperation.NOT_IN_QUERY) {
        NotINQuery inst = (NotINQuery) v;
        return builder.notInQuery(inst.getValue());
      } else if (v.getOper() == FilterOperation.ISNULL) {
        return builder.isNull();
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        return builder.isNotNull();
      } else if (v.getOper() == FilterOperation.LIKE) {
        return builder.like(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_LIKE) {
        return builder.fuzzyLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_ORDER_LIKE) {
        return builder.fuzzyOrderLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STARTS_WITH) {
        return builder.startWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_STARTS_WITH) {
        return builder.notStartWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_RANGE) {
        ContainsRange inst = (ContainsRange) v;
        return builder.containsRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.OVERLAPS_RANGE) {
        OverlapsRange inst = (OverlapsRange) v;
        return builder.overlapsRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.CONTAINS_ELEMENT) {
        return builder.containsElement(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.EQ_RANGE) {
        EQRange inst = (EQRange) v;
        return builder.eqRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.GT_RANGE) {
        GTRange inst = (GTRange) v;
        return builder.gtRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.GTE_RANGE) {
        GTERange inst = (GTERange) v;
        return builder.gteRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.NEQ_RANGE) {
        NEQRange inst = (NEQRange) v;
        return builder.neqRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.LT_RANGE) {
        LTRange inst = (LTRange) v;
        return builder.ltRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.LTE_RANGE) {
        LTERange inst = (LTERange) v;
        return builder.lteRange(inst.getValue());
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        return builder.between(b.getValue());
      } else if (v.getOper() == FilterOperation.CONTAINS) {
        ContainsKVP b = (ContainsKVP) v;
        return builder.contains(b.getValue());
      }
    }
    return null;
  }

  public static JsonCond toJson(Object customValue, CustomValueFilter v) {
    JsonCond valueCond = JsonCond.newValueCond(customValue);

    JsonCond cond = null;
    if (v instanceof YearFilter yf) {
      if (yf.getOper() == FilterOperation.EQ_YEAR) {
        cond = valueCond.eqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GT_YEAR) {
        cond = valueCond.gtYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.GTE_YEAR) {
        cond = valueCond.gteYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.NEQ_YEAR) {
        cond = valueCond.neqYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LT_YEAR) {
        cond = valueCond.ltYear(yf.getValue());
      } else if (yf.getOper() == FilterOperation.LTE_YEAR) {
        cond = valueCond.lteYear(yf.getValue());
      }
    } else if (v instanceof LocalDateDateFilter df) {
      if (df.getOper() == FilterOperation.EQ_DAY) {
        cond = valueCond.eqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_DAY) {
        cond = valueCond.gtDay(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_DAY) {
        cond = valueCond.gteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_DAY) {
        cond = valueCond.neqDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_DAY) {
        cond = valueCond.ltDay(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_DAY) {
        cond = valueCond.lteDay(df.getValue());
      } else if (df.getOper() == FilterOperation.EQ_MONTH) {
        cond = valueCond.eqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GT_MONTH) {
        cond = valueCond.gtMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.GTE_MONTH) {
        cond = valueCond.gteMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.NEQ_MONTH) {
        cond = valueCond.neqMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LT_MONTH) {
        cond = valueCond.ltMonth(df.getValue());
      } else if (df.getOper() == FilterOperation.LTE_MONTH) {
        cond = valueCond.lteMonth(df.getValue());
      }
    } else {
      if (v.getOper() == FilterOperation.EQ) {
        cond = valueCond.eq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.SOFT_EQ) {
        cond = valueCond.softEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STRICT_EQ) {
        cond = valueCond.strictEq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GT) {
        cond = valueCond.gt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.GTE) {
        cond = valueCond.gte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ) {
        cond = valueCond.neq(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LT) {
        cond = valueCond.lt(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.LTE) {
        cond = valueCond.lte(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.IN) {
        IN inst = (IN) v;
        cond = valueCond.in(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN) {
        NotIN inst = (NotIN) v;
        cond = valueCond.notIn(toStrings(inst.getValue()));
      } else if (v.getOper() == FilterOperation.IN_QUERY) {
        INQuery inst = (INQuery) v;
        cond = valueCond.inQuery(toJsonSubquery(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_IN_QUERY) {
        NotINQuery inst = (NotINQuery) v;
        cond = valueCond.notInQuery(toJsonSubquery(inst.getValue()));
      } else if (v.getOper() == FilterOperation.ISNULL) {
        cond = valueCond.isNull(true);
      } else if (v.getOper() == FilterOperation.ISNOTNULL) {
        cond = valueCond.isNotNull(true);
      } else if (v.getOper() == FilterOperation.LIKE) {
        cond = valueCond.like(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_LIKE) {
        cond = valueCond.fuzzyLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.FUZZY_ORDER_LIKE) {
        cond = valueCond.fuzzyOrderLike(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.STARTS_WITH) {
        cond = valueCond.startsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.NOT_STARTS_WITH) {
        cond = valueCond.notStartsWith(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_RANGE) {
        ContainsRange inst = (ContainsRange) v;
        cond = valueCond.containsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.OVERLAPS_RANGE) {
        OverlapsRange inst = (OverlapsRange) v;
        cond = valueCond.overlapsRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.CONTAINS_ELEMENT) {
        cond = valueCond.containsElement(stringValue(v.getValue()));
      } else if (v.getOper() == FilterOperation.EQ_RANGE) {
        EQRange inst = (EQRange) v;
        cond = valueCond.eqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GT_RANGE) {
        GTRange inst = (GTRange) v;
        cond = valueCond.gtRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.GTE_RANGE) {
        GTERange inst = (GTERange) v;
        cond = valueCond.gteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.NEQ_RANGE) {
        NEQRange inst = (NEQRange) v;
        cond = valueCond.neqRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LT_RANGE) {
        LTRange inst = (LTRange) v;
        cond = valueCond.ltRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.LTE_RANGE) {
        LTERange inst = (LTERange) v;
        cond = valueCond.lteRange(toJsonRange(inst.getValue()));
      } else if (v.getOper() == FilterOperation.BETWEEN) {
        Between b = (Between) v;
        Pair pair = b.getValue();
        cond = valueCond.between(new JsonBetween(stringValue(pair.getFrom()), stringValue(pair.getTo())));
      } else if (v.getOper() == FilterOperation.CONTAINS) {
        ContainsKVP b = (ContainsKVP) v;
        KeyValuePair entry = b.getValue();
        cond = valueCond.contains(JsonContainsKVP.from(stringValue(entry.getKey()), stringValue(entry.getValue())));
      }
    }
    return cond;
  }
}
