package mp.jprime.parsers;

import mp.jprime.parsers.exceptions.JPParseException;

/**
 * Парсер типов
 */
public interface ParserService {
  /**
   * Приводит значение к строке
   *
   * @param value Значение
   * @return Значение
   */
  default String toString(Object value) {
    return parseTo(String.class, value);
  }

  /**
   * Приводит значение к указанному типу
   * При ошибке возвращает типовую ошибку "Неверно указано значение"
   *
   * @param to    Выходной тип
   * @param value Значение
   * @param field Код роля
   * @return Значение
   */
  default <T> T parseTo(Class<T> to, Object value, String field) {
    try {
      return parseTo(to, value);
    } catch (Exception e) {
      throw new JPParseException("valueparseerror." + field, "Неверно указано значение " + field);
    }
  }

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

  /**
   * Проверяет возможность приведения к указанному типу
   *
   * @param to    Выходной тип
   * @param value Значение
   * @return Признак возможности успешного приведения к указанному типу
   */
  boolean isParsable(Class<?> to, Object value);
}
