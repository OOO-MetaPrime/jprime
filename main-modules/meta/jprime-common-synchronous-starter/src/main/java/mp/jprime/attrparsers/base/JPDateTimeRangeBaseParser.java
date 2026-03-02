package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.beans.JsonDateTimeRange;
import mp.jprime.json.beans.JsonRange;
import mp.jprime.json.beans.JsonStringRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPDateTimeRange;
import mp.jprime.lang.JPStringRange;
import mp.jprime.meta.JPAttr;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class JPDateTimeRangeBaseParser implements AttrTypeParser<JPDateTimeRange> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public Class<JPDateTimeRange> getOutputType() {
    return JPDateTimeRange.class;
  }

  @Override
  public JPDateTimeRange parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }

    return parse(jpAttr, attrValue);
  }

  @Override
  public JPDateTimeRange parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != getJPType()) {
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
        JsonDateTimeRange json = jsonMapper.toObject(
            JsonDateTimeRange.class,
            jsonMapper.toString(attrValue)
        );
        result = JPDateTimeRange.create(
            json.getLower(),
            json.getUpper(),
            json.isCloseLower(),
            json.isCloseUpper()
        );
      } catch (Exception e) {
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    } else if (attrValue instanceof JPStringRange) {
      try {
        JsonDateTimeRange json = jsonMapper.toObject(
            JsonDateTimeRange.class,
            jsonMapper.toString(JsonStringRange.of((JPStringRange) attrValue))
        );
        result = JPDateTimeRange.create(
            json.getLower(),
            json.getUpper(),
            json.isCloseLower(),
            json.isCloseUpper()
        );
      } catch (Exception e) {
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    } else if (attrValue instanceof String) {
      /*
       * Если строка, мало ли кто-то положил
       */
      try {
        JsonDateTimeRange json = jsonMapper.toObject(
            JsonDateTimeRange.class,
            (String) attrValue
        );
        result = JPDateTimeRange.create(
            json.getLower(),
            json.getUpper(),
            json.isCloseLower(),
            json.isCloseUpper()
        );
      } catch (Exception e) {
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    }
    return result;
  }

  @Override
  public void fill(JPAttr jpAttr, JPDateTimeRange attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue == null ? null : attrValue.asString());
  }
}