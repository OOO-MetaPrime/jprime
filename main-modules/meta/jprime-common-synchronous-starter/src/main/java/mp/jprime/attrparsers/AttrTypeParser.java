package mp.jprime.attrparsers;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Парсер типов атрибутов
 */
public interface AttrTypeParser<T> {
  Logger LOG = LoggerFactory.getLogger(AttrTypeParser.class);

  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  T parse(JPAttr jpAttr, JPAttrData data);

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  T parse(JPAttr jpAttr, Object attrValue);

  /**
   * Раскладываем значение атрибута в поля JPMutableData
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение атрибута
   * @param data      Значения атрибутов
   */
  void fill(JPAttr jpAttr, T attrValue, JPMutableData data);

  /**
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  JPType getJPType();

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  Class<T> getOutputType();
}
