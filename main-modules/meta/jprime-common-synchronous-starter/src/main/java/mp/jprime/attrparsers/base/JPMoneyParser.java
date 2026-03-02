package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPMoney;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * реализация парсера {@link JPType#MONEY}
 */
@Service
public final class JPMoneyParser implements AttrTypeParser<JPMoney>, ParserServiceAware {
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
    return JPType.MONEY;
  }

  @Override
  public Class<JPMoney> getOutputType() {
    return JPMoney.class;
  }

  @Override
  public JPMoney parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }

    JPMoney result;
    if (attrValue instanceof Number) {
      mp.jprime.meta.JPMoney jpMoney = jpAttr.getMoney();

      if (!data.containsAttr(jpAttr)) {
        return null;
      }
      result = JPMoney.of(
          parserService.parseTo(BigDecimal.class, attrValue),
          jpMoney.getCurrencyCode()
      );
    } else {
      result = parse(jpAttr, attrValue);
    }
    return result;
  }

  @Override
  public JPMoney parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return null;
    }

    String attrName = jpAttr.getName();

    JPMoney result = null;
    if (attrValue instanceof BigDecimal value) {
      result = JPMoney.of(value, jpAttr.getMoney().getCurrencyCode());
    } else if (attrValue instanceof String value) {
      /*
       * Если строка, мало ли кто-то положил
       */
      try {
        result = JPMoney.of(jsonMapper.toObject(BigDecimal.class, value), jpAttr.getMoney().getCurrencyCode());
      } catch (Exception e) {
        throw new JPParseException("valueparseerror." + attrName, "Неверно указано значение " + attrName);
      }
    }
    return result;
  }

  @Override
  public void fill(JPAttr jpAttr, JPMoney attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != getJPType()) {
      return;
    }
    data.put(jpAttr, attrValue == null ? null : attrValue.getNumberStripped());
  }
}