package mp.jprime.meta.services;

import mp.jprime.meta.JPClass;

import java.util.Collection;

/**
 * Хранилище метаинформации
 */
public interface JPMetaStorage {
  /**
   * Возвращает метаописания классов
   *
   * @return метаописания классов
   */
  Collection<JPClass> getJPClasses();
  /**
   * Возвращает метаописание класса по коду
   *
   * @param code Код класса
   * @return Метаописание класса
   */
  JPClass getJPClassByCode(String code);
  /**
   * Возвращает метаописание класса по коду
   *
   * @param pluralCode Множественный код класса
   * @return Метаописание класса
   */
  JPClass getJPClassByPluralCode(String pluralCode);
}
