package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Свойства типа атрибута
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonTypeProperty {
  private final String code;
  private final Collection<String> availableChanges;

  private JsonTypeProperty(String code, Collection<String> availableChanges) {
    this.code = code;
    this.availableChanges = availableChanges;
  }

  /**
   * Код типа атрибута
   *
   * @return Код типа
   */
  public String getCode() {
    return code;
  }

  /**
   * Список типов, на которые можно изменить текущий
   *
   * @return Список типов, на которые можно изменить текущий
   */
  public Collection<String> getAvailableChanges() {
    return availableChanges;
  }

  /**
   * Построитель {@link JsonTypeProperty}
   *
   * @param code Код типа атрибута
   * @return Builder
   */
  public static Builder newBuilder(String code) {
    return new Builder(code);
  }

  /**
   * Построитель  {@link JsonTypeProperty}
   */
  public static final class Builder {
    private final String code;
    private Collection<String> availableChanges;

    private Builder(String code) {
      this.code = code;
    }

    /**
     * Список типов, на которые можно изменить указанный
     *
     * @param types Список типов, на которые можно изменить указанный
     * @return Builder
     */
    public Builder availableChanges(Collection<JPType> types) {
      if (types != null) {
        availableChanges = types.stream()
            .map(x -> x.getCode())
            .collect(Collectors.toList());
      }
      return this;
    }

    /**
     * Создаем  {@link JsonTypeProperty}
     *
     * @return {@link JsonTypeProperty}
     */
    public JsonTypeProperty build() {
      return new JsonTypeProperty(code, availableChanges);
    }
  }
}
