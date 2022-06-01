package mp.jprime.dataaccess.applyvalues;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Реализация JPObjectApplyValueParams
 */
public final class JPObjectApplyValueParamsBean implements JPObjectApplyValueParams {
  private final Object id;
  private final String jpClassCode;
  private final JPData data;
  private final Collection<String> attrs;
  private final AuthInfo authInfo;
  private final Source source;

  private JPObjectApplyValueParamsBean(Object id, String jpClassCode, JPData data,
                                       Collection<String> attrs, AuthInfo authInfo, Source source) {
    this.id = id;
    this.jpClassCode = jpClassCode;
    this.data = data;
    this.attrs = attrs == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(attrs));
    this.authInfo = authInfo;
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
   * Данные объекта
   *
   * @return Данные объекта
   */
  @Override
  public JPData getData() {
    return data;
  }

  /**
   * Кодовые имена атрибутов класса, которые были модифицированы
   *
   * @return Список кодовых имен атрибутов
   */
  @Override
  public Collection<String> getAttrs() {
    return attrs;
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
   * Построитель JPObjectApplyValueParamsBean
   *
   * @param jpClassCode Кодовое имя класса объекта
   * @param data        Данные объекта
   * @return Builder
   */
  public static Builder newBuilder(String jpClassCode, JPData data) {
    return new Builder(null, jpClassCode, data);
  }

  /**
   * Построитель JPObjectApplyValueParamsBean
   *
   * @param id          Идентификатор объекта
   * @param jpClassCode Кодовое имя класса объекта
   * @param data        Данные объекта
   * @return Builder
   */
  public static Builder newBuilder(Object id, String jpClassCode, JPData data) {
    return new Builder(id, jpClassCode, data);
  }

  /**
   * Построитель JPObjectApplyValueParamsBean
   */
  public static final class Builder {
    private final Object id;
    private final String jpClassCode;
    private final JPData data;
    private Collection<String> attrs;
    private AuthInfo authInfo;
    private Source source;

    private Builder(Object id, String jpClassCode, JPData data) {
      this.id = id;
      this.jpClassCode = jpClassCode;
      this.data = data;
    }

    /**
     * Создаем JPObjectApplyValueParams
     *
     * @return JPObjectApplyValueParams
     */
    public JPObjectApplyValueParamsBean build() {
      return new JPObjectApplyValueParamsBean(id, jpClassCode, data, attrs, authInfo, source);
    }

    /**
     * Кодовые имена атрибутов класса, которые были модифицированы
     *
     * @param attrs Список кодовых имен атрибутов
     * @return Builder
     */
    public Builder attrs(Collection<String> attrs) {
      this.attrs = attrs;
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
