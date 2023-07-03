package mp.jprime.functions;

import java.util.Collection;

/**
 * Группа функций
 */
public interface JPFunctionGroup {
  /**
   * Код группы
   *
   * @return Код группы
   */
  String getGroupCode();

  /**
   * Коды функций, входящих в группу
   *
   * @return Коды функций, входящих в группу
   */
  Collection<String> getFunctionCodes();

  /**
   * Возвращает описание функции по коду
   *
   * @param code Код функции
   * @return Функция
   */
  JPFunction<?> getFunction(String code);
}
