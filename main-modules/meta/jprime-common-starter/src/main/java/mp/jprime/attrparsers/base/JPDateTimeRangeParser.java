package mp.jprime.attrparsers.base;


import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.beans.JsonDateTimeRange;
import mp.jprime.json.beans.JsonRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPDateTimeRange;
import mp.jprime.lang.JPIntegerRange;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * реализация парсера {@link JPIntegerRange}
 */
@Service
public class JPDateTimeRangeParser implements AttrTypeParser<JPDateTimeRange> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPType getJPType() {
    return JPType.TSRANGE;
  }

  @Override
  public Class<JPDateTimeRange> getOutputType() {
    return JPDateTimeRange.class;
  }


  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  @Override
  public JPDateTimeRange parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.TSRANGE) {
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
  public JPDateTimeRange parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JPType.TSRANGE) {
      return null;
    }
    if (attrValue instanceof JPDateTimeRange) {
      return (JPDateTimeRange) attrValue;
    }

    String attrName = jpAttr.getName();

    JPDateTimeRange result = null;
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
        JsonDateTimeRange json = jsonMapper.getObjectMapper().readValue(
            jsonMapper.toString(attrValue),
            JsonDateTimeRange.class
        );
        result = JPDateTimeRange.create(
            json.getLower(),
            json.getUpper(),
            json.isCloseLower(),
            json.isCloseUpper()
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
        JsonDateTimeRange json = jsonMapper.getObjectMapper().readValue(
            (String) attrValue,
            JsonDateTimeRange.class
        );
        result = JPDateTimeRange.create(
            json.getLower(),
            json.getUpper(),
            json.isCloseLower(),
            json.isCloseUpper()
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
  public void fill(JPAttr jpAttr, JPDateTimeRange attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.TSRANGE) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue == null ? null : attrValue.asString());
  }
}