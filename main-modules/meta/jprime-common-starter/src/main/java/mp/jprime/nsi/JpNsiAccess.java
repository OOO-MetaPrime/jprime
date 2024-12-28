package mp.jprime.nsi;

import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Доступ к данным НСИ
 */
public interface JpNsiAccess {
  JpNsiAccess EMPTY = new JpNsiAccess() {
    @Override
    public String getOktmo() {
      return null;
    }

    @Override
    public Collection<Integer> getSubjectGroups() {
      return Collections.emptySet();
    }

    @Override
    public boolean accept(AuthInfo authInfo) {
      return authInfo != null;
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  };

  /**
   * Доступ. ОКТМО.
   *
   * @return Доступ. ОКТМО.
   */
  String getOktmo();

  /**
   * Доступ. Предметные группы.
   *
   * @return Доступ. Предметные группы.
   */
  Collection<Integer> getSubjectGroups();

  /**
   * Проверка доступа
   *
   * @param authInfo AuthInfo
   * @return Да/Нет
   */
  boolean accept(AuthInfo authInfo);

  /**
   * Признак отсутствия настроек
   *
   * @return Да/Нет
   */
  boolean isEmpty();
}
