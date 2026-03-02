package mp.jprime.json;

import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.json.beans.JsonExpr;
import mp.jprime.json.beans.JsonStringRange;
import mp.jprime.json.beans.JsonSubquery;
import mp.jprime.lang.JPRange;
import mp.jprime.lang.JPStringRange;
import mp.jprime.parsers.ValueParser;

import java.util.Collection;
import java.util.stream.Collectors;

abstract class JsonCondBaseUtils {
  /**
   * JsonSubquery to JPSubQuery
   *
   * @param v JsonSubquery
   * @return JPSubQuery
   */
  protected static JPSubQuery toSubquery(JsonSubquery v) {
    if (v == null) {
      return JPSubQuery.of(null, null, null);
    }
    return JPSubQuery.of(v.getAttr(), v.getClassCode(), JsonExpr.toFilter(v.getFilter()));
  }

  /**
   * JsonRange to JPStringRange
   *
   * @param v JsonRange
   * @return JPStringRange
   */
  protected static JPStringRange toStringRange(JsonStringRange v) {
    return v != null ? JPStringRange.create(v.getLower(), v.getUpper()) : null;
  }

  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  protected static JsonStringRange toJsonRange(JPRange<?> v) {
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
  protected static Collection<String> toStrings(Collection<? extends Comparable> v) {
    return v.stream()
        .map(JsonCondBaseUtils::stringValue)
        .collect(Collectors.toList());
  }


  /**
   * Object to String
   *
   * @param v Object
   * @return String
   */
  protected static String stringValue(Object v) {
    return ValueParser.toString(v);
  }

  /**
   * JPSubQuery to JsonSubquery
   *
   * @param v JPSubQuery
   * @return JsonSubquery
   */
  protected static JsonSubquery toJsonSubquery(JPSubQuery v) {
    JsonSubquery result = new JsonSubquery();
    if (v == null) {
      return result;
    }
    result.attr(v.getAttr());
    result.classCode(v.getClassCode());
    result.filter(JsonExpr.toJson(v.getFilter()));
    return result;
  }
}
