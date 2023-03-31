package mp.jprime.parsers;

/**
 * Парсер типов
 */
public interface ParserService {
  /**
   * Приводит значение к указанному типу
   *
   * @param to    Выходной тип
   * @param value Значение
   * @return Значение
   */
  <T> T parseTo(Class<T> to, Object value);

  /**
   * Возвращает парсер по приводимым типам
   *
   * @param from Входной тип
   * @param to   Выходной тип
   * @return Парсер типов
   */
  TypeParser getParser(Class from, Class to);
}
