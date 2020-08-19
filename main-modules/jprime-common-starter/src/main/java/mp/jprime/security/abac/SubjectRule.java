package mp.jprime.security.abac;

import mp.jprime.dataaccess.conds.CollectionCond;

/**
 * Правило - субъект доступа
 */
public interface SubjectRule extends Rule {
  /**
   * Возвращает условие на логин
   *
   * @return Условие на логин
   */
  CollectionCond<String> getUsernameCond();

  /**
   * Возвращает условие на роли
   *
   * @return Условие на роли
   */
  CollectionCond<String> getRoleCond();

  /**
   * Возвращает условие на организацию
   *
   * @return Условие на организацию
   */
  CollectionCond<String> getOrgIdCond();

  /**
   * Возвращает условие на подразделение
   *
   * @return Условие на подразделение
   */
  CollectionCond<String> getDepIdCond();
}
