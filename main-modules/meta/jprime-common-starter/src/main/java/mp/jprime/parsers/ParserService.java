package mp.jprime.parsers;

import mp.jprime.parsers.exceptions.JPParserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Парсер типов
 */
@Service
@SuppressWarnings("rawtypes")
public final class ParserService {
  /**
   * Все парсеры
   */
  private final Map<Class, Map<Class, TypeParser>> parsers = new HashMap<>();

  /**
   * Конструктор
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
  public <T> T parseTo(Class<T> to, Object value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call ParserService!");
    }
    if (to.isInstance(value)) {
      if (to == String.class) {
        value = ((String) value).trim();
      }
      return (T) value;
    }
    Map<Class, TypeParser> map = parsers.get(value.getClass());
    if (map == null) {
      throw new JPParserNotFoundException(String.format("Not found parser from {%s} to {%s} class", value.getClass(), to));
    }
    TypeParser parser = map.get(to);
    if (parser == null) {
      throw new JPParserNotFoundException(String.format("Not found parser from {%s} to {%s} class", value.getClass(), to));
    }
    return (T) parser.parseObject(value);
  }

  /**
   * Возвращает парсер по приводимым типам
   *
   * @param from Входной тип
   * @param to   Выходной тип
   * @return Парсер типов
   */
  public TypeParser getParser(Class from, Class to) {
    Map<Class, TypeParser> map = parsers.get(from);
    if (map == null) {
      return null;
    }
    return map.get(to);
  }
}
