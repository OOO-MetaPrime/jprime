package mp.jprime.dataaccess.beans;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPMeta;

import java.util.Objects;

/**
 * Базовая реализация JPObject
 */
public class JPObjectBase implements JPObject {
  private static final JPObjectBase DEFAULT = new JPObjectBase();

  protected JPObjectBase() {

  }

  private JPId jpId; // Идентификатор объекта
  private String jpClassCode; // Мета класса
  private JPData jpData; // Данные объекта
  private JPLinkedData jpLinkedData; // Данные связных объектов

  /**
   * Конструктор
   *
   * @param jpClassCode        Кодовое имя класса
   * @param primaryKeyAttrCode Кодовое имя атрибута-идентификатора
   * @param jpData             Данные объекта
   * @param jpLinkedData       Данные связных объектов
   */
  private void setData(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData) {
    this.jpClassCode = jpClassCode;
    this.jpData = jpData;
    this.jpLinkedData = jpLinkedData;
    // Ключевой атрибут
    Comparable primaryKey = jpData == null || primaryKeyAttrCode == null ? null : (Comparable) jpData.get(primaryKeyAttrCode);
    this.jpId = JPId.get(jpClassCode, primaryKey);
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  @Override
  public JPId getJpId() {
    return jpId;
  }

  /**
   * Данные объекта
   *
   * @return Данные объекта
   */
  @Override
  public JPData getData() {
    return jpData;
  }

  /**
   * Данные связных объектов
   *
   * @return Данные связных объектов
   */
  @Override
  public JPLinkedData getLinkedData() {
    return jpLinkedData;
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  @Override
  public JPObject getLinkedObject(String attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  @Override
  public JPObject getLinkedObject(JPAttr attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  /**
   * Кодовое имя пакета объекта
   *
   * @return Кодовое имя пакета объекта
   */
  @Override
  public String getJpPackage() {
    return getAttrValue(JPMeta.Attr.JPPACKAGE);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  @Override
  public <T> T getAttrValue(JPAttr attr) {
    return (T) (attr != null ? getAttrValue(attr.getCode()) : null);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  @Override
  public <T> T getAttrValue(String attr) {
    return (T) (jpData != null ? jpData.get(attr) : null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPObject jpObject = (JPObject) o;
    return Objects.equals(jpId, jpObject.getJpId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(jpId);
  }

  /**
   * Создает новый объект
   *
   * @param jpClassCode        Класс данных
   * @param primaryKeyAttrCode Атрибут идентификатор
   * @param jpData             Данные объекта
   * @return Новый объект
   */
  public static JPObject newBaseInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData) {
    return newBaseInstance(jpClassCode, primaryKeyAttrCode, jpData, null);
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
  public static JPObject newBaseInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData) {
    return DEFAULT.newInstance(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
  }

  /**
   * Дополняет данные
   *
   * @param jpData JPData
   * @return Новая JPData
   */
  protected JPData appendData(JPData jpData) {
    return jpData;
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
  public JPObject newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData) {
    try {
      JPObjectBase inst = getClass().newInstance();
      jpData = appendData(jpData);
      inst.setData(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
      return inst;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
