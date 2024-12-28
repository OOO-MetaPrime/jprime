package mp.jprime.meta;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Фильтр меты
 */
public interface JPMetaFilter {
  /**
   * Возвращает список классов, соответствующих фильтрации
   *
   * @return список метаописаний
   */
  Collection<JPClass> getList();

  /**
   * Возвращает список классов, соответствующих настройкам доступа и фильтрации
   *
   * @param auth AuthInfo
   * @return список метаописаний
   */
  Collection<JPClass> getList(AuthInfo auth);

  /**
   * Возвращает класс, если он соответствует настройкам доступа и фильтрации
   *
   * @param jpClassCode Код класса
   * @param auth        AuthInfo
   * @return Метаописание класса
   */
  JPClass get(String jpClassCode, AuthInfo auth);

  /**
   * Возвращает класс, если он соответствует настройкам доступа и фильтрации для анонимного доступа
   *
   * @param jpClassCode Код класса
   * @return Метаописание класса
   */
  JPClass getAnonymous(String jpClassCode);
}
