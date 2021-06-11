package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Информация о приложении. Код-название
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAppInfo {
  private String code;
  private String title;


  /**
   * Конструктор
   */
  public JsonAppInfo() {

  }

  /**
   * Конструктор
   *
   * @param code  Код приложения
   * @param title Название приложения
   */
  private JsonAppInfo(String code, String title) {
    this.code = code;
    this.title = title;
  }

  /**
   * Код приложения
   *
   * @return Код приложения
   */
  public String getCode() {
    return code;
  }

  /**
   * Название приложения
   *
   * @return Название приложения
   */
  public String getTitle() {
    return title;
  }

  /**
   * Построитель JsonAppInfo
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonAppInfo
   */
  public static final class Builder {
    private String code;
    private String title;

    private Builder() {
    }

    /**
     * Создаем JsonAppInfo
     *
     * @return JsonAppInfo
     */
    public JsonAppInfo build() {
      return new JsonAppInfo(code, title);
    }

    /**
     * Код приложения
     *
     * @param code Код приложения
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Название приложения
     *
     * @param title Название приложения
     * @return Builder
     */
    public Builder title(String title) {
      this.title = title;
      return this;
    }
  }
}
