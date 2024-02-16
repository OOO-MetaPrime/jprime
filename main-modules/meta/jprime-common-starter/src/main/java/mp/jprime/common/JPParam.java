package mp.jprime.common;

import mp.jprime.meta.beans.JPType;

import java.util.Collection;

/**
 * Описание параметра
 */
public interface JPParam {
  /**
   * кодовое имя метакласса корневого объекта
   */
  String ROOT_OBJECT_CLASS_CODE = "rootObjectClassCode";
  /**
   * идентификатор корневого объекта
   */
  String ROOT_OBJECT_ID = "rootObjectId";
  /**
   * кодовое имя метакласса объекта/ов
   */
  String OBJECT_CLASS_CODE = "objectClassCode";
  /**
   * идентификатор или идентификаторы объектов через запятую, в случае списка
   */
  String OBJECT_IDS = "objectIds";
  /**
   * Фильтр
   */
  String OBJECT_JP_FILTER = "objectJpFilter";

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String getCode();

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Тип
   *
   * @return Тип
   */
  JPType getType();

  /**
   * Возвращает длину
   *
   * @return Длина
   */
  Integer getLength();

  /**
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  String getDescription();

  /**
   * Уникальный qName атрибута
   *
   * @return Уникальный qName атрибута
   */
  String getQName();

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
   * json фильтрации объектов
   *
   * @return json фильтрации объектов
   */
  String getRefFilter();

  /**
   * Разрешен множественный выбор
   *
   * @return Да/Нет
   */
  boolean isMultiple();

  /**
   * Возможность внешнего переопределения параметра. Например, для ввода пользователем
   *
   * @return Да/Нет
   */
  boolean isExternal();

  /**
   * Значение параметра
   *
   * @return Значение
   */
  Object getValue();

  /**
   * Возможные перечислимые значения
   *
   * @return Возможные перечислимые значения
   */
  Collection<JPEnum> getEnums();

  /**
   * Признак клиенткого поиска
   *
   * @return Да/Нет
   */
  boolean isClientSearch();
}
