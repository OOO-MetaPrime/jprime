package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение для случая, когда конвертация не удалась
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class JPFileConversionFailedException extends JPRuntimeException {

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
  public JPFileConversionFailedException(String messageCode, String message) {
    super(message);
    this.messageCode = messageCode;
  }

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPFileConversionFailedException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPFileConversionFailedException(String message) {
    super(message);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации
   *
   * @param cause исключительная ситуация
   */
  public JPFileConversionFailedException(Throwable cause) {
    super(cause);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации с дополнительным указанием сообщения
   *
   * @param message сообщение
   * @param cause   исключительная ситуация
   */
  public JPFileConversionFailedException(String message, Throwable cause) {
    super(message, cause);
  }

}
