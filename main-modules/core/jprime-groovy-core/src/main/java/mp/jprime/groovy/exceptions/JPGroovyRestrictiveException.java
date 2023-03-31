package mp.jprime.groovy.exceptions;

/**
 * Ошибка выполнения запрещенного groovy-скрипта
 */
public class JPGroovyRestrictiveException extends RuntimeException {

  /**
   * Код мультиязычного сообщения
   */
  private String messageCode;

  /**
   * Код ошибки
   *
   * @return Код ошибки
   */
  public String getMessageCode() {
    return messageCode;
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPGroovyRestrictiveException(String messageCode, String message) {
    super(message);
    this.messageCode = messageCode;
  }

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPGroovyRestrictiveException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPGroovyRestrictiveException(String message) {
    super(message);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации
   *
   * @param cause исключительная ситуация
   */
  public JPGroovyRestrictiveException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации с дополнительным указанием сообщения
   *
   * @param message сообщение
   * @param cause   исключительная ситуация
   */
  public JPGroovyRestrictiveException(String message, Throwable cause) {
    super(message, cause);
  }
}
