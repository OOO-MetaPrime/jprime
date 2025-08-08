package mp.jprime.nsi;

import mp.jprime.formats.JPStringFormat;

/**
 * Дополнительное поле НСИ справочника
 */
public interface JpNsiProperty {
  /**
   * Кодовое имя
   *
   * @return Кодовое имя
   */
  String getCode();

  /**
   * Тип
   *
   * @return Тип
   */
  JpNsiPropertyType getType();

  /**
   * Тип строкового поля
   *
   * @return Тип строкового поля
   */
  JPStringFormat getStringFormat();

  /**
   * Маска строкового поля
   *
   * @return Маска строкового поля
   */
  String getStringMask();

  /**
   * Признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Название
   *
   * @return Название
   */
  String getName();

  /**
   * Короткое название
   *
   * @return Короткое название
   */
  String getShortName();

  /**
   * Код класса, на который ссылается
   *
   * @return Код класса, на который ссылается
   */
  String getRefJpClassCode();

  /**
   * Код атрибута, на который ссылается
   *
   * @return Код атрибута, на который ссылается
   */
  String getRefJpAttrCode();

  /**
   * Признак основного атрибута
   *
   * @return Да/Нет
   */
  boolean isBasic();
}
