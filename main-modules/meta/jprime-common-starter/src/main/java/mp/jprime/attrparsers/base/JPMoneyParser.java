package mp.jprime.attrparsers.base;

import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.beans.JsonMoney;
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
import java.util.Map;

/**
 * реализация парсера {@link JPMoney}
 */
@Service
public class JPMoneyParser implements AttrTypeParser<JPMoney>, ParserServiceAware {
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


  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  @Override
  public JPMoney parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.MONEY) {
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

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  @Override
  public JPMoney parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JPType.MONEY) {
      return null;
    }

    String attrName = jpAttr.getName();

    JPMoney result = null;
    if (attrValue instanceof Map) {
      /*
       *  Сюда может прийти LinkedHashMap из REST-а
       *  "part": {
       *    "currentCode": "RUR"
       *  }
       */
      try {
        JsonMoney json = jsonMapper.toObject(
            JsonMoney.class,
            jsonMapper.toString(attrValue)
        );
        result = JPMoney.of(
            json.getValue(),
            jpAttr.getMoney().getCurrencyCode()
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
        JsonMoney json = jsonMapper.toObject(
            JsonMoney.class,
            (String) attrValue
        );
        result = JPMoney.of(
            json.getValue(),
            jpAttr.getMoney().getCurrencyCode()
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
  public void fill(JPAttr jpAttr, JPMoney attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JPType.MONEY) {
      return;
    }
    data.put(jpAttr, attrValue == null ? null : attrValue.getNumberStripped());
  }
}