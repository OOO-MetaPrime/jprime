package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.beans.JsonDateRange;
import mp.jprime.json.beans.JsonRange;
import mp.jprime.json.beans.JsonStringRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPDateRange;
import mp.jprime.lang.JPIntegerRange;
import mp.jprime.lang.JPStringRange;
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
public class JPDateRangeParser implements AttrTypeParser<JPDateRange> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPType getJPType() {
    return JPType.DATE_RANGE;
  }

  @Override
  public Class<JPDateRange> getOutputType() {
    return JPDateRange.class;
  }


  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  @Override
  public JPDateRange parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.DATE_RANGE) {
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
  public JPDateRange parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JPType.DATE_RANGE) {
      return null;
    }
    if (attrValue instanceof JPDateRange) {
      return (JPDateRange) attrValue;
    }

    String attrName = jpAttr.getName();

    JPDateRange result = null;
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
        JsonDateRange json = jsonMapper.toObject(
            JsonDateRange.class,
            jsonMapper.toString(attrValue)
        );
        result = JPDateRange.create(
            json.getLower(),
            json.getUpper()
        );
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение поля " + attrName);
      }
    } else if (attrValue instanceof JPStringRange) {
      try {
        JsonDateRange json = jsonMapper.toObject(
            JsonDateRange.class,
            jsonMapper.toString(JsonStringRange.of((JPStringRange) attrValue))
        );
        result = JPDateRange.create(
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
        JsonDateRange json = jsonMapper.toObject(
            JsonDateRange.class,
            (String) attrValue
        );
        result = JPDateRange.create(
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
  public void fill(JPAttr jpAttr, JPDateRange attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.DATE_RANGE) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue == null ? null : attrValue.asString());
  }
}