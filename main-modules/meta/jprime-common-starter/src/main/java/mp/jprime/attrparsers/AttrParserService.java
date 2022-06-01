package mp.jprime.attrparsers;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import mp.jprime.parsers.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Парсер значений атрибутов
 */
@Service
public final class AttrParserService {
  /**
   * Все парсеры
   */
  private Map<JPType, AttrTypeParser> parsers = new HashMap<>();
  /**
   * Парсер типов
   */
  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }


  /**
   * Конструктор
   */
  @Autowired(required = false)
  private void setAttrParsers(Collection<AttrTypeParser> parsers) {
    if (parsers == null) {
      return;
    }
    for (AttrTypeParser parser : parsers) {
      this.parsers.put(parser.getJPType(), parser);
    }
  }

  /**
   * Раскладываем значение атрибута в поля JPMutableData
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение атрибута
   * @param data      Значения атрибутов
   */
  public void fill(JPAttr jpAttr, Object attrValue, JPMutableData data) {
    if (jpAttr == null || data == null) {
      return;
    }
    JPType valueType = jpAttr.getValueType();
    Class valueClass = valueType != null ? valueType.getJavaClass() : null;
    if (valueClass == null) {
      return;
    }
    AttrTypeParser parser = parsers.get(jpAttr.getValueType());
    if (parser != null && attrValue != null && parser.getOutputType() == attrValue.getClass()) {
      parser.fill(jpAttr, attrValue, data);
    } else if (!data.containsAttr(jpAttr)) {
      // Если парсера нет, приводим значение "as is"
      data.putIfAbsent(jpAttr, parserService.parseTo(valueClass, attrValue));
    }
  }

  /**
   * Форматирование значения на основе данных в JPAttrData
   *
   * @param jpAttr Атрибут
   * @param data   Значения атрибутов
   * @return Данные в выходном формате
   */
  public <T> T parseTo(JPAttr jpAttr, JPAttrData data) {
    if (jpAttr == null || data == null) {
      return null;
    }
    JPType valueType = jpAttr.getValueType();
    Class valueClass = valueType != null ? valueType.getJavaClass() : null;
    if (valueClass == null) {
      return null;
    }
    Object value = data.get(jpAttr);
    if (value == null) {
      return null;
    }
    AttrTypeParser parser = parsers.get(jpAttr.getValueType());
    if (parser != null) {
      if (value.getClass() == parser.getOutputType()) {
        return (T) value;
      }
      return (T) parser.parse(jpAttr, data);
    } else {
      // Если парсера нет, приводим значение "as is"
      return (T) parserService.parseTo(valueClass, value);
    }
  }

  /**
   * Форматирование значения
   *
   * @param jpAttr    Атрибут
   * @param attrValue Значение
   * @return Данные в выходном формате
   */
  public <T> T parseTo(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || attrValue == null) {
      return null;
    }
    JPType valueType = jpAttr.getValueType();
    Class valueClass = valueType != null ? valueType.getJavaClass() : null;
    if (valueClass == null) {
      return null;
    }
    AttrTypeParser parser = parsers.get(jpAttr.getValueType());
    if (parser != null) {
      return (T) parser.parse(jpAttr, attrValue);
    } else {
      // Если парсера нет, приводим значение "as is"
      return (T) parserService.parseTo(valueClass, attrValue);
    }
  }
}
