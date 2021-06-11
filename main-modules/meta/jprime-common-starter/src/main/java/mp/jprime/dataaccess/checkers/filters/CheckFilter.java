package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.security.AuthInfo;

/**
 * Проверка значений указанным условиям
 *
 * @param <T> Filter
 */
public interface CheckFilter<T extends Filter> {
  boolean check(T filter, JPMutableData data, AuthInfo auth);
}
