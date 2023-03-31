package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPSimpleFraction;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * реализация парсера JPSimpleFraction
 */
@Service
public final class JPSimpleFractionParser implements AttrTypeParser<JPSimpleFraction> {
  private ParserService parserService;
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPType getJPType() {
    return JPType.SIMPLEFRACTION;
  }

  @Override
  public Class<JPSimpleFraction> getOutputType() {
    return JPSimpleFraction.class;
  }

  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  @Override
  public JPSimpleFraction parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.SIMPLEFRACTION) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }

    JPSimpleFraction result;
    if (attrValue instanceof Number) {
      /*
       * Полноценный числитель, как базовое значение
       */
      mp.jprime.meta.JPSimpleFraction fraction = jpAttr.getSimpleFraction();
      String intAttr = fraction != null ? fraction.getIntegerAttrCode() : null;
      String denomAttr = fraction != null ? fraction.getDenominatorAttrCode() : null;

      if (!data.containsAttr(jpAttr) ||
          (intAttr != null && !data.containsAttr(intAttr)) ||
          (denomAttr != null && !data.containsAttr(denomAttr))) {
        return null;
      }
      result = JPSimpleFraction.of(
          intAttr != null ? parserService.parseTo(Integer.class, data.get(intAttr)) : 0,
          parserService.parseTo(Integer.class, data.get(jpAttr)),
          denomAttr != null ? parserService.parseTo(Integer.class, data.get(denomAttr)) : 1
      );
    } else {
      result = parse(jpAttr, attrValue);
    }
    return result;
  }

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  @Override
  public JPSimpleFraction parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JPType.SIMPLEFRACTION) {
      return null;
    }

    String attrName = jpAttr.getName();

    JPSimpleFraction result = null;
    if (attrValue instanceof Map) {
      /*
       *  Сюда может прийти LinkedHashMap из REST-а
       *  "part": {
       *    "positive": true,
       *    "integer": 2,
       *    "numerator": 1,
       *    "denominator": 2
       *  }
       */
      try {
        result = jsonMapper.toObject(
            JPSimpleFraction.class,
            jsonMapper.toString(attrValue)
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
        result = jsonMapper.toObject(
            JPSimpleFraction.class,
            (String) attrValue
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
  public void fill(JPAttr jpAttr, JPSimpleFraction attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.SIMPLEFRACTION) {
      return;
    }

    mp.jprime.meta.JPSimpleFraction fraction = jpAttr.getSimpleFraction();
    String intAttr = fraction != null ? fraction.getIntegerAttrCode() : null;
    String denomAttr = fraction != null ? fraction.getDenominatorAttrCode() : null;
    if (attrValue == null) {
      data.put(jpAttr, null);
      data.put(intAttr, null);
      data.put(denomAttr, null);
      return;
    }
    int sign = attrValue.isPositive() ? 1 : -1;
    if (intAttr != null) {
      data.put(intAttr, sign * attrValue.getInteger());
      data.put(jpAttr, attrValue.getNumerator());
    } else {
      data.put(jpAttr, sign * (attrValue.getInteger() * attrValue.getDenominator() + attrValue.getNumerator()));
    }
    if (denomAttr != null) {
      data.put(denomAttr, attrValue.getDenominator());
    }
  }
}
