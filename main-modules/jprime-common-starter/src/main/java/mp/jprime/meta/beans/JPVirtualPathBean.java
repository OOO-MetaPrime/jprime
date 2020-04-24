package mp.jprime.meta.beans;

import mp.jprime.meta.JPVirtualPath;

/**
 * Бин JPVirtualPath
 */
public class JPVirtualPathBean implements JPVirtualPath {
  /**
   * Кодовое имя ссылочного атрибута, по которому строится ссылка
   */
  private final String refAttrCode;
  /**
   * Кодовое имя целевого атрибута, на которыйф строится ссылка
   */
  private final String targerAttrCode;

  private JPVirtualPathBean(String refAttrCode, String targerAttrCode) {
    this.refAttrCode = refAttrCode;
    this.targerAttrCode = targerAttrCode;
  }

  /**
   * Кодовое имя ссылочного атрибута, по которому строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getRefAttrCode() {
    return refAttrCode;
  }

  /**
   * Кодовое имя целевого атрибута, на которыйф строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getTargerAttrCode() {
    return targerAttrCode;
  }

  /**
   * Построитель JPVirtualPath
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String refAttrCode;
    private String targerAttrCode;

    private Builder() {

    }

    public JPVirtualPathBean build() {
      return new JPVirtualPathBean(refAttrCode, targerAttrCode);
    }

    /**
     * Кодовое имя ссылочного атрибута, по которому строится ссылка
     *
     * @param refAttrCode Кодовое имя
     * @return Builder
     */
    public Builder refAttrCode(String refAttrCode) {
      this.refAttrCode = refAttrCode;
      return this;
    }

    /**
     * Кодовое имя целевого атрибута, на которыйф строится ссылка
     *
     * @param targerAttrCode Кодовое имя
     * @return Builder
     */
    public Builder targerAttrCode(String targerAttrCode) {
      this.targerAttrCode = targerAttrCode;
      return this;
    }
  }
}

