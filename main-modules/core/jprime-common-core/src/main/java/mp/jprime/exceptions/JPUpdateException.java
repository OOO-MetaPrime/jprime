package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибки обновления объекта
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class JPUpdateException extends JPAppRuntimeException {
  /**
   * Создание исключительной ситуации без параметров
   */
  public JPUpdateException() {
    super();
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param code        Код объекта
   */
  public JPUpdateException(String messageCode, String code) {
    super(messageCode, "Не удалось обновить объект с кодом: \"" + code + "\"");
  }
}
