package mp.jprime.security.abac;

import mp.jprime.dataaccess.conds.CollectionCond;

/**
 * Правило - атрибуты ресурса
 */
public interface ResourceRule extends Rule {
  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String getAttrCode();

  /**
   * Возвращает условие на атрибуты
   *
   * @return Условие на атрибут
   */
  CollectionCond<String> getCond();
}
