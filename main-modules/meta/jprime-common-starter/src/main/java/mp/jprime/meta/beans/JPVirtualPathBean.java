package mp.jprime.meta.beans;

import mp.jprime.meta.JPVirtualPath;

/**
 * Бин JPVirtualPath
 */
public final class JPVirtualPathBean implements JPVirtualPath {
  /**
   * Кодовое имя ссылочного атрибута, по которому строится ссылка
   */
  private final String refAttrCode;
  /**
   * Кодовое имя целевого атрибута, на который строится ссылка
   */
  private final String targerAttrCode;
  /**
   * Тип виртуальной ссылки
   */
  private final JPType virtualType;

  private JPVirtualPathBean(String refAttrCode, String targerAttrCode, String virtualType) {
    this.refAttrCode = refAttrCode;
    this.targerAttrCode = targerAttrCode;
    this.virtualType = virtualType != null && !virtualType.isEmpty() ? JPType.getType(virtualType) : null;
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
   * Кодовое имя целевого атрибута, на который строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getTargerAttrCode() {
    return targerAttrCode;
  }

  /**
   * Тип виртуальной ссылки
   *
   * @return Тип виртуальной ссылки
   */
  @Override
  public JPType getType() {
    return virtualType;
  }

  /**
   * Построитель JPVirtualPath
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Путь виртуальной ссылки
   *
   * @param attrsPath Путь виртуальной ссылки
   * @param attrType  Код типа атрибута на который ссылаемся
   * @return JPVirtualPathBean
   */
  public static JPVirtualPath newInstance(String[] attrsPath, JPType attrType) {
    if (attrsPath == null || attrsPath.length == 0) {
      return null;
    }
    if (attrsPath.length == 2) {
      return new JPVirtualPathBean(attrsPath[0], attrsPath[1], attrType != null ? attrType.getCode() : null);
    }
    return null;
  }

  /**
   * Путь виртуальной ссылки
   *
   * @param virtualReference Путь виртуальной ссылки
   * @param virtualType      Код типа атрибута на который ссылаемся
   * @return JPVirtualPathBean
   */
  public static JPVirtualPath newInstance(String virtualReference, String virtualType) {
    if (virtualReference != null && !virtualReference.isEmpty()) {
      String[] parts = virtualReference.split("\\.");
      if (parts.length == 2) {
        return new JPVirtualPathBean(parts[0], parts[1], virtualType);
      }
    }
    return null;
  }

  public static final class Builder {
    private String refAttrCode;
    private String targerAttrCode;
    private String virtualType;

    private Builder() {

    }

    public JPVirtualPathBean build() {
      return new JPVirtualPathBean(refAttrCode, targerAttrCode, virtualType);
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
     * Кодовое имя целевого атрибута, на который строится ссылка
     *
     * @param targerAttrCode Кодовое имя
     * @return Builder
     */
    public Builder targerAttrCode(String targerAttrCode) {
      this.targerAttrCode = targerAttrCode;
      return this;
    }

    /**
     * Тип виртуальной ссылки
     *
     * @param virtualType Тип виртуальной ссылки
     * @return Builder
     */
    public Builder type(String virtualType) {
      this.virtualType = virtualType;
      return this;
    }
  }
}

