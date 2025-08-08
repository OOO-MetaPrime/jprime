package mp.jprime.security;

import mp.jprime.utils.Oktmo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public interface AuthParams extends ConnectionInfo {
  /**
   * Возвращает логин пользователя
   *
   * @return логин пользователя
   */
  String getUsername();

  /**
   * Возвращает ФИО пользователя
   *
   * @return ФИО пользователя
   */
  String getFio();

  /**
   * Возвращает роли пользователя
   *
   * @return Роли
   */
  Collection<String> getRoles();

  /**
   * Возвращает идентификатор пользователя
   *
   * @return идентификатор пользователя
   */
  String getUserId();

  /**
   * Возвращает идентификатор пользователя
   *
   * @return идентификатор пользователя
   */
  String getUserGuid();

  /**
   * Возвращает основное ОКТМО пользователя
   *
   * @return основное ОКТМО пользователя
   */
  String getOktmo();

  /**
   * Возвращает ОКТМО пользователя
   *
   * @return ОКТМО пользователя
   */
  Collection<String> getOktmoList();

  /**
   * Возвращает префиксы ОКТМО пользователя
   * 75 738 000 -> 75 738
   * 75 000 000 -> 75
   *
   * @return Префиксы ОКТМО пользователя
   */
  default Collection<String> getOktmoPrefixList() {
    return Oktmo.getPrefix(getOktmoList());
  }

  /**
   * Возвращает дерево ОКТМО пользователя
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
   *
   * @return Префиксы ОКТМО пользователя
   */
  default Collection<String> getOktmoTreeList() {
    Collection<String> oktmo = getOktmoList();
    Collection<String> result = new HashSet<>();
    result.addAll(Oktmo.getPrefix(oktmo));
    result.addAll(Oktmo.getHierarchy(oktmo));
    return result;
  }

  /**
   * Возвращает предметные группы пользователя
   *
   * @return предметные группы пользователя
   */
  Collection<Integer> getSubjectGroups();

  /**
   * Возвращает организацию пользователя
   *
   * @return организация пользователя
   */
  String getOrgId();

  /**
   * Возвращает обособленное подразделение пользователя
   *
   * @return обособленное подразделение пользователя
   */
  String getSepDepId();

  /**
   * Возвращает подразделение пользователя
   *
   * @return подразделение пользователя
   */
  String getDepId();

  /**
   * Возвращает сотрудника пользователя
   *
   * @return сотрудника пользователя
   */
  String getEmplId();

  /**
   * Данные ЕСИА
   *
   * @return Данные ЕСИА
   */
  default Esia getEsia() {
    return null;
  }

  interface Esia {
    /**
     * Идентификатор пользователя в ЕСИА
     *
     * @return Идентификатор пользователя в ЕСИА
     */
    String getOid();

    /**
     * Дата рождения пользователя в ЕСИА
     *
     * @return Дата рождения
     */
    LocalDate getBirthdate();

    /**
     * Идентификатор организации пользователя в ЕСИА
     *
     * @return Идентификатор организации пользователя в ЕСИА
     */
    String getOrgOid();

    /**
     * Название организации пользователя в ЕСИА
     *
     * @return Название организации пользователя в ЕСИА
     */
    String getOrgName();

    /**
     * ИНН организации пользователя в ЕСИА
     *
     * @return ИНН организации пользователя в ЕСИА
     */
    String getOrgInn();

    /**
     * ОГРН организации пользователя в ЕСИА
     *
     * @return ОГРН организации пользователя в ЕСИА
     */
    String getOrgOgrn();
  }
}
