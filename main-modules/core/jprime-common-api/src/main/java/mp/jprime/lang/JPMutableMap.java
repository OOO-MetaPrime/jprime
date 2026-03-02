package mp.jprime.lang;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Редактируемая Map
 */
public interface JPMutableMap extends JPMap {
  /**
   * Сохраняет данные
   *
   * @param key   Ключ
   * @param value Значение
   * @return Значение
   */
  <T> T put(String key, Object value);

  /**
   * Удаляет данные
   *
   * @return Данные
   */
  <T> T remove(String key);

  /**
   * Сохраняет данные
   *
   * @param data Данные
   */
  void putAll(JPMap data);

  /**
   * Сохраняет данные
   *
   * @param data Данные
   */
  void putAll(Map<String, Object> data);

  /**
   * Сохраняет данные
   *
   * @param key             Ключ
   * @param mappingFunction Вычисление значения
   * @return Значение
   */
  <T> T computeIfAbsent(String key, Function<String, ? extends Object> mappingFunction);

  /**
   * Сохраняет данные
   *
   * @param key   Ключ
   * @param value Значение
   * @return Значение
   */
  <T> T putIfAbsent(String key, Object value);

  /**
   * Сохраняет данные
   *
   * @param data Данные
   */
  void putIfAbsent(Map<String, Object> data);

  /**
   * Возвращает entrySet
   *
   * @return entrySet
   */
  Set<Map.Entry<String, Object>> entrySet();
}
