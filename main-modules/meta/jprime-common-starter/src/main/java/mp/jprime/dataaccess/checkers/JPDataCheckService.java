package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.security.AuthInfo;

/**
 * Сервис проверки данных указанному условию
 */
public interface JPDataCheckService {
  boolean check(Filter filter, JPMutableData data, AuthInfo auth);

  default boolean check(Filter filter, JPMutableData data) {
    return check(filter, data, null);
  }
}
