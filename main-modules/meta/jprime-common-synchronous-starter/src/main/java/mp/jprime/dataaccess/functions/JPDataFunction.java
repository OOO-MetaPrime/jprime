package mp.jprime.dataaccess.functions;

import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Функция с данными
 */
public interface JPDataFunction<V> {
  /**
   * Кодовое имя функции
   *
   * @return Кодовое имя функции
   */
  String getCode();

  /**
   * список шаблонов для вызова функции
   *
   * @return список шаблонов для вызова функции
   */
  Collection<String> getTemplates();

  /**
   * Получает данные функции
   *
   * @param args Список всех аргументов функции
   * @param auth авторизация
   * @return Параметры - результат
   */
  Map<JPDataFunctionParams, JPDataFunctionResult<V>> eval(Collection<JPDataFunctionParams> args, AuthInfo auth);

  /**
   * Возвращает кодовое имя аргумента функции по его индексу
   *
   * @param index индекс аргумента
   * @return Кодовое имя аргумента функции
   */
  String getArgCode(int index);
}
