package mp.jprime.dataaccess.functions;

import java.util.List;

/**
 * Параметры функции
 */
public interface JPDataFunctionParams {
  /**
   * Имя блока
   */
  String getSourceCode();

  /**
   * Номер строки
   *
   * @return Номер строки
   */
  int getRowNum();

  /**
   * Имя поля
   *
   * @return Имя поля
   */
  String getFieldCode();

  /**
   * Формат результата функции
   *
   * @return Формат функции
   */
  String getTemplate();

  /**
   * Аргументы функции
   *
   * @return Аргументы функции
   */
  List<Object> getArgs();
}
