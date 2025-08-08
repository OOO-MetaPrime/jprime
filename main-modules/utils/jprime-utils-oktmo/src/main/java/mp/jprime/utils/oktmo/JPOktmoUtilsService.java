package mp.jprime.utils.oktmo;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Сервис поиска по ОКТМО
 */
public interface JPOktmoUtilsService {
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
     * Поиск по образованиям
     *
     * @return Да/Нет
     */
    boolean isFormationSearch();

    /**
     * Поиск по округам
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
     * @param formationSearch Поиск по образованиям
     * @param districtSearch  Поиск по округам
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
}