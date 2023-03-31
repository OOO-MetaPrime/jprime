package mp.jprime.dataaccess.params.query;

import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.*;
import mp.jprime.dataaccess.params.query.filters.range.*;
import mp.jprime.lang.JPRange;
import mp.jprime.meta.JPAttr;

import java.time.LocalDate;
import java.util.Arrays;
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
   * Построитель условия по значению
   *
   * @param attr Атрибут
   * @return Условие по значению
   */
  public static ValueBuilder attr(JPAttr attr) {
    return new ValueBuilder(attr.getCode());
  }

  /**
   * Построитель условия по признаку
   *
   * @param featureCode Кодовое имя признака
   * @return Условие по значению
   */
  public static FeatureBuilder feature(String featureCode) {
    return new FeatureBuilder(featureCode);
  }

  /**
   * Построитель условия по признаку
   */
  public static final class FeatureBuilder {
    private final String featureCode; // Кодовое имя признака

    /**
     * Кодовое имя признака
     *
     * @param featureCode Кодовое имя признака
     */
    private FeatureBuilder(String featureCode) {
      this.featureCode = featureCode;
    }

    /**
     * Проверка признака
     *
     * @return Условие
     */
    public FeatureFilter check() {
      return new FeatureFilter(featureCode);
    }

    /**
     * Проверка признака
     *
     * @param day День, на которую проверяем
     * @return Условие
     */
    public DayFeatureFilter check(LocalDate day) {
      return new DayFeatureFilter(featureCode, day);
    }

    /**
     * Проверка признака
     *
     * @param fromDay Дата начала периода проверки признака
     * @param toDay   Дата окончания периода проверки признака
     * @return Условие
     */
    public PeriodFeatureFilter check(LocalDate fromDay, LocalDate toDay) {
      return new PeriodFeatureFilter(featureCode, fromDay, toDay);
    }
  }

  /**
   * Построитель условия по значению
   *
   * @param attrCode Кодовое имя атрибута
   * @return Условие по значению
   */
  public static ValueBuilder attr(String attrCode) {
    return new ValueBuilder(attrCode);
  }

  /**
   * Построитель условия
   */
  public static final class ValueBuilder {
    private final String attrCode; // Кодовое имя атрибута

    /**
     * Кодовое имя атрибута
     *
     * @param attrCode Кодовое имя атрибута
     */
    private ValueBuilder(String attrCode) {
      this.attrCode = attrCode;
    }

    /**
     * Значение не указано
     *
     * @return Условие
     */
    public Null isNull() {
      return new Null(attrCode);
    }

    /**
     * Значение указано
     *
     * @return Условие
     */
    public NotNull isNotNull() {
      return new NotNull(attrCode);
    }

    /**
     * Равно
     *
     * @param value Значение
     * @return Условие
     */
    public EQ eq(Object value) {
      return new EQ(attrCode, value);
    }

    /**
     * Не равно
     *
     * @param value Значение
     * @return Условие
     */
    public NEQ neq(Object value) {
      return new NEQ(attrCode, value);
    }

    /**
     * Больше или равно
     *
     * @param value Значение
     * @return Условие
     */
    public GTE gte(Object value) {
      return new GTE(attrCode, value);
    }

    /**
     * Больше
     *
     * @param value Значение
     * @return Условие
     */
    public GT gt(Object value) {
      return new GT(attrCode, value);
    }

    /**
     * Меньше или равно
     *
     * @param value Значение
     * @return Условие
     */
    public LTE lte(Object value) {
      return new LTE(attrCode, value);
    }

    /**
     * Меньше
     *
     * @param value Значение
     * @return Условие
     */
    public LT lt(Object value) {
      return new LT(attrCode, value);
    }

    /**
     * Равно в днях
     *
     * @param value Значение
     * @return Условие
     */
    public EQDay eqDay(LocalDate value) {
      return new EQDay(attrCode, value);
    }

    /**
     * Не равно в днях
     *
     * @param value Значение
     * @return Условие
     */
    public NEQDay neqDay(LocalDate value) {
      return new NEQDay(attrCode, value);
    }

    /**
     * Больше или равно в днях
     *
     * @param value Значение
     * @return Условие
     */
    public GTEDay gteDay(LocalDate value) {
      return new GTEDay(attrCode, value);
    }

    /**
     * Больше в днях
     *
     * @param value Значение в днях
     * @return Условие
     */
    public GTDay gtDay(LocalDate value) {
      return new GTDay(attrCode, value);
    }

    /**
     * Меньше или равно в днях
     *
     * @param value Значение в днях
     * @return Условие
     */
    public LTEDay lteDay(LocalDate value) {
      return new LTEDay(attrCode, value);
    }

    /**
     * Меньше в днях
     *
     * @param value Значение в днях
     * @return Условие
     */
    public LTDay ltDay(LocalDate value) {
      return new LTDay(attrCode, value);
    }

    /**
     * Равно в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public EQMonth eqMonth(LocalDate value) {
      return new EQMonth(attrCode, value);
    }

    /**
     * Не равно в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public NEQMonth neqMonth(LocalDate value) {
      return new NEQMonth(attrCode, value);
    }

    /**
     * Больше или равно в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public GTEMonth gteMonth(LocalDate value) {
      return new GTEMonth(attrCode, value);
    }

    /**
     * Больше в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public GTMonth gtMonth(LocalDate value) {
      return new GTMonth(attrCode, value);
    }

    /**
     * Меньше или равно в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public LTEMonth lteMonth(LocalDate value) {
      return new LTEMonth(attrCode, value);
    }

    /**
     * Меньше в месяцах
     *
     * @param value Значение
     * @return Условие
     */
    public LTMonth ltMonth(LocalDate value) {
      return new LTMonth(attrCode, value);
    }

    /**
     * Равно в годах
     *
     * @param value Значение
     * @return Условие
     */
    public EQYear eqYear(Integer value) {
      return new EQYear(attrCode, value);
    }

    /**
     * Не равно в годах
     *
     * @param value Значение
     * @return Условие
     */
    public NEQYear neqYear(Integer value) {
      return new NEQYear(attrCode, value);
    }

    /**
     * Больше или равно в годах
     *
     * @param value Значение
     * @return Условие
     */
    public GTEYear gteYear(Integer value) {
      return new GTEYear(attrCode, value);
    }

    /**
     * Больше в годах
     *
     * @param value Значение
     * @return Условие
     */
    public GTYear gtYear(Integer value) {
      return new GTYear(attrCode, value);
    }

    /**
     * Меньше или равно в годах
     *
     * @param value Значение
     * @return Условие
     */
    public LTEYear lteYear(Integer value) {
      return new LTEYear(attrCode, value);
    }

    /**
     * Меньше в годах
     *
     * @param value Значение
     * @return Условие
     */
    public LTYear ltYear(Integer value) {
      return new LTYear(attrCode, value);
    }

    /**
     * Начинается С
     *
     * @param value Значение
     * @return Условие
     */
    public StartsWith startWith(Object value) {
      return new StartsWith(attrCode, value);
    }

    /**
     * Не начинается С
     *
     * @param value Значение
     * @return Условие
     */
    public NotStartsWith notStartWith(Object value) {
      return new NotStartsWith(attrCode, value);
    }

    /**
     * Существует
     *
     * @param value Значение внутренних объектов
     * @return Условие
     */
    public Exists exists(Filter value) {
      return new Exists(attrCode, value);
    }

    /**
     * Не существует
     *
     * @param value Значение  внутренних объектов
     * @return Условие
     */
    public NotExists notExists(Filter value) {
      return new NotExists(attrCode, value);
    }

    /**
     * Содержит
     *
     * @param value Значение
     * @return Условие
     */
    public Like like(Object value) {
      return new Like(attrCode, value);
    }

    /**
     * Нечеткий поиск
     *
     * @param value Значение
     * @return Условие
     */
    public FuzzyLike fuzzyLike(String value) {
      return new FuzzyLike(attrCode, value);
    }

    /**
     * Нечеткий поиск с учетом порядка лексем
     *
     * @param value Значение
     * @return Условие
     */
    public FuzzyOrderLike fuzzyOrderLike(String value) {
      return new FuzzyOrderLike(attrCode, value);
    }

    /**
     * Нечеткий поиск без преобразования
     *
     * @param value Значение
     * @return Условие
     */
    public FuzzyQuery fuzzyQuery(String value) {
      return new FuzzyQuery(attrCode, value);
    }

    /**
     * В указанном списке
     *
     * @param value Значение
     * @return Условие
     */
    public IN in(Collection<? extends Comparable> value) {
      return new IN(attrCode, value);
    }

    /**
     * В указанном списке
     *
     * @param value Значение
     * @return Условие
     */
    public IN in(Comparable... value) {
      return new IN(attrCode, Arrays.asList(value));
    }

    /**
     * В указанном списке
     *
     * @param value Значение
     * @return Условие
     */
    public NotIN notIn(Collection<? extends Comparable> value) {
      return new NotIN(attrCode, value);
    }

    /**
     * В указанном списке
     *
     * @param value Значение
     * @return Условие
     */
    public NotIN notIn(Comparable... value) {
      return new NotIN(attrCode, Arrays.asList(value));
    }

    /**
     * Между
     *
     * @param value Значение
     * @return Условие
     */
    public Between between(Pair value) {
      return new Between(attrCode, value);
    }

    /**
     * Содержит
     *
     * @param value Значение
     * @return Условие
     */
    public ContainsKVP contains(KeyValuePair value) {
      return new ContainsKVP(attrCode, value);
    }

    /**
     * Диапазон содержит диапазон
     *
     * @param value Значение
     * @return Условие
     */
    public ContainsRange containsRange(JPRange<?> value) {
      return new ContainsRange(attrCode, value);
    }

    /**
     * Диапазон содержит элемент
     *
     * @param value Значение
     * @return Условие
     */
    public ContainsElement containsElement(Object value) {
      return new ContainsElement(attrCode, value);
    }

    /**
     * Этот диапазон содержится в диапазон
     *
     * @param value Значение
     * @return Условие
     */
    public OverlapsRange overlapsRange(JPRange<?> value) {
      return new OverlapsRange(attrCode, value);
    }

    /**
     * Равно
     *
     * @param value Значение
     * @return Условие
     */
    public EQRange eqRange(JPRange<?> value) {
      return new EQRange(attrCode, value);
    }

    /**
     * Не равно
     *
     * @param value Значение
     * @return Условие
     */
    public NEQRange neqRange(JPRange<?> value) {
      return new NEQRange(attrCode, value);
    }

    /**
     * Больше или равно
     *
     * @param value Значение
     * @return Условие
     */
    public GTERange gteRange(JPRange<?> value) {
      return new GTERange(attrCode, value);
    }

    /**
     * Больше
     *
     * @param value Значение
     * @return Условие
     */
    public GTRange gtRange(JPRange<?> value) {
      return new GTRange(attrCode, value);
    }

    /**
     * Меньше или равно
     *
     * @param value Значение
     * @return Условие
     */
    public LTERange lteRange(JPRange<?> value) {
      return new LTERange(attrCode, value);
    }

    /**
     * Меньше
     *
     * @param value Значение
     * @return Условие
     */
    public LTRange ltRange(JPRange<?> value) {
      return new LTRange(attrCode, value);
    }
  }
}
