package mp.jprime.dataaccess.applyvalues.beans;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueParams;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

import java.util.ArrayList;
import java.util.Arrays;
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
  private final AuthInfo auth;
  private final Source source;

  private JPObjectApplyValueParamsBean(Object id, String jpClassCode, JPData data,
                                       Collection<String> attrs, AuthInfo auth, Source source) {
    this.id = id;
    this.jpClassCode = jpClassCode;
    this.data = data;
    this.attrs = attrs == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(attrs));
    this.auth = auth;
    this.source = source;
  }

  @Override
  public Object getId() {
    return id;
  }

  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  @Override
  public JPData getData() {
    return data;
  }

  @Override
  public Collection<String> getAttrs() {
    return attrs;
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
    private AuthInfo auth;
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
      return new JPObjectApplyValueParamsBean(id, jpClassCode, data, attrs, auth, source);
    }

    /**
     * Кодовые имена атрибутов класса, которые были модифицированы
     *
     * @param attrs Список кодовых имен атрибутов
     * @return Builder
     */
    public Builder attrs(String... attrs) {
      if (attrs.length > 0) {
        this.attrs = Arrays.asList(attrs);
      }
      return this;
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
