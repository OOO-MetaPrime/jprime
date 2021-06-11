package mp.jprime.dataaccess.defvalues;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

/**
 * Реализация JPObjectDefValueParams
 */
public final class JPObjectDefValueParamsBean implements JPObjectDefValueParams {
  private final Object rootId;
  private final String rootJpClassCode;
  private final JPData rootData;
  private final String refAttrCode;
  private final AuthInfo authInfo;
  private final Source source;

  private JPObjectDefValueParamsBean(Object rootId, String rootJpClassCode, JPData rootData,
                                     String refAttrCode, AuthInfo authInfo, Source source) {
    this.rootId = rootId;
    this.rootJpClassCode = rootJpClassCode;
    this.rootData = rootData;
    this.refAttrCode = refAttrCode;
    this.authInfo = authInfo;
    this.source = source;
  }

  /**
   * Идентификатор объекта, из которого создаем
   * Может быть не указан
   *
   * @return Идентификатор объекта, из которого создаем
   */
  @Override
  public Object getRootId() {
    return rootId;
  }

  /**
   * Кодовое имя класса объекта, из которого создаем
   * Может быть не указан
   *
   * @return Кодовое имя класса объекта, из которого создаем
   */
  @Override
  public String getRootJpClassCode() {
    return rootJpClassCode;
  }

  /**
   * Данные объекта, из которого создаем
   * Могут быть не указаны
   *
   * @return Данные объекта, из которого создаем
   */
  @Override
  public JPData getRootData() {
    return rootData;
  }

  /**
   * Атрибут текущего класса, по которому строилась ссылка
   * Может быть не указан
   *
   * @return Атрибут текущего класса, по которому строилась ссылка
   */
  @Override
  public String getRefAttrCode() {
    return refAttrCode;
  }

  /**
   * Данные авторизации
   * Могут быть не указаны
   *
   * @return Данные авторизации
   */
  @Override
  public AuthInfo getAuth() {
    return authInfo;
  }

  /**
   * Источник данных
   *
   * @return Источник данных
   */
  @Override
  public Source getSource() {
    return source != null ? source : Source.SYSTEM;
  }

  /**
   * Построитель JPObjectDefValueParamsBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPObjectDefValueParamsBean
   */
  public static final class Builder {
    private Object rootId;
    private String rootJpClassCode;
    private JPData rootData;
    private String refAttrCode;
    private AuthInfo authInfo;
    private Source source;

    private Builder() {

    }

    /**
     * Создаем JPObjectDefValueParamsBean
     *
     * @return JPObjectDefValueParamsBean
     */
    public JPObjectDefValueParamsBean build() {
      return new JPObjectDefValueParamsBean(rootId, rootJpClassCode, rootData, refAttrCode, authInfo, source);
    }

    /**
     * Идентификатор объекта, из которого создаем
     *
     * @param rootId Идентификатор объекта, из которого создаем
     * @return Builder
     */
    public Builder rootId(Object rootId) {
      this.rootId = rootId;
      return this;
    }

    /**
     * Кодовое имя класса объекта, из которого создаем
     *
     * @param rootJpClassCode Кодовое имя класса объекта, из которого создаем
     * @return Builder
     */
    public Builder rootJpClassCode(String rootJpClassCode) {
      this.rootJpClassCode = rootJpClassCode;
      return this;
    }

    /**
     * Данные объекта, из которого создаем
     *
     * @param rootData Данные объекта, из которого создаем
     * @return Builder
     */
    public Builder rootData(JPData rootData) {
      this.rootData = rootData;
      return this;
    }

    /**
     * Атрибут текущего класса, по которому строилась ссылка
     *
     * @param refAttrCode Атрибут текущего класса, по которому строилась ссылка
     * @return Builder
     */
    public Builder refAttrCode(String refAttrCode) {
      this.refAttrCode = refAttrCode;
      return this;
    }

    /**
     * Данные авторизации
     *
     * @param authInfo Данные авторизации
     * @return Builder
     */
    public Builder auth(AuthInfo authInfo) {
      this.authInfo = authInfo;
      return this;
    }

    /**
     * Источник данных
     *
     * @param source Источник данных
     * @return Builder
     */
    public Builder source(Source source) {
      this.source = source;
      return this;
    }
  }
}
