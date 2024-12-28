package mp.jprime.nsi;

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
