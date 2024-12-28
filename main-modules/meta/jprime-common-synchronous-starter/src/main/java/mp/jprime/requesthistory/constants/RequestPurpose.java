package mp.jprime.requesthistory.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Цель запроса
 */
public enum RequestPurpose {
  FIND("find", "Запрос объекта"),
  SEARCH("search", "Запрос списка"),
  FIND_RESULT("findResult", "Просмотр объекта"),
  SEARCH_RESULT("searchResult", "Превью объекта");

  private static final Map<String, RequestPurpose> BY_CODE = new HashMap<>();

  static {
    for (RequestPurpose type : RequestPurpose.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;
  private final String title;

  RequestPurpose(String code, String title) {
    this.code = code;
    this.title = title;
  }

  /**
   * Получить код
   *
   * @return Код
   */
  public String getCode() {
    return code;
  }

  /**
   * Получить описание
   *
   * @return Описание
   */
  public String getTitle() {
    return title;
  }

  /**
   * Получить Цель запроса по коду
   *
   * @param code Код
   * @return Цель запроса
   */
  public static RequestPurpose getByCode(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }

  /**
   * Получить код по цели запроса
   *
   * @param purpose Цель запроса
   * @return Код
   */
  public static String getCode(RequestPurpose purpose) {
    return purpose == null ? null : purpose.getCode();
  }
}
