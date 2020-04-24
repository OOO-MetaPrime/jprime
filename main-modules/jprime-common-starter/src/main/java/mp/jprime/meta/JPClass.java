package mp.jprime.meta;

import java.util.*;

/**
 * метаописание класса
 */
public interface JPClass {

  /**
   * Идентификатор/гуид класса
   *
   * @return Идентификатор/гуид класса
   */
  String getGuid();

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String getCode();

  /**
   * Множественное кодовое имя класса
   *
   * @return Множественное кодовое имя класса
   */
  String getPluralCode();

  /**
   * Название класса
   *
   * @return Название класса
   */
  String getName();

  /**
   * Короткое название класса
   *
   * @return Короткое название класса
   */
  String getShortName();

  /**
   * Описание класса
   *
   * @return Описание класса
   */
  String getDescription();

  /**
   * Уникальный qName класса
   *
   * @return Уникальный qName класса
   */
  String getQName();

  /**
   * Кодовое имя доступа к метаописанию класса
   *
   * @return Кодовое имя доступа к метаописанию класса
   */
  String getJpPackage();

  /**
   * Признак класса только для внутренного доступа
   *
   * @return Признак класса только для внутренного доступа
   */
  boolean isInner();

  /**
   * Список атрибутов
   *
   * @return Список атрибутов
   */
  Collection<JPAttr> getAttrs();

  /**
   * Возвращает атрибут по его кодовому имени
   *
   * @param code Кодовое имя атрибутоа
   * @return JPAttr
   */
  JPAttr getAttr(String code);

  /**
   * Возвращает признак наличия атрибута
   *
   * @param code Кодовое имя атрибутоа
   * @return Да/Нет
   */
  default boolean hasAttr(String code) {
    return getAttr(code) != null;
  }

  /**
   * Возвращает ключевой атрибут класса
   *
   * @return Ключевой атрибут
   */
  JPAttr getPrimaryKeyAttr();
}