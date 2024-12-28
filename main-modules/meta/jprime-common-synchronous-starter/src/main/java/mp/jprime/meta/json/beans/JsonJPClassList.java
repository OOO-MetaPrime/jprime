package mp.jprime.meta.json.beans;

import java.util.Collection;

public final class JsonJPClassList {
  private Long totalCount;
  private Long count;
  private Collection<JsonJPClass> classes;

  public JsonJPClassList() {

  }

  private JsonJPClassList(Long totalCount, Long count, Collection<JsonJPClass> classes) {
    this.totalCount = totalCount;
    this.count = count;
    this.classes = classes;
  }

  /**
   * Список классов
   *
   * @return Список классов
   */
  public Collection<JsonJPClass> getClasses() {
    return classes;
  }

  /**
   * Возвращает количество в выборке
   *
   * @return Количество в выборке
   */
  public Long getCount() {
    return count;
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
    private Collection<JsonJPClass> classes;
    private Long totalCount;
    private Long count;

    private Builder() {
    }

    /**
     * Список классов
     *
     * @param classes Список классов
     * @return Builder
     */
    public Builder classes(Collection<JsonJPClass> classes) {
      this.classes = classes;
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
     * количество классов в выборке
     *
     * @param count количество классов в выборке
     * @return Builder
     */
    public Builder count(long count) {
      this.count = count;
      return this;
    }

    /**
     * Создаем JsonJPClasses
     *
     * @return JsonJPClasses
     */
    public JsonJPClassList build() {
      return new JsonJPClassList(totalCount, count, classes);
    }
  }
}
