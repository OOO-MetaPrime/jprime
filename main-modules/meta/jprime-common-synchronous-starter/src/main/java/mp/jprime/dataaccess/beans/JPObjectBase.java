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

  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  @Override
  public JPId getJpId() {
    return jpId;
  }

  @Override
  public JPData getData() {
    return jpData;
  }

  @Override
  public JPLinkedData getLinkedData() {
    return jpLinkedData;
  }

  @Override
  public JPObject getLinkedObject(String attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  @Override
  public JPObject getLinkedObject(JPAttr attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  @Override
  public String getJpPackage() {
    return getAttrValue(JPMeta.Attr.JPPACKAGE);
  }

  @Override
  public <T> T getAttrValue(JPAttr attr) {
    return (T) (attr != null ? getAttrValue(attr.getCode()) : null);
  }

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

  @Override
  public JPObject newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData, JPLinkedData jpLinkedData) {
    try {
      JPObjectBase inst = getClass().getDeclaredConstructor().newInstance();
      jpData = appendData(jpData);
      inst.setData(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
      return inst;
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
