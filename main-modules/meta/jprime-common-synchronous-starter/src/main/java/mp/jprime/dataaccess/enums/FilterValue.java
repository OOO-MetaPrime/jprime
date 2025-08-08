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
   * Текущее время
   */
  String CUR_TIME_TEMPLATE = "CUR_TIME";
  String CUR_TIME = "{" + CUR_TIME_TEMPLATE + "}";


  /**
   * 1 число текущего месяца
   */
  String CUR_MONTH_BEGIN_DATE_TEMPLATE = "CUR_MONTH_BEGIN_DATE";
  String CUR_MONTH_BEGIN_DATE = "{" + CUR_MONTH_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число текущего месяца
   */
  String CUR_MONTH_END_DATE_TEMPLATE = "CUR_MONTH_END_DATE";
  String CUR_MONTH_END_DATE = "{" + CUR_MONTH_END_DATE_TEMPLATE + "}";

  /**
   * 1 число текущего года
   */
  String CUR_YEAR_BEGIN_DATE_TEMPLATE = "CUR_YEAR_BEGIN_DATE";
  String CUR_YEAR_BEGIN_DATE = "{" + CUR_YEAR_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число текущего года
   */
  String CUR_YEAR_END_DATE_TEMPLATE = "CUR_YEAR_END_DATE";
  String CUR_YEAR_END_DATE = "{" + CUR_YEAR_END_DATE_TEMPLATE + "}";

  /**
   * Предыдущий день
   */
  String PREV_DATE_TEMPLATE = "PREV_DATE";
  String PREV_DATE = "{" + PREV_DATE_TEMPLATE + "}";

  /**
   * 1 число предыдущего месяца
   */
  String PREV_MONTH_BEGIN_DATE_TEMPLATE = "PREV_MONTH_BEGIN_DATE";
  String PREV_MONTH_BEGIN_DATE = "{" + PREV_MONTH_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число предыдущего месяца
   */
  String PREV_MONTH_END_DATE_TEMPLATE = "PREV_MONTH_END_DATE";
  String PREV_MONTH_END_DATE = "{" + PREV_MONTH_END_DATE_TEMPLATE + "}";

  /**
   * 1 число предыдущего года
   */
  String PREV_YEAR_BEGIN_DATE_TEMPLATE = "PREV_YEAR_BEGIN_DATE";
  String PREV_YEAR_BEGIN_DATE = "{" + PREV_YEAR_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число предыдущего года
   */
  String PREV_YEAR_END_DATE_TEMPLATE = "PREV_YEAR_END_DATE";
  String PREV_YEAR_END_DATE = "{" + PREV_YEAR_END_DATE_TEMPLATE + "}";

  /**
   * Следующий день
   */
  String NEXT_DATE_TEMPLATE = "NEXT_DATE";
  String NEXT_DATE = "{" + NEXT_DATE_TEMPLATE + "}";

  /**
   * 1 число следующего месяца
   */
  String NEXT_MONTH_BEGIN_DATE_TEMPLATE = "NEXT_MONTH_BEGIN_DATE";
  String NEXT_MONTH_BEGIN_DATE = "{" + NEXT_MONTH_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число следующего месяца
   */
  String NEXT_MONTH_END_DATE_TEMPLATE = "NEXT_MONTH_END_DATE";
  String NEXT_MONTH_END_DATE = "{" + NEXT_MONTH_END_DATE_TEMPLATE + "}";

  /**
   * 1 число следующего года
   */
  String NEXT_YEAR_BEGIN_DATE_TEMPLATE = "NEXT_YEAR_BEGIN_DATE";
  String NEXT_YEAR_BEGIN_DATE = "{" + NEXT_YEAR_BEGIN_DATE_TEMPLATE + "}";

  /**
   * Последнее число следующего года
   */
  String NEXT_YEAR_END_DATE_TEMPLATE = "NEXT_YEAR_END_DATE";
  String NEXT_YEAR_END_DATE = "{" + NEXT_YEAR_END_DATE_TEMPLATE + "}";

  /**
   * Текущий месяц
   */
  String CUR_MONTH_TEMPLATE = "CUR_MONTH";
  String CUR_MONTH = "{" + CUR_MONTH_TEMPLATE + "}";

  /**
   * Текущий год
   */
  String CUR_YEAR_TEMPLATE = "CUR_YEAR";
  String CUR_YEAR = "{" + CUR_YEAR_TEMPLATE + "}";

  /**
   * Предыдущий месяц
   */
  String PREV_MONTH_TEMPLATE = "PREV_MONTH";
  String PREV_MONTH = "{" + PREV_MONTH_TEMPLATE + "}";

  /**
   * Предыдущий год
   */
  String PREV_YEAR_TEMPLATE = "PREV_YEAR";
  String PREV_YEAR = "{" + PREV_YEAR_TEMPLATE + "}";

  /**
   * Следующий месяц
   */
  String NEXT_MONTH_TEMPLATE = "NEXT_MONTH";
  String NEXT_MONTH = "{" + NEXT_MONTH_TEMPLATE + "}";

  /**
   * Следующий год
   */
  String NEXT_YEAR_TEMPLATE = "NEXT_YEAR";
  String NEXT_YEAR = "{" + NEXT_YEAR_TEMPLATE + "}";

  /**
   * Идентификатор пользователя
   */
  String AUTH_USERID_TEMPLATE = "AUTH_USERID";
  String AUTH_USERID = "{" + AUTH_USERID_TEMPLATE + "}";

  /**
   * Название пользователя
   */
  String AUTH_USERNAME_TEMPLATE = "AUTH_USERNAME";
  String AUTH_USERNAME = "{" + AUTH_USERNAME_TEMPLATE + "}";

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
   * признак ФЛ
   */
  String AUTH_ESIA_IS_INDIVIDUAL_TEMPLATE = "AUTH_ESIA_IS_INDIVIDUAL";
  String AUTH_ESIA_IS_INDIVIDUAL = "{" + AUTH_ESIA_IS_INDIVIDUAL_TEMPLATE + "}";

  /**
   * признак ФЛ
   */
  String AUTH_ESIA_IS_LEGALENTITY_TEMPLATE = "AUTH_ESIA_IS_LEGALENTITY";
  String AUTH_ESIA_IS_LEGALENTITY = "{" + AUTH_ESIA_IS_LEGALENTITY_TEMPLATE + "}";

  /**
   * ЕСИА идентификатор организации пользователя
   */
  String AUTH_ESIA_ORGID_TEMPLATE = "AUTH_ESIA_ORGID";
  String AUTH_ESIA_ORGID = "{" + AUTH_ESIA_ORGID_TEMPLATE + "}";

  /**
   * ИНН ЕСИА организации пользователя
   */
  String AUTH_ESIA_ORGINN_TEMPLATE = "AUTH_ESIA_ORGINN";
  String AUTH_ESIA_ORGINN = "{" + AUTH_ESIA_ORGINN_TEMPLATE + "}";

  /**
   * Название ЕСИА организации пользователя
   */
  String AUTH_ESIA_ORGNAME_TEMPLATE = "AUTH_ESIA_ORGNAME";
  String AUTH_ESIA_ORGNAME = "{" + AUTH_ESIA_ORGNAME_TEMPLATE + "}";

  /**
   * ОГРН ЕСИА организации пользователя
   */
  String AUTH_ESIA_ORGOGRN_TEMPLATE = "AUTH_ESIA_ORGOGRN";
  String AUTH_ESIA_ORGOGRN = "{" + AUTH_ESIA_ORGOGRN_TEMPLATE + "}";

  /**
   * ЕСИА идентификатор пользователя
   */
  String AUTH_ESIA_USERID_TEMPLATE = "AUTH_ESIA_USERID";
  String AUTH_ESIA_USERID = "{" + AUTH_ESIA_USERID_TEMPLATE + "}";

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