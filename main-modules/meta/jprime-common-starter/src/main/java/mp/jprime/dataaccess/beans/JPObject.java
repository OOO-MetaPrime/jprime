package mp.jprime.dataaccess.beans;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class JPObject {
  private static final Logger LOG = LoggerFactory.getLogger(JPObject.class);

  protected JPObject() {

  }

  private JPId jpId; // Идентификатор объекта
  private String jpClassCode; // Мета класса
  private JPData jpData; // Данные объекта
  private JPLinkedData jpLinkedData; // Данные связных объектов

  /**
   * Конструктор
   *
   * @param jpClass      Кодовое имя класса
   * @param jpData       Данные объекта
   * @param jpLinkedData Данные связных объектов
   */
  private void setData(JPClass jpClass, JPData jpData, JPLinkedData jpLinkedData) {
    this.jpClassCode = jpClass.getCode();
    this.jpData = jpData;
    this.jpLinkedData = jpLinkedData;
    // Ключевой атрибут
    JPAttr primaryKeyAttr = jpClass.getPrimaryKeyAttr();
    Comparable primaryKey = jpData == null || primaryKeyAttr == null ? null : (Comparable) jpData.get(primaryKeyAttr.getCode());
    this.jpId = JPId.get(jpClass.getCode(), primaryKey);
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClassCode() {
    return jpClassCode;
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  public JPId getJpId() {
    return jpId;
  }

  /**
   * Данные объекта
   *
   * @return Данные объекта
   */
  public JPData getData() {
    return jpData;
  }

  /**
   * Данные связных объектов
   *
   * @return Данные связных объектов
   */
  public JPLinkedData getLinkedData() {
    return jpLinkedData;
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  public JPObject getLinkedObject(String attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  /**
   * Данные связного объекта
   *
   * @return Данные связного объекта
   */
  public JPObject getLinkedObject(JPAttr attr) {
    return jpLinkedData != null ? jpLinkedData.get(attr) : null;
  }

  /**
   * Кодовое имя пакета объекта
   *
   * @return Кодовое имя пакета объекта
   */
  public String getJpPackage() {
    return getAttrValue(JPMeta.Attr.JPPACKAGE);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public <T> T getAttrValue(JPAttr attr) {
    return (T) (attr != null ? getAttrValue(attr.getCode()) : null);
  }

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  public <T> T getAttrValue(String attr) {
    return (T) (jpData != null ? jpData.get(attr) : null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPObject jpObject = (JPObject) o;
    return Objects.equals(jpId, jpObject.jpId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jpId);
  }

  /**
   * Построитель JPObject
   *
   * @param jpClass Класс объекта
   * @return Builder
   */
  public static Builder newBuilder(JPClass jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель JPObject
   */
  public static final class Builder {
    private final JPClass jpClass;
    private Class<?> objectClass;
    private JPData jpData;
    private JPLinkedData jpLinkedData;

    private Builder(JPClass jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPData
     *
     * @return JPData
     */
    public JPObject build() {
      try {
        JPObject inst;
        if (objectClass == null) {
          inst = JPObject.class.newInstance();
        } else {
          inst = (JPObject) objectClass.newInstance();
        }
        inst.setData(jpClass, jpData, jpLinkedData);
        return inst;
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }

    /**
     * Класс данных
     *
     * @param objectClass Класс данных
     * @return Builder
     */
    public Builder objectClass(Class<?> objectClass) {
      this.objectClass = objectClass;
      return this;
    }

    /**
     * Данные объекта
     *
     * @param jpData Данные объекта
     * @return Builder
     */
    public Builder jpData(JPData jpData) {
      this.jpData = jpData;
      return this;
    }

    /**
     * Данные связных объектов
     *
     * @param jpLinkedData Данные связных объектов
     * @return Builder
     */
    public Builder jpLinkedData(JPLinkedData jpLinkedData) {
      this.jpLinkedData = jpLinkedData;
      return this;
    }
  }
}
