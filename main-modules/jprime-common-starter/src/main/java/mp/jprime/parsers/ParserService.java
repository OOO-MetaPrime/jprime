package mp.jprime.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Парсер типов
 */
@Service
public final class ParserService {
  /**
   * Все парсеры
   */
  private Map<Class, Map<Class, TypeParser>> parsers = new HashMap<>();

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
   * Возвращает парсер по приводимым типам
   *
   * @param to    Выходной тип
   * @param value Значение
   * @return Парсер типов
   */
  public <T> T parseTo(Class<T> to, Object value) {
    if (value == null || to == null) {
      return null;
    }
    if (value.getClass() == to) {
      if (to == String.class) {
        value = ((String) value).trim();
      }
      return (T) value;
    }
    Map<Class, TypeParser> map = parsers.get(value.getClass());
    if (map == null) {
      return null;
    }
    TypeParser parser = map.get(to);
    if (parser == null) {
      return null;
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
