package mp.jprime.dataaccess.params.query;

import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;
import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.filters.value.*;
import mp.jprime.dataaccess.params.query.filters.value.array.OverlapsArray;
import mp.jprime.dataaccess.params.query.filters.value.range.*;
import mp.jprime.lang.JPArray;
import mp.jprime.lang.JPRange;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

public final class CustomValueBuilder {
  private final Object customValue;

  /**
   * Значение
   *
   * @param customValue Значение
   */
  private CustomValueBuilder(Object customValue) {
    this.customValue = customValue;
  }

  /**
   * Создаем  AttrValueBuilder
   *
   * @param customValue Значение
   * @return AttrValueBuilder
   */
  public static CustomValueBuilder of(Object customValue) {
    return new CustomValueBuilder(customValue);
  }

  /**
   * Значение не указано
   *
   * @return Условие
   */
  public Null isNull() {
    return new Null(customValue);
  }

  /**
   * Значение указано
   *
   * @return Условие
   */
  public NotNull isNotNull() {
    return new NotNull(customValue);
  }

  /**
   * Равно
   *
   * @param value Значение
   * @return Условие
   */
  public EQ eq(Object value) {
    return new EQ(customValue, value);
  }

  /**
   * Мягкое равно строк (без учета регистра, например)
   *
   * @param value Значение
   * @return Условие
   */
  public SoftEQ softEq(String value) {
    return new SoftEQ(customValue, value);
  }

  /**
   * Строгое равенство строки
   *
   * @param value Значение
   * @return Условие
   */
  public StrictEQ strictEq(String value) {
    return new StrictEQ(customValue, value);
  }

  /**
   * Не равно
   *
   * @param value Значение
   * @return Условие
   */
  public NEQ neq(Object value) {
    return new NEQ(customValue, value);
  }

  /**
   * Больше или равно
   *
   * @param value Значение
   * @return Условие
   */
  public GTE gte(Object value) {
    return new GTE(customValue, value);
  }

  /**
   * Больше
   *
   * @param value Значение
   * @return Условие
   */
  public GT gt(Object value) {
    return new GT(customValue, value);
  }

  /**
   * Меньше или равно
   *
   * @param value Значение
   * @return Условие
   */
  public LTE lte(Object value) {
    return new LTE(customValue, value);
  }

  /**
   * Меньше
   *
   * @param value Значение
   * @return Условие
   */
  public LT lt(Object value) {
    return new LT(customValue, value);
  }

  /**
   * Равно в днях
   *
   * @param value Значение
   * @return Условие
   */
  public EQDay eqDay(LocalDate value) {
    return new EQDay(customValue, value);
  }

  /**
   * Не равно в днях
   *
   * @param value Значение
   * @return Условие
   */
  public NEQDay neqDay(LocalDate value) {
    return new NEQDay(customValue, value);
  }

  /**
   * Больше или равно в днях
   *
   * @param value Значение
   * @return Условие
   */
  public GTEDay gteDay(LocalDate value) {
    return new GTEDay(customValue, value);
  }

  /**
   * Больше в днях
   *
   * @param value Значение в днях
   * @return Условие
   */
  public GTDay gtDay(LocalDate value) {
    return new GTDay(customValue, value);
  }

  /**
   * Меньше или равно в днях
   *
   * @param value Значение в днях
   * @return Условие
   */
  public LTEDay lteDay(LocalDate value) {
    return new LTEDay(customValue, value);
  }

  /**
   * Меньше в днях
   *
   * @param value Значение в днях
   * @return Условие
   */
  public LTDay ltDay(LocalDate value) {
    return new LTDay(customValue, value);
  }

  /**
   * Равно в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public EQMonth eqMonth(LocalDate value) {
    return new EQMonth(customValue, value);
  }

  /**
   * Не равно в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public NEQMonth neqMonth(LocalDate value) {
    return new NEQMonth(customValue, value);
  }

  /**
   * Больше или равно в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public GTEMonth gteMonth(LocalDate value) {
    return new GTEMonth(customValue, value);
  }

  /**
   * Больше в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public GTMonth gtMonth(LocalDate value) {
    return new GTMonth(customValue, value);
  }

  /**
   * Меньше или равно в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public LTEMonth lteMonth(LocalDate value) {
    return new LTEMonth(customValue, value);
  }

  /**
   * Меньше в месяцах
   *
   * @param value Значение
   * @return Условие
   */
  public LTMonth ltMonth(LocalDate value) {
    return new LTMonth(customValue, value);
  }

  /**
   * Равно в годах
   *
   * @param value Значение
   * @return Условие
   */
  public EQYear eqYear(Integer value) {
    return new EQYear(customValue, value);
  }

  /**
   * Не равно в годах
   *
   * @param value Значение
   * @return Условие
   */
  public NEQYear neqYear(Integer value) {
    return new NEQYear(customValue, value);
  }

  /**
   * Больше или равно в годах
   *
   * @param value Значение
   * @return Условие
   */
  public GTEYear gteYear(Integer value) {
    return new GTEYear(customValue, value);
  }

  /**
   * Больше в годах
   *
   * @param value Значение
   * @return Условие
   */
  public GTYear gtYear(Integer value) {
    return new GTYear(customValue, value);
  }

