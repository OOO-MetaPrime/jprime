package mp.jprime.dataaccess.addinfos.beans;

import mp.jprime.dataaccess.addinfos.AddInfo;

/**
 * Реализация AddInfo
 */
public final class AddInfoBean implements AddInfo {
  /**
   * Код
   */
  private final String code;
  /**
   * Информация
   */
  private final String info;

  private AddInfoBean(String code, String info) {
    this.code = code;
    this.info = info;
  }

  /**
   * Код сведений
   *
   * @return Код сведений
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Информация
   *
   * @return Информация
   */
  @Override
  public String getInfo() {
    return info;
  }

  /**
   * Создать новый экземпляр AddInfo
   *
   * @param code Код
   * @param info Информация
   * @return Новый экземпляр AddInfo
   */
  public static AddInfo newInstance(String code, String info) {
    return new AddInfoBean(code, info);
  }

  /**
   * Построитель AddInfoBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель AddInfoBean
   */
  public static final class Builder {
    private String code;
    private String info;

    private Builder() {

    }

    /**
     * Создаем AddInfoBean
     *
     * @return AddInfoBean
     */
    public AddInfoBean build() {
      return new AddInfoBean(code, info);
    }

    /**
     * Код
     *
     * @param code Код
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Информация
     *
     * @param info Информация
     * @return Builder
     */
    public Builder info(String info) {
      this.info = info;
      return this;
    }
  }
}
