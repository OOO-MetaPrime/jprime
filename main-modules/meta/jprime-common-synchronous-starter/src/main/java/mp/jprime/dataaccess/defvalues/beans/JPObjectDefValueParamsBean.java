package mp.jprime.dataaccess.defvalues.beans;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueParams;
import mp.jprime.security.AuthInfo;

/**
 * Реализация JPObjectDefValueParams
 */
public final class JPObjectDefValueParamsBean implements JPObjectDefValueParams {
  private final Object rootId;
  private final String rootJpClassCode;
  private final JPData rootData;
  private final String refAttrCode;
  private final JPAttrData data;
  private final AuthInfo auth;
  private final Source source;

  private JPObjectDefValueParamsBean(Object rootId, String rootJpClassCode, JPData rootData,
                                     String refAttrCode, JPAttrData data,
                                     AuthInfo auth, Source source) {
    this.rootId = rootId;
    this.rootJpClassCode = rootJpClassCode;
    this.rootData = rootData;
    this.data = data != null ? data : JPData.empty();
    this.refAttrCode = refAttrCode;
    this.auth = auth;
    this.source = source;
  }

  @Override
  public Object getRootId() {
    return rootId;
  }

  @Override
  public String getRootJpClassCode() {
    return rootJpClassCode;
  }

  @Override
  public JPData getRootData() {
    return rootData;
  }

  @Override
  public String getRefAttrCode() {
    return refAttrCode;
  }

  @Override
  public JPAttrData getData() {
    return data;
  }

  @Override
  public AuthInfo getAuth() {
    return auth;
  }

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
    private JPAttrData data;
    private AuthInfo auth;
    private Source source;

    private Builder() {

    }

    /**
     * Создаем JPObjectDefValueParamsBean
     *
     * @return JPObjectDefValueParamsBean
     */
    public JPObjectDefValueParamsBean build() {
      return new JPObjectDefValueParamsBean(rootId, rootJpClassCode, rootData, refAttrCode, data, auth, source);
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
     * Данные текущего объекта
     *
     * @param data Данные текущего объекта
     * @return Builder
     */
    public Builder data(JPAttrData data) {
      this.data = data;
      return this;
    }

    /**
     * Данные авторизации
     *
     * @param auth Данные авторизации
     * @return Builder
     */
    public Builder auth(AuthInfo auth) {
      this.auth = auth;
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
