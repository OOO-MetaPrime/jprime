package mp.jprime.parsers.stringformat;

import mp.jprime.formats.JPStringFormat;

/**
 * Приведение строки в соответствие с JPStringFormat
 */
public interface JpStringFormatParser {
  /**
   * Результат
   */
  interface Result {
    /**
     * Положительный результат обработки
     *
     * @return Да/Нет
     */
    boolean isCheck();

    /**
     * Строка, соответствующая формату
     *
     * @return Результат
     */
    String getParseValue();
  }

  /**
   * Выделяет данные по формату
   *
   * @param format Формат строки
   * @param value  Запрос
   * @return Данные по формату
   */
  Result parse(JPStringFormat format, String value);

  /**
   * Выделяет БИК
   *
   * @param value Запрос
   * @return БИК по строке
   */
  Result parseBik(String value);


  /**
   * Выделяет номер банковского счета
   *
   * @param value Запрос
   * @return номер банковской счета по строке
   */
  Result parseBankAccountNumber(String value);

  /**
   * Выделяет номер банковской карты
   *
   * @param value Запрос
   * @return номер банковской карты по строке
   */
  Result parseBankCardNumber(String value);

  /**
   * Выделяет email
   *
   * @param value Запрос
   * @return Email по строке
   */
  Result parseEmail(String value);

  /**
   * Выделяет ИНН физического лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  Result parseInn(String value);

  /**
   * Выделяет ИНН юридического лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  Result parseInn10(String value);

  /**
   * Выделяет ИНН физического или юридического  лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  Result parseInnAny(String value);

  /**
   * Выделяет КБК
   *
   * @param value Запрос
   * @return КБК по строке
   */
  Result parseKbk(String value);

  /**
   * Выделяет КПП
   *
   * @param value Запрос
   * @return КПП по строке
   */
  Result parseKpp(String value);

  /**
   * Выделяет ОГРН (20 символов)
   *
   * @param value Запрос
   * @return ОГРН (20 символов)
   */
  Result parseOgrn(String value);

  /**
   * Выделяет ОКТМО (8 символов)
   *
   * @param value Запрос
   * @return ОКТМО (8 символов)
   */
  Result parseOktmo(String value);

  /**
   * Выделяет ОКТМО (11 символов)
   *
   * @param value Запрос
   * @return ОКТМО (11 символов)
   */
  Result parseOktmo11(String value);

  /**
   * Выделяет телефон
   *
   * @param value Запрос
   * @return Телефон по строке
   */
  Result parsePhone(String value);

  /**
   * Выделяет СНИЛС
   *
   * @param value Запрос
   * @return СНИЛС по строке
   */
  Result parseSnils(String value);

  /**
   * Выделяет единый регистрационный номер
   *
   * @param value Запрос
   * @return Единый регистрационный номер по строке
   */
  Result parseErn(String value);

  /**
   * Возвращает строку с заглавной буквы
   *
   * @param value Запрос
   * @return Строка с заглавной буквы
   */
  Result parseFio(String value);

  /**
   * Выделяет номер записи акта гр.состояния
   *
   * @param value Запрос
   * @return Номер записи акта гр.состояния
   */
  Result parseZagsAgs(String value);
}
