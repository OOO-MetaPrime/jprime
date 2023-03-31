package mp.jprime.groovy;

import java.util.Map;

/**
 * Интерфейс работы с Groovy
 */
public interface JPGroovyShell {
  /**
   * Возвращает значение переменной
   *
   * @param name Имя переменной
   * @return Значение переменной
   */
  Object getVariable(String name);

  /**
   * Устанавливает  значение переменной
   *
   * @param name  Имя переменной
   * @param value Значение переменной
   */
  void setVariable(String name, Object value);

  /**
   * Убирает значение переменной из контекста
   *
   * @param name Имя переменной
   */
  void removeVariable(String name);

  /**
   * Признак наличия переменной в контексте
   *
   * @param name Имя переменной
   * @return Да/Нет
   */
  boolean hasVariable(String name);

  /**
   * Возращает полный список переменных из контекста
   *
   * @return Мап переменных
   */
  Map<String, Object> getVariables();

  /**
   * Выполняет Groovy скрипт
   *
   * @param script Groovy скрипт
   * @return Результат выполнения скрипта
   */
  <T> T evaluate(String script);

  /**
   * Выполняет Groovy скрипт с кешированием
   *
   * @param script Groovy скрипт
   * @return Результат выполнения скрипта
   */
  <T> T evaluateByCache(String script);

  static JPGroovyShell newInstance() {
    return JPGroovyCommonShell.newInstance();
  }
}
