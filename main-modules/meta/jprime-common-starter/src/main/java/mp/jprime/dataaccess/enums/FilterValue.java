package mp.jprime.dataaccess.enums;

/**
 * Константные значения фильтра
 */
public interface FilterValue {
  /**
   * Идентификатор пользователя
   */
  String AUTH_USERID = "{AUTH_USERID}";

  /**
   * Организация пользователя
   */
  String AUTH_ORGID = "{AUTH_ORGID}";

  /**
   * Обособленное подразделение пользователя
   */
  String AUTH_SEPDEPID = "{AUTH_SEPDEPID}";

  /**
   * Подразделение пользователя
   */
  String AUTH_DEPID = "{AUTH_DEPID}";

  /**
   * Предметные группы пользователя
   */
  String AUTH_SUBJECT_GROUP = "{AUTH_SUBJECT_GROUP}";

  /**
   * ОКТМО пользователя
   * 75 738 000 -> 75 738
   * 75 000 000 -> 75
   */
  String AUTH_OKTMO = "{AUTH_OKTMO}";

  /**
   * Дерево ОКТМО пользователя
   * 75 738 123 ->
   * - 75 738 123
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   * 75 738 000 ->
   * - 75 738
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   */
  String AUTH_OKTMO_TREE = "{AUTH_OKTMO_TREE}";
}