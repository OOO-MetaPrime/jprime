package mp.jprime.meta;

import mp.jprime.meta.beans.JPType;

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
   * Теги класса
   *
   * @return Теги класса
   */
  Collection<String> getTags();

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
   * Признак логирования действий над объектами (удаление/создание/изменение)
   *
   * @return Признак логирования действий над объектами (удаление/создание/изменение)
   */
  default boolean useActionLog() {
    return true;
  }

  /**
   * Список атрибутов
   *
   * @return Список атрибутов
   */
  Collection<JPAttr> getAttrs();

  /**
   * Возвращает атрибут по его кодовому имени
   *
   * @param code Кодовое имя атрибута
   * @return JPAttr
   */
  JPAttr getAttr(String code);

  /**
   * Возвращает атрибуты по типу
   *
   * @param jpType Тип атрибута
   * @return Список JPAttr
   */
  Collection<JPAttr> getAttrs(JPType jpType);

  /**
   * Возвращает признак наличия атрибута
   *
   * @param code Кодовое имя атрибута
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

  /**
   * Признак неизменяемой меты
   *
   * @return Да/Нет
   */
  default boolean isImmutable() {
    return Boolean.TRUE;
  }
}