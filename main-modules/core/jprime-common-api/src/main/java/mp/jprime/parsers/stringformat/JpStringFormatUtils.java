package mp.jprime.parsers.stringformat;


import mp.jprime.formats.JPStringFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Разбор данных по строке
 */
@Service
public final class JpStringFormatUtils {
  private static final JpStringFormatParser.Result EMPTY = new JpStringFormatParser.Result() {
    @Override
    public boolean isCheck() {
      return true;
    }

    @Override
    public String getParseValue() {
      return null;
    }
  };

  private static JpStringFormatParser SERVICE;

  private JpStringFormatUtils(@Autowired JpStringFormatParser parser) {
    SERVICE = parser;
  }

  /**
   * Разбирает значение
   *
   * @param value Запрос
   * @return Значение
   */
  public static JpStringFormatParser.Result parse(JPStringFormat format, String value) {
    return SERVICE != null ? SERVICE.parse(format, value) : EMPTY;
  }

  /**
   * Выделяет БИК
   *
   * @param value Запрос
   * @return БИК по строке
   */
  public static JpStringFormatParser.Result parseBik(String value) {
    return SERVICE != null ? SERVICE.parseBik(value) : EMPTY;
  }

  /**
   * Выделяет номер банковской карты
   *
   * @param value Запрос
   * @return номер банковской карты по строке
   */
  public static JpStringFormatParser.Result parseBankCardNumber(String value) {
    return SERVICE != null ? SERVICE.parseBankCardNumber(value) : EMPTY;
  }

  /**
   * Выделяет email
   *
   * @param value Запрос
   * @return Email по строке
   */
  public static JpStringFormatParser.Result parseEmail(String value) {
    return SERVICE != null ? SERVICE.parseEmail(value) : EMPTY;
  }

  /**
   * Выделяет ИНН физического лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  public static JpStringFormatParser.Result parseInn(String value) {
    return SERVICE != null ? SERVICE.parseInn(value) : EMPTY;
  }

  /**
   * Выделяет ИНН юридического лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  public static JpStringFormatParser.Result parseInn10(String value) {
    return SERVICE != null ? SERVICE.parseInn10(value) : EMPTY;
  }

  /**
   * Выделяет ИНН физического или юридического лица
   *
   * @param value Запрос
   * @return ИНН по строке
   */
  public static JpStringFormatParser.Result parseInnAny(String value) {
    return SERVICE != null ? SERVICE.parseInnAny(value) : EMPTY;
  }

  /**
   * Выделяет КБК
   *
   * @param value Запрос
   * @return КБК по строке
   */
  public static JpStringFormatParser.Result parseKbk(String value) {
    return SERVICE != null ? SERVICE.parseKbk(value) : EMPTY;
  }

  /**
   * Выделяет КПП
   *
   * @param value Запрос
   * @return КПП по строке
   */
  public static JpStringFormatParser.Result parseKpp(String value) {
    return SERVICE != null ? SERVICE.parseKpp(value) : EMPTY;
  }

  /**
   * Выделяет ОКТМО (8 символов)
   *
   * @param value Запрос
   * @return ОКТМО (8 символов)
   */
  public static JpStringFormatParser.Result parseOktmo(String value) {
    return SERVICE != null ? SERVICE.parseOktmo(value) : EMPTY;
  }

  /**
   * Выделяет ОКТМО (11 символов)
   *
   * @param value Запрос
   * @return ОКТМО (11 символов)
   */
  public static JpStringFormatParser.Result parseOktmo11(String value) {
    return SERVICE != null ? SERVICE.parseOktmo11(value) : EMPTY;
  }

  /**
   * Выделяет телефон
   *
   * @param value Запрос
   * @return Телефон по строке
   */
  public static JpStringFormatParser.Result parsePhone(String value) {
    return SERVICE != null ? SERVICE.parsePhone(value) : EMPTY;
  }

  /**
   * Выделяет СНИЛС
   *
   * @param value Запрос
   * @return СНИЛС по строке
   */
  public static JpStringFormatParser.Result parseSnils(String value) {
    return SERVICE != null ? SERVICE.parseSnils(value) : EMPTY;
  }

  /**
   * Выделяет единый регистрационный номер
   *
   * @param value Запрос
   * @return Единый регистрационный номер по строке
   */
  public static JpStringFormatParser.Result parseErn(String value) {
    return SERVICE != null ? SERVICE.parseErn(value) : EMPTY;
  }

  /**
   * Возвращает строку с заглавной буквы
   *
   * @param value Запрос
   * @return Строка с заглавной буквы
   */
  public static JpStringFormatParser.Result parseFio(String value) {
    return SERVICE != null ? SERVICE.parseFio(value) : EMPTY;
  }
}