  /**
   * Меньше или равно в годах
   *
   * @param value Значение
   * @return Условие
   */
  public LTEYear lteYear(Integer value) {
    return new LTEYear(customValue, value);
  }

  /**
   * Меньше в годах
   *
   * @param value Значение
   * @return Условие
   */
  public LTYear ltYear(Integer value) {
    return new LTYear(customValue, value);
  }

  /**
   * Начинается С
   *
   * @param value Значение
   * @return Условие
   */
  public StartsWith startWith(Object value) {
    return new StartsWith(customValue, value);
  }

  /**
   * Не начинается С
   *
   * @param value Значение
   * @return Условие
   */
  public NotStartsWith notStartWith(Object value) {
    return new NotStartsWith(customValue, value);
  }

  /**
   * Содержит
   *
   * @param value Значение
   * @return Условие
   */
  public Like like(Object value) {
    return new Like(customValue, value);
  }

  /**
   * Нечеткий поиск
   *
   * @param value Значение
   * @return Условие
   */
  public FuzzyLike fuzzyLike(String value) {
    return new FuzzyLike(customValue, value);
  }

  /**
   * Нечеткий поиск с учетом порядка лексем
   *
   * @param value Значение
   * @return Условие
   */
  public FuzzyOrderLike fuzzyOrderLike(String value) {
    return new FuzzyOrderLike(customValue, value);
  }

  /**
   * Нечеткий поиск без преобразования
   *
   * @param value Значение
   * @return Условие
   */
  public FuzzyQuery fuzzyQuery(String value) {
    return new FuzzyQuery(customValue, value);
  }

  /**
   * В указанном списке
   *
   * @param value Значение
   * @return Условие
   */
  public IN in(Collection<? extends Comparable> value) {
    return new IN(customValue, value);
  }

  /**
   * В указанном списке
   *
   * @param value Значение
   * @return Условие
   */
  public IN in(Comparable... value) {
    return new IN(customValue, Arrays.asList(value));
  }

  /**
   * В указанном подзапросе
   *
   * @param value Значение
   * @return Условие
   */
  public INQuery inQuery(JPSubQuery value) {
    return new INQuery(customValue, value);
  }

  /**
   * Не в указанном списке
   *
   * @param value Значение
   * @return Условие
   */
  public NotIN notIn(Collection<? extends Comparable> value) {
    return new NotIN(customValue, value);
  }

  /**
   * Не в указанном списке
   *
   * @param value Значение
   * @return Условие
   */
  public NotIN notIn(Comparable... value) {
    return new NotIN(customValue, Arrays.asList(value));
  }

  /**
   * Не в указанном подзапросе
   *
   * @param value Значение
   * @return Условие
   */
  public NotINQuery notInQuery(JPSubQuery value) {
    return new NotINQuery(customValue, value);
  }

  /**
   * Между
   *
   * @param value Значение
   * @return Условие
   */
  public Between between(Pair value) {
    return new Between(customValue, value);
  }

  /**
   * Содержит
   *
   * @param value Значение
   * @return Условие
   */
  public ContainsKVP contains(KeyValuePair value) {
    return new ContainsKVP(customValue, value);
  }

  /**
   * Диапазон содержит диапазон
   *
   * @param value Значение
   * @return Условие
   */
  public ContainsRange containsRange(JPRange<?> value) {
    return new ContainsRange(customValue, value);
  }

  /**
   * Диапазон содержит элемент
   *
   * @param value Значение
   * @return Условие
   */
  public ContainsElement containsElement(Object value) {
    return new ContainsElement(customValue, value);
  }

  /**
   * Этот диапазон пересекается с диапазоном
   *
   * @param value Значение
   * @return Условие
   */
  public OverlapsRange overlapsRange(JPRange<?> value) {
    return new OverlapsRange(customValue, value);
  }

  /**
   * Этот массив пересекается с массивом
   *
   * @param value Значение
   * @return Условие
   */
  public OverlapsArray overlapsArray(JPArray<?> value) {
    return new OverlapsArray(customValue, value);
  }

  /**
   * Равно
   *
   * @param value Значение
   * @return Условие
   */
  public EQRange eqRange(JPRange<?> value) {
    return new EQRange(customValue, value);
  }

  /**
   * Не равно
   *
   * @param value Значение
   * @return Условие
   */
  public NEQRange neqRange(JPRange<?> value) {
    return new NEQRange(customValue, value);
  }

  /**
   * Больше или равно
   *
   * @param value Значение
   * @return Условие
   */
  public GTERange gteRange(JPRange<?> value) {
    return new GTERange(customValue, value);
  }

  /**
   * Больше
   *
   * @param value Значение
   * @return Условие
   */
  public GTRange gtRange(JPRange<?> value) {
    return new GTRange(customValue, value);
  }

  /**
   * Меньше или равно
   *
   * @param value Значение
   * @return Условие
   */
  public LTERange lteRange(JPRange<?> value) {
    return new LTERange(customValue, value);
  }

  /**
   * Меньше
   *
   * @param value Значение
   * @return Условие
   */
  public LTRange ltRange(JPRange<?> value) {
    return new LTRange(customValue, value);
  }
}
