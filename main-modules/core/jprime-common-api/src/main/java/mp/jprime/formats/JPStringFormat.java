package mp.jprime.formats;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип формата строки
 */
public enum JPStringFormat {
  NONE("none", "Не определен"),
  BANK_ACCOUNT_NUMBER("bankAccountNumber", "Номер банковского счета"),
  BANK_CARD_NUMBER("bankCardNumber", "Номер банковской карты"),
  BIK("bik", "БИК"),
  EMAIL("email", "Email"),
  ERN("ern", "Номер ЕРН (Единый реестр населения)"),
  INN("inn", "ИНН ФЛ"),
  INN_10("inn10", "ИНН ЮЛ"),
  INN_ANY("innAny", "ИНН ФЛ/ЮЛ"),
  KBK("kbk", "КБК"),
  KPP("kpp", "КПП"),
  OGRN("ogrn", "ОГРН"),
  OGRN_IE("ogrnIE", "ОГРН ИП"),
  OKTMO("oktmo", "ОКТМО (8 знаков)"),
  OKTMO_11("oktmo11", "ОКТМО (11 знаков)"),
  OKTMO_ANY("oktmoAny", "ОКТМО (8/11 знаков)"),
  PASSPORT_SERIES("passportSeries", "Серия паспорта РФ"),
  PASSPORT_NUMBER("passportNumber", "Номер паспорта РФ"),
  PASSPORT_DEPARTMENT_CODE("passportDepartmentCode", "Код подразделения паспорта РФ"),
  PASSWORD("password", "Пароль"),
  PHONE("phone", "Телефон"),
  SNILS("snils", "СНИЛС"),
  ZAGS_AGS("zagsAgs", "Номер записи акта гр.состояния"),
  ZAGS_DEPARTAMENT_CODE("zagsDepartmentCode", "Код подразделения ЗАГС"),
  ZAGS_NUMBER("zagsNumber", "Номер св-ва ЗАГС"),
  ZAGS_SERIES("zagsSeries", "Серия св-ва ЗАГС");

  private static final Map<String, JPStringFormat> BY_CODE = new HashMap<>();

  static {
    for (JPStringFormat format : JPStringFormat.values()) {
      BY_CODE.put(format.getCode().toLowerCase(), format);
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
  public static String getCode(JPStringFormat format) {
    return format == null ? null : format.getCode();
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
