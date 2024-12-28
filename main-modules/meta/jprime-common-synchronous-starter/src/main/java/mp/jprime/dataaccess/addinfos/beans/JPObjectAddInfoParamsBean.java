package mp.jprime.dataaccess.addinfos.beans;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoParams;
import mp.jprime.security.AuthInfo;

/**
 * Реализация JPObjectAddInfoParams
 */
public final class JPObjectAddInfoParamsBean implements JPObjectAddInfoParams {
  private final Object id;
  private final String jpClassCode;
  private final AuthInfo auth;
  private final Source source;

  private JPObjectAddInfoParamsBean(Object id, String jpClassCode, AuthInfo auth, Source source) {
    this.id = id;
    this.jpClassCode = jpClassCode;
    this.auth = auth;
    this.source = source;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  @Override
  public Object getId() {
    return id;
  }

  /**
   * Кодовое имя класса объекта
   *
   * @return Кодовое имя класса объекта
   */
  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  /**
   * Данные авторизации
   * Могут быть не указаны
   *
   * @return Данные авторизации
   */
  @Override
  public AuthInfo getAuth() {
    return auth;
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
   * Построитель JPObjectAddInfoParamsBean
   *
   * @param jpClassCode Кодовое имя класса объекта
   * @param id          Идентификатор объекта
   * @return Builder
   */
  public static Builder newBuilder(String jpClassCode, Object id) {
    return new Builder(jpClassCode, id);
  }

  /**
   * Построитель JPObjectAddInfoParamsBean
   */
  public static final class Builder {
    private Object id;
    private String jpClassCode;
    private AuthInfo auth;
    private Source source;

    private Builder(String jpClassCode, Object id) {
      this.jpClassCode = jpClassCode;
      this.id = id;
    }

    /**
     * Создаем JPObjectAddInfoParamsBean
     *
     * @return JPObjectAddInfoParamsBean
     */
    public JPObjectAddInfoParamsBean build() {
      return new JPObjectAddInfoParamsBean(id, jpClassCode, auth, source);
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
