package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
 * Модель данных ответа получения jpId
 */
@JsonPropertyOrder({
    "id",
    "classCode"
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonJPId {
  private final Comparable id;
  private final String classCode;

  /**
   * Конструктор
   *
   * @param id        Идентификатор объекта
   * @param classCode Кодовое имя класса
   */
  private JsonJPId(Comparable id, String classCode) {
    this.id = id;
    this.classCode = classCode;
  }

  public Comparable getId() {
    return id;
  }

  public String getClassCode() {
    return classCode;
  }

  /**
   * Построитель JsonJPId
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель ObjectResult
   */
  public static final class Builder {
    private Comparable id;
    private String classCode;


    private Builder() {
    }

    /**
     * Идентификатор
     *
     * @param id Идентификатор
     * @return Builder
     */
    public Builder id(Comparable id) {
      this.id = id;
      return this;
    }

    /**
     * Кодовое имя класса
     *
     * @param classCode Кодовое имя класса
     * @return Builder
     */
    public Builder classCode(String classCode) {
      this.classCode = classCode;
      return this;
    }


    /**
     * Создаем JsonJPId
     *
     * @return JsonJPId
     */
    public JsonJPId build() {
      return new JsonJPId(id, classCode);
    }
  }
}
