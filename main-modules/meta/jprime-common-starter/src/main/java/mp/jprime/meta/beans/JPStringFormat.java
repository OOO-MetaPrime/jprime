package mp.jprime.meta.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип формата строки
 */
public enum JPStringFormat {
  BANK_CARD_NUMBER("bankCardNumber", "Номер банковской карты"),
  BIK("bik", "БИК"),
  EMAIL("email", "Email"),
  INN("inn", "ИНН ФЛ"),
  INN_10("inn10", "ИНН ЮЛ"),
  INN_ANY("innAny", "ИНН ФЛ/ЮЛ"),
  KBK("kbk", "КБК"),
  KPP("kpp", "КПП"),
  OKTMO("oktmo", "ОТКМО (8 знаков)"),
  OKTMO_11("oktmo11", "ОТКМО (11 знаков)"),
  PHONE("phone", "Телефон"),
  SNILS("snils", "СНИЛС");

  private static final Map<String, JPStringFormat> BY_CODE = new HashMap<>();

  static {
    for (JPStringFormat stringFormat : JPStringFormat.values()) {
      BY_CODE.put(stringFormat.getCode().toLowerCase(), stringFormat);
    }
  }

  /**
   * Код формата строки
   */
  private final String code;

  /**
   * Название формата строки
   */
  private final String name;

  JPStringFormat(String code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * Возвращает код формата строки
   *
   * @return Код формата строки
   */
  public String getCode() {
    return code;
  }

  /**
   * Возвращает название формата строки
   *
   * @return Название формата строки
   */
  public String getName() {
    return name;
  }

  /**
   * Возвращает код формата строки
   *
   * @return Код
   */
  public static String getCode(JPStringFormat stringFormat) {
    return stringFormat == null ? null : stringFormat.getCode();
  }

  /**
   * Возвращает тип формата строки
   *
   * @param code Код
   * @return Тип формата строки
   */
  public static JPStringFormat getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
