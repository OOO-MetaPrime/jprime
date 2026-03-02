package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPSimpleFraction;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * реализация парсера {@link JPType#SIMPLEFRACTION}
 */
@Service
public final class JPSimpleFractionParser implements AttrTypeParser<JPSimpleFraction>, ParserServiceAware {
  private ParserService parserService;
  private JPJsonMapper jsonMapper;

  @Override
  public void setParserService(ParserService parserService) {
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

  @Override
  public JPSimpleFraction parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null || attrValue instanceof Number) {
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
      Integer integer = intAttr != null ? parserService.parseTo(Integer.class, data.get(intAttr)) : null;
      Integer numerator = parserService.parseTo(Integer.class, data.get(jpAttr));
      Integer denominator = denomAttr != null ? parserService.parseTo(Integer.class, data.get(denomAttr)) : null;

      if (integer == null && numerator == null && denominator == null) {
        return null;
      }
      return JPSimpleFraction.of(
          integer != null ? integer : 0,
          numerator != null ? numerator : 0,
          denominator != null ? denominator : 1);
    } else {
      return parse(jpAttr, attrValue);
    }
  }

  @Override
  public JPSimpleFraction parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != getJPType()) {
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
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    } else if (attrValue instanceof String s) {
      /*
       * Если строка, мало ли кто-то положил
       */
      try {
        result = jsonMapper.toObject(JPSimpleFraction.class, s);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    }
    return result;
  }

  @Override
  public void fill(JPAttr jpAttr, JPSimpleFraction attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
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
    int integer = attrValue.getInteger();
    int numerator = attrValue.getNumerator();
    int denominator = attrValue.getDenominator();

    if (intAttr != null) {
      if (integer == 0) {
        data.put(intAttr, integer);
        data.put(jpAttr, numerator != 0 ? sign * numerator : null);
      } else {
        data.put(intAttr, sign * integer);
        data.put(jpAttr, numerator != 0 ? numerator : null);
      }
    } else {
      data.put(jpAttr, sign * (integer * denominator + numerator));
    }

    if (denomAttr != null) {
      data.put(denomAttr, numerator != 0 ? denominator : null);
    }
  }
}
