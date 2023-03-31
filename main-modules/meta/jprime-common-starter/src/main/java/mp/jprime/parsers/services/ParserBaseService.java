package mp.jprime.parsers.services;

import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import mp.jprime.parsers.exceptions.JPParserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация парсера типов
 */
@Service
public class ParserBaseService implements ParserService {
  /**
   * Все парсеры
   */
  private final Map<Class, Map<Class, TypeParser>> parsers = new HashMap<>();

  /**
   * Указание ссылок
   */
  @Autowired(required = false)
  private void setAwares(Collection<ParserServiceAware> awares) {
    for (ParserServiceAware aware : awares) {
      aware.setParserService(this);
    }
  }

  /**
   * Кеширование парсеров
   */
  @Autowired(required = false)
  private void setParsers(Collection<TypeParser> parsers) {
    if (parsers == null) {
      return;
    }
    for (TypeParser parser : parsers) {
      this.parsers.computeIfAbsent(parser.getInputType(), x -> new HashMap<>()).put(parser.getOutputType(), parser);
    }
  }

  /**
   * Приводит значение к указанному типу
   *
   * @param to    Выходной тип
   * @param value Значение
   * @return Значение
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T parseTo(Class<T> to, Object value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call ParserService!");
    }
    if (to.isInstance(value)) {
      if (to == String.class) {
        String s = ((String) value).trim();
        value = s.length() == 0 ? null : s;
      }
      return (T) value;
    }
    Map<Class, TypeParser> map = parsers.get(value.getClass());
    if (map == null && value instanceof Collection) {
      map = parsers.get(Collection.class);
    }
    if (map == null) {
      throw new JPParserNotFoundException(String.format("Not found parser from {%s} to {%s} class", value.getClass(), to));
    }
    TypeParser parser = map.get(to);
    if (parser == null) {
      throw new JPParserNotFoundException(String.format("Not found parser from {%s} to {%s} class", value.getClass(), to));
    }
    try {
      return (T) parser.parseObject(value);
    } catch (Exception e) {
      throw new JPParseException("parser.service", e.getMessage());
    }
  }

  /**
   * Возвращает парсер по приводимым типам
   *
   * @param from Входной тип
   * @param to   Выходной тип
   * @return Парсер типов
   */
  @Override
  public TypeParser getParser(Class from, Class to) {
    Map<Class, TypeParser> map = parsers.get(from);
    if (map == null) {
      return null;
    }
    return map.get(to);
  }
}
