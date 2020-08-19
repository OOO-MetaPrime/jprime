package mp.jprime.security.services;

import mp.jprime.dataaccess.params.query.Filter;

/**
 * Доступ к ресурсу
 */
public class JPResourceAccessBean implements JPResourceAccess {
  private final Boolean access;
  private final Filter filter;

  private JPResourceAccessBean(Boolean access, Filter filter) {
    this.access = access != null ? access : Boolean.FALSE;
    this.filter = filter;
  }

  /**
   * Доступ
   *
   * @return Да/Нет
   */
  @Override
  public boolean isAccess() {
    return access;
  }

  /**
   * Возвращает ограничение данных
   *
   * @return ограничение данных
   */
  @Override
  public Filter getFilter() {
    return filter;
  }

  public static JPResourceAccessBean from(Boolean access) {
    return new JPResourceAccessBean(access, null);
  }

  public static JPResourceAccessBean from(Boolean access, Filter filter) {
    return new JPResourceAccessBean(access, filter);
  }
}


