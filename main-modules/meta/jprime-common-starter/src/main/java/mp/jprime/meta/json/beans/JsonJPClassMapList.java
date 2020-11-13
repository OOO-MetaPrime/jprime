package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;

@JsonPropertyOrder({
    "totalCount",
    "maps"
})
public final class JsonJPClassMapList {
  private final Long totalCount;
  private final Collection<JsonJPClassMap> maps;

  private JsonJPClassMapList(Long totalCount, Collection<JsonJPClassMap> maps) {
    this.totalCount = totalCount;
    this.maps = maps;
  }

  /**
   * Список классов
   *
   * @return Список классов
   */
  public Collection<JsonJPClassMap> getMaps() {
    return maps;
  }

  /**
   * Общее количество классов
   *
   * @return Общее количество классов
   */
  public Long getTotalCount() {
    return totalCount;
  }

  /**
   * Построитель JsonJPClasses
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPClasses
   */
  public static final class Builder {
    private Collection<JsonJPClassMap> maps;
    private Long totalCount;

    private Builder() {
    }

    /**
     * Список мапинга
     *
     * @param maps Список мапинга
     * @return Builder
     */
    public Builder maps(Collection<JsonJPClassMap> maps) {
      this.maps = maps;
      return this;
    }

    /**
     * Общее количество классов
     *
     * @param totalCount Общее количество классов
     * @return Builder
     */
    public Builder totalCount(long totalCount) {
      this.totalCount = totalCount;
      return this;
    }

    /**
     * Создаем JsonJPClasses
     *
     * @return JsonJPClasses
     */
    public JsonJPClassMapList build() {
      return new JsonJPClassMapList(totalCount, maps);
    }
  }
}
