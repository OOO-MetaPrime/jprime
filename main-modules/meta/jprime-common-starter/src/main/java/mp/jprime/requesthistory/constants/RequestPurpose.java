package mp.jprime.requesthistory.constants;

import org.springframework.util.StringUtils;

/**
 * Цель запроса
 */
public enum RequestPurpose {
  FIND("find", "Запрос объекта"),
  SEARCH("search", "Запрос списка"),
  FIND_RESULT("findResult", "Просмотр объекта"),
  SEARCH_RESULT("searchResult", "Превью объекта");

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
   * @param purpose Код
   * @return Цель запроса
   */
  public static RequestPurpose getByCode(String purpose) {
    if (!StringUtils.hasText(purpose)) {
      return null;
    }
    for (RequestPurpose v : RequestPurpose.values()) {
      if (v.code.equalsIgnoreCase(purpose)) {
        return v;
      }
    }
    return null;
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
