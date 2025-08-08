package mp.jprime.dataaccess;

import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

public interface JPSubQueryService {
  /**
   * Возвращает значения согласно запросу
   *
   * @param subQuery JPSubQuery
   * @param auth     AuthInfo
   * @return Список значений
   */
  Collection<Comparable> getValues(JPSubQuery subQuery, AuthInfo auth);
}
