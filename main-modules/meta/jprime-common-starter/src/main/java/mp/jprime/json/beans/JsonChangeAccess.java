package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Доступ на изменение (удаление/редактирование)
 */
@JsonPropertyOrder({
    "update",
    "delete"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonChangeAccess {
  private boolean update;
  private boolean delete;

  public JsonChangeAccess() {

  }

  private JsonChangeAccess(boolean update, boolean delete) {
    this.update = update;
    this.delete = delete;
  }

  public boolean isUpdate() {
    return update;
  }

  public boolean isDelete() {
    return delete;
  }


  /**
   * Построитель {@link JsonChangeAccess}
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель {@link JsonChangeAccess}
   */
  public static final class Builder {
    private boolean update;
    private boolean delete;

    private Builder() {

    }

    /**
     * Создаем {@link JsonChangeAccess}
     *
     * @return {@link JsonChangeAccess}
     */
    public JsonChangeAccess build() {
      return new JsonChangeAccess(update, delete);
    }

    /**
     * Доступ на изменение
     *
     * @param update Доступ на изменение
     * @return Builder
     */
    public Builder update(boolean update) {
      this.update = update;
      return this;
    }

    /**
     * Доступ на удаление
     *
     * @param delete Доступ на удаление
     * @return Builder
     */
    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }
  }
}
