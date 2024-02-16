package mp.jprime.common.beans;


import mp.jprime.common.JPClassAttr;

/**
 * Путь атрибута
 */
public final class JPClassAttrBean implements JPClassAttr {
  /**
   * Кодовое имя класса
   */
  private final String jpClass;
  /**
   * Кодовое имя атрибута
   */
  private final String jpAttr;

  @Override
  public String getJpClass() {
    return jpClass;
  }

  @Override
  public String getJpAttr() {
    return jpAttr;
  }

  private JPClassAttrBean(String jpClass, String jpAttr) {
    this.jpClass = jpClass;
    this.jpAttr = jpAttr;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    /**
     * Кодовое имя класса
     */
    private String jpClass;
    /**
     * Кодовое имя атрибута
     */
    private String jpAttr;

    private Builder() {
    }

    public JPClassAttrBean build() {
      return new JPClassAttrBean(jpClass, jpAttr);
    }

    public Builder jpClass(String jpClass) {
      this.jpClass = jpClass;
      return this;
    }

    public Builder jpAttr(String jpAttr) {
      this.jpAttr = jpAttr;
      return this;
    }
  }
}
