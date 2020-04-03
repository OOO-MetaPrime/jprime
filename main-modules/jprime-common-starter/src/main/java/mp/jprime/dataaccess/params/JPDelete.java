package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.security.AuthInfo;

/**
 * Запрос удаления
 */
public class JPDelete extends JPBaseCRUD {
  private final JPId jpId;
  private final AuthInfo auth;


  /**
   * Конструктор
   *
   * @param jpId   Идентификатор объекта
   * @param auth   Данные аутентификации
   * @param source Источник данных
   */
  private JPDelete(JPId jpId, AuthInfo auth, Source source) {
    super(source);
    this.jpId = jpId;
    this.auth = auth;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  public JPId getJpId() {
    return jpId;
  }

  /**
   * Класс объекта
   *
   * @return Класс объекта
   */
  public String getJpClass() {
    return getJpId().getJpClass();
  }

  /**
   * Данные аутентификации
   *
   * @return Данные аутентификации
   */
  public AuthInfo getAuth() {
    return auth;
  }

  /**
   * Построитель Builder
   *
   * @param jpId Идентификатор объекта
   * @return Builder
   */
  public static Builder delete(JPId jpId) {
    return new Builder(jpId);
  }

  /**
   * Построитель JPDelete
   */
  public static final class Builder {
    private JPId jpId;
    private AuthInfo auth;
    private Source source;

    private Builder(JPId jpId) {
      this.jpId = jpId;
    }

    /**
     * Создаем JPDelete
     *
     * @return JPDelete
     */
    public JPDelete build() {
      return new JPDelete(jpId, auth, source);
    }

    /**
     * Аутентификация
     *
     * @param auth Аутентификация
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