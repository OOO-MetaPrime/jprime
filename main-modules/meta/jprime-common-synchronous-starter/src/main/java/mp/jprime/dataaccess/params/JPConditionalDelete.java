package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.security.AuthInfo;

/**
 * Запрос удаления по условию
 */
public class JPConditionalDelete extends JPBaseParams {
  private final String jpClass;
  private final Filter where;

  private JPConditionalDelete(String jpClass, Filter where, Source source, AuthInfo auth) {
    super(source, auth);
    this.jpClass = jpClass;
    this.where = where;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Возвращает условие удаления объектов
   * @return условие удаления объектов
   */
  public Filter getWhere() {
    return where;
  }

  public static JPConditionalDelete.Builder delete(String jpClass, Filter where) {
    return new Builder(jpClass, where);
  }

  /**
   * Построитель JPDelete
   */
  public static final class Builder {
    private final String jpClass;
    private final Filter where;
    private AuthInfo auth;
    private Source source;

    private Builder(String jpClass, Filter where) {
      this.jpClass = jpClass;
      this.where = where;
    }

    /**
     * Создаем JPDelete
     *
     * @return JPDelete
     */
    public JPConditionalDelete build() {
      return new JPConditionalDelete(jpClass, where, source, auth);
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
