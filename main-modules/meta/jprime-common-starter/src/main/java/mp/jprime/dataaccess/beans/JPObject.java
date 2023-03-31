package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

/**
 * Описание объекта
 */
public interface JPObject {
  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String getJpClassCode();

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  JPId getJpId();

  /**
   * Данные объекта
   *
   * @return Данные объекта
   */
  JPData getData();

  /**
   * Данные связных объектов
   *
   * @return Данные связных объектов
   */
  JPLinkedData getLinkedData();

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  JPObject getLinkedObject(String attr);

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  JPObject getLinkedObject(JPAttr attr);

  /**
   * Кодовое имя пакета объекта
   *
   * @return Кодовое имя пакета объекта
   */
  String getJpPackage();

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  <T> T getAttrValue(JPAttr attr);

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  <T> T getAttrValue(String attr);

  /**
   * Создает новый объект
   *
   * @param jpClassCode        Класс данных
   * @param primaryKeyAttrCode Атрибут идентификатор
   * @param jpData             Данные объекта
   * @param jpLinkedData       Данные связных объектов
   * @return Новый объект
   */
  <T extends JPObject> T newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData);

  /**
   * Создает новый объект
   *
   * @param jpClassCode        Класс данных
   * @param primaryKeyAttrCode Атрибут идентификатор
   * @param jpData             Данные объекта
   * @return Новый объект
   */
  default <T extends JPObject> T newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData) {
    return newInstance(jpClassCode, primaryKeyAttrCode, jpData, null);
  }
}
