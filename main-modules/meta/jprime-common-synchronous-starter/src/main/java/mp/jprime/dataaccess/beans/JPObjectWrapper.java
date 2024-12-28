package mp.jprime.dataaccess.beans;

import mp.jprime.meta.JPAttr;

/**
 * Описание wrap-объекта
 */
public interface JPObjectWrapper extends JPObject {
  /**
   * Базовый объект
   *
   * @return Базовый объект
   */
  JPObject getJPObject();

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  default String getJpClassCode() {
    return getJPObject().getJpClassCode();
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  default JPId getJpId() {
    return getJPObject().getJpId();
  }

  /**
   * Данные объекта
   *
   * @return Данные объекта
   */
  default JPData getData() {
    return getJPObject().getData();
  }

  /**
   * Данные связных объектов
   *
   * @return Данные связных объектов
   */
  default JPLinkedData getLinkedData() {
    return getJPObject().getLinkedData();
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  default JPObject getLinkedObject(String attr) {
    return getJPObject().getLinkedObject(attr);
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  default JPObject getLinkedObject(JPAttr attr) {
    return getJPObject().getLinkedObject(attr);
  }

  /**
   * Кодовое имя пакета объекта
   *
   * @return Кодовое имя пакета объекта
   */
  default String getJpPackage() {
    return getJPObject().getJpPackage();
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  default <T> T getAttrValue(JPAttr attr) {
    return getJPObject().getAttrValue(attr);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  default <T> T getAttrValue(String attr) {
    return getJPObject().getAttrValue(attr);
  }

  /**
   * Создает новый объект
   *
   * @param jpClassCode        Класс данных
   * @param primaryKeyAttrCode Атрибут идентификатор
   * @param jpData             Данные объекта
   * @param jpLinkedData       Данные связных объектов
   * @return Новый объект
   */
  @Override
  default <T extends JPObject> T newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData) {
    return getJPObject().newInstance(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
  }
}
