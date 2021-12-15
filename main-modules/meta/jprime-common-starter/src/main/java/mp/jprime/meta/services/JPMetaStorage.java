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
   * @param code Код класса
   * @return Метаописание класса
   * @throws mp.jprime.exceptions.JPClassNotFoundException если класс не найден
   */
  JPClass getJPClassByCodeOrThrow(String code);

  /**
   * Возвращает метаописание класса по множественному коду
   *
   * @param pluralCode Множественный код класса
   * @return Метаописание класса
   * @deprecated Отказ от множественного кодового имени. Рекомендуется использовать обычное кодовое имя
   */
  @Deprecated
  JPClass getJPClassByPluralCode(String pluralCode);

  /**
   * Возвращает метаописание класса по множественному или простому коду
   *
   * @param code Код класса
   * @return Метаописание класса
   * @deprecated Отказ от множественного кодового имени. Рекомендуется использовать обычное кодовое имя
   */
  @Deprecated
  JPClass getJPClassByCodeOrPluralCode(String code);
}
