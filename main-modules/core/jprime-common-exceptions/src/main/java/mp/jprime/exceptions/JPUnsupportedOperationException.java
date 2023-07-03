package mp.jprime.exceptions;

/**
 * Ошибка неподдерживаемой операции
 */
public class JPUnsupportedOperationException extends JPAppRuntimeException {
  /**
   * Создание исключительной ситуации без параметров
   */
  public JPUnsupportedOperationException() {
    super("unsupportedOperationException", "Данная операция не поддерживается");
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   */
  public JPUnsupportedOperationException(String messageCode) {
    super(messageCode, "Данная операция не поддерживается");
  }
}
