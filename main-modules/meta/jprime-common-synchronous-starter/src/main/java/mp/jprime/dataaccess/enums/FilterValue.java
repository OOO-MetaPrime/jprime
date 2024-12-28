package mp.jprime.dataaccess.enums;

/**
 * Константные значения фильтра
 */
public interface FilterValue {
  /**
   * Текущий день
   */
  String CUR_DATE_TEMPLATE = "CUR_DATE";
  String CUR_DATE = "{" + CUR_DATE_TEMPLATE + "}";

  /**
   * Текущее дата+время
   */
  String CUR_DATETIME_TEMPLATE = "CUR_DATETIME";
  String CUR_DATETIME = "{" + CUR_DATETIME_TEMPLATE + "}";

  /**
   * Идентификатор пользователя
   */
  String AUTH_USERID_TEMPLATE = "AUTH_USERID";
  String AUTH_USERID = "{" + AUTH_USERID_TEMPLATE + "}";

  /**
   * Организация пользователя
   */
  String AUTH_ORGID_TEMPLATE = "AUTH_ORGID";
  String AUTH_ORGID = "{" + AUTH_ORGID_TEMPLATE + "}";

  /**
   * Обособленное подразделение пользователя
   */
  String AUTH_SEPDEPID_TEMPLATE = "AUTH_SEPDEPID";
  String AUTH_SEPDEPID = "{" + AUTH_SEPDEPID_TEMPLATE + "}";

  /**
   * Подразделение пользователя
   */
  String AUTH_DEPID_TEMPLATE = "AUTH_DEPID";
  String AUTH_DEPID = "{" + AUTH_DEPID_TEMPLATE + "}";

  /**
   * Штатная единица пользователя
   */
  String AUTH_EMPLID_TEMPLATE = "AUTH_EMPLID";
  String AUTH_EMPLID = "{" + AUTH_EMPLID_TEMPLATE + "}";

  /**
   * Предметные группы пользователя
   */
  String AUTH_SUBJECT_GROUP_TEMPLATE = "AUTH_SUBJECT_GROUP";
  String AUTH_SUBJECT_GROUP = "{" + AUTH_SUBJECT_GROUP_TEMPLATE + "}";

  /**
   * ОКТМО пользователя
   * 75 738 000 -> 75 738
   * 75 000 000 -> 75
   */
  String AUTH_OKTMO_TEMPLATE = "AUTH_OKTMO";
  String AUTH_OKTMO = "{" + AUTH_OKTMO_TEMPLATE + "}";

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
  String AUTH_OKTMO_TREE_TEMPLATE = "AUTH_OKTMO_TREE";
  String AUTH_OKTMO_TREE = "{" + AUTH_OKTMO_TREE_TEMPLATE + "}";
}