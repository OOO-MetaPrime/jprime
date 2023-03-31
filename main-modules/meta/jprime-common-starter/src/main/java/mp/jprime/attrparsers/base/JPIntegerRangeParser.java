package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.beans.JsonIntegerRange;
import mp.jprime.json.beans.JsonRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPIntegerRange;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * реализация парсера {@link mp.jprime.lang.JPIntegerRange}
 */
@Service
public class JPIntegerRangeParser implements AttrTypeParser<JPIntegerRange> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPType getJPType() {
    return JPType.INT_RANGE;
  }

  @Override
  public Class<JPIntegerRange> getOutputType() {
    return JPIntegerRange.class;
  }


  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  @Override
  public JPIntegerRange parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.INT_RANGE) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }

    return parse(jpAttr, attrValue);
  }

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  @Override
  public JPIntegerRange parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JPType.INT_RANGE) {
      return null;
    }
    if (attrValue instanceof JPIntegerRange) {
      return (JPIntegerRange) attrValue;
    }

    String attrName = jpAttr.getName();

    JPIntegerRange result = null;
    if (attrValue instanceof Map || attrValue instanceof JsonRange) {
      /*
       *  Сюда может прийти LinkedHashMap из REST-а
       *  "part": {
       *    "lower": 1,
       *    "upper": 10,
       *    "lowerClose": true,
       *    "upperClose": true
       *  }
       */
      try {
        JsonIntegerRange json = jsonMapper.toObject(
            JsonIntegerRange.class,
            jsonMapper.toString(attrValue)
        );
        result = JPIntegerRange.create(
            json.getLower(),
            json.getUpper()
        );
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение поля " + attrName);
      }
    } else if (attrValue instanceof String) {
      /*
       * Если строка, мало ли кто-то положил
       */
      try {
        JsonIntegerRange json = jsonMapper.toObject(
            JsonIntegerRange.class,
            (String) attrValue
        );
        result = JPIntegerRange.create(
            json.getLower(),
            json.getUpper()
        );
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение поля " + attrName);
      }
    }
    return result;
  }

  /**
   * Раскладываем значение атрибута в поля JPMutableData
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение атрибута
   * @param data      Значения атрибутов
   */
  @Override
  public void fill(JPAttr jpAttr, JPIntegerRange attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.INT_RANGE) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue == null ? null : attrValue.asString());
  }
}