package mp.jprime.utils.oktmo;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Сервис поиска по ОКТМО
 */
public interface JpOktmoUtilsService {
  /**
   * Возвращает описание по переданным кодам ОКТМО
   *
   * @param oktmoList Коды ОКТМО
   * @return Список ОКТМО
   */
  Collection<Oktmo> get(Collection<String> oktmoList);

  /**
   * Поиск ОКТМО по параметрам
   *
   * @param query  Поисковая строка
   * @param limit  Количество объектов в выборке
   * @param params Настройки поиска ОКТМО
   * @return Список ОКТМО
   */
  Collection<Oktmo> search(String query, Integer limit, SearchParams params);

  /**
   * Возвращает описание по переданным кодам групп ОКТМО
   *
   * @param groupList  Коды групп ОКТМО
   * @param prefixMode Признак возврата значимых префиксов ОКТМО
   * @return Список групп ОКТМО
   */
  Collection<Group> getGroup(Collection<String> groupList, boolean prefixMode);

  /**
   * Поиск групп ОКТМО по параметрам
   *
   * @param query      Поисковая строка
   * @param limit      Количество объектов в выборке
   * @param prefixMode Признак возврата значимых префиксов ОКТМО
   * @param params     Настройки поиска групп ОКТМО
   * @return Список ОКТМО
   */
  Collection<Group> groupSearch(String query, Integer limit, boolean prefixMode, GroupSearchParams params);

  /**
   * Настройки поиска ОКТМО
   */
  interface SearchParams {
    /**
     * Поиск по субъектам
     *
     * @return Да/Нет
     */
    boolean isSubjectSearch();

    /**
     * Поиск по муниципальному уровню
     *
     * @return Да/Нет
     */
    boolean isFormationSearch();

    /**
     * Поиск по поселенческому уровню
     *
     * @return Да/Нет
     */
    boolean isDistrictSearch();

    /**
     * Поиск с учетом указанных ОКТМО
     *
     * @return Список ОКТМО
     */
    Collection<String> getOktmoSearch();

    /**
     * Поиск с учетом ОКТМО пользователя
     *
     * @return Да/Нет
     */
    boolean isAuthSearch();

    /**
     * Данные пользователя
     *
     * @return AuthInfo
     */
    AuthInfo getAuth();

    /**
     * Создание SearchParams
     *
     * @param subjectSearch   Поиск по субъектам
     * @param formationSearch Поиск по муниципальному уровню
     * @param districtSearch  Поиск по поселенческому уровню
     * @param oktmoSearch     Поиск с учетом указанных ОКТМО
     * @param authSearch      Поиск с учетом ОКТМО пользователя
     * @param auth            Данные пользователя
     * @return SearchParams
     */
    static SearchParams of(boolean subjectSearch, boolean formationSearch, boolean districtSearch,
                           Collection<String> oktmoSearch, boolean authSearch, AuthInfo auth) {
      return new SearchParamsRecord(subjectSearch, formationSearch, districtSearch, oktmoSearch, authSearch, auth);
    }


    record SearchParamsRecord(boolean isSubjectSearch, boolean isFormationSearch, boolean isDistrictSearch,
                              Collection<String> getOktmoSearch, boolean isAuthSearch,
                              AuthInfo getAuth) implements SearchParams {

    }
  }

  /**
   * Настройки поиска группы ОКТМО
   */
  interface GroupSearchParams {
    /**
     * Поиск с учетом указанных ОКТМО
     *
     * @return Список ОКТМО
     */
    Collection<String> getOktmoSearch();

    /**
     * Поиск с учетом ОКТМО пользователя
     *
     * @return Да/Нет
     */
    boolean isAuthSearch();

    /**
     * Данные пользователя
     *
     * @return AuthInfo
     */
    AuthInfo getAuth();

    /**
     * Создание GroupSearchParams
     *
     * @param oktmoSearch Поиск с учетом указанных ОКТМО
     * @param authSearch  Поиск с учетом ОКТМО пользователя
     * @param auth        Данные пользователя
     * @return SearchParams
     */
    static GroupSearchParams of(Collection<String> oktmoSearch, boolean authSearch, AuthInfo auth) {
      return new GroupSearchParamsRecord(oktmoSearch, authSearch, auth);
    }


    record GroupSearchParamsRecord(Collection<String> getOktmoSearch, boolean isAuthSearch,
                                   AuthInfo getAuth) implements GroupSearchParams {

    }
  }

  /**
   * ОКТМО
   */
  interface Oktmo {
    /**
     * Код ОКТМО
     *
     * @return Код ОКТМО
     */
    String getCode();

    /**
     * Название ОКТМО
     *
     * @return Название ОКТМО
     */
    String getName();

    /**
     * Создание Oktmo
     *
     * @param code Код ОКТМО
     * @param name Название ОКТМО
     * @return Oktmo
     */
    static Oktmo of(String code, String name) {
      return new OktmoRecord(code, name);
    }

    record OktmoRecord(String getCode, String getName) implements Oktmo {

    }
  }

  /**
   * Группа ОКТМО
   */
  interface Group {
    /**
     * Код группы ОКТМО
     *
     * @return Код группы ОКТМО
     */
    String getCode();

    /**
     * Название группы ОКТМО
     *
     * @return Название группы ОКТМО
     */
    String getName();

    /**
     * Список ОКТМО, входящих в группу
     *
     * @return Список ОКТМО
     */
    Collection<String> getOktmo();

    /**
     * Создание группы ОКТМО
     *
     * @param code  Код группы ОКТМО
     * @param name  Название группы ОКТМО
     * @param oktmo Список ОКТМО, входящих в группу
     * @return группа ОКТМО
     */
    static Group of(String code, String name, Collection<String> oktmo) {
      return new GroupRecord(code, name, oktmo);
    }

    record GroupRecord(String getCode, String getName, Collection<String> getOktmo) implements Group {

    }
  }
}