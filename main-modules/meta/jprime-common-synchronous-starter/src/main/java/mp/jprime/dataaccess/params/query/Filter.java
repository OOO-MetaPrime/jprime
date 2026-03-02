package mp.jprime.dataaccess.params.query;

import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.meta.JPAttr;

import java.util.Collection;

/**
 * Условие
 */
public abstract class Filter {
  /**
   * Построитель логического условия ИЛИ
   *
   * @param filters Условия
   * @return условия ИЛИ
   */
  public static Or or(Filter... filters) {
    return new Or(filters);
  }

  /**
   * Построитель логического условия И
   *
   * @param filters Условия
   * @return условия И
   */
  public static And and(Filter... filters) {
    return new And(filters);
  }

  /**
   * Построитель логического условия ИЛИ
   *
   * @param filters Условия
   * @return условия ИЛИ
   */
  public static Or or(Collection<Filter> filters) {
    return new Or(filters);
  }

  /**
   * Построитель логического условия И
   *
   * @param filters Условия
   * @return условия И
   */
  public static And and(Collection<Filter> filters) {
    return new And(filters);
  }
  /**
   * Построитель условия по признаку
   *
   * @param featureCode Кодовое имя признака
   * @return Условие по значению
   */
  public static FeatureBuilder feature(String featureCode) {
    return FeatureBuilder.of(featureCode);
  }

  /**
   * Построитель условия по значению
   *
   * @param attr Атрибут
   * @return Условие по значению
   */
  public static AttrValueBuilder attr(JPAttr attr) {
    return attr(attr.getCode());
  }

  /**
   * Построитель условия по значению
   *
   * @param attrCode Кодовое имя атрибута
   * @return Условие по значению
   */
  public static AttrValueBuilder attr(String attrCode) {
    return AttrValueBuilder.of(attrCode);
  }

  /**
   * Построитель условия по значению
   *
   * @param customValue Значение
   * @return Условие по значению
   */
  public static CustomValueBuilder value(Object customValue) {
    return CustomValueBuilder.of(customValue);
  }
}
