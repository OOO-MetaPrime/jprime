package mp.jprime.meta;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPOrder;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.formats.JPStringFormat;

import java.util.Collection;
import java.util.List;

/**
 * Метаописание свойства
 */
public interface JPProperty {
  /**
   * Кодовое имя свойства
   *
   * @return Кодовое имя свойства
   */
  String getCode();

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  JPPropertyType getType();

  /**
   * Возвращает описание денежного типа
   *
   * @return Описание денежного типа
   */
  JPMoney getMoney();

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
   * Возвращает длину
   *
   * @return Длина
   */
  Integer getLength();

  /**
   * Значение по умолчанию
   *
   * @return Значение по умолчанию
   */
  Object getDefValue();

  /**
   * Название свойства
   *
   * @return Название свойства
   */
  String getName();

  /**
   * Уникальный qName свойства
   *
   * @return Уникальный qName свойства
   */
  String getQName();

  /**
   * Код класса, на который ссылается
   *
   * @return Код класса, на который ссылается
   */
  String getRefJpClass();

  /**
   * Код атрибута, на который ссылается
   *
   * @return Код атрибута, на который ссылается
   */
  String getRefJpAttr();

  /**
   * Условие, на объекты класса
   *
   * @return Условие
   */
  Filter getFilter();

  /**
   * Порядок сортировки
   *
   * @return Порядок сортировки
   */
  List<JPOrder> getOrders();

  /**
   * Возвращает перечислимые значения
   *
   * @return Перечислимые значения
   */
  List<JPEnum> getEnums();

  /**
   * Список вложенных свойств
   *
   * @return список вложенных свойств
   */
  Collection<JPProperty> getJpProps();
}
