package mp.jprime.attrparsers;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;

import java.util.Collection;

/**
 * Сервис парсеров типов атрибутов
 */
public interface AttrTypeParserService {
  /**
   * Возвращает связанные атрибуты
   *
   * @param jpAttr Атрибут
   * @return Кодовые имена связанных атрибутов
   */
  Collection<String> getRelatedAttrs(JPAttr jpAttr);

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  <T> T parseTo(JPAttr jpAttr, Object attrValue);


  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  <T> T parseTo(JPAttr jpAttr, JPAttrData data);

  /**
   * Раскладываем значение атрибута в поля JPMutableData
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение атрибута
   * @param data      Значения атрибутов
   */
  void fill(JPAttr jpAttr, Object attrValue, JPMutableData data);
}
