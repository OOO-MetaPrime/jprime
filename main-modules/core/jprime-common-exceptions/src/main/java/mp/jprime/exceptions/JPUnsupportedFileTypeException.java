package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение для случая, если тип файла не поддерживается для конвертации
 */
@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class JPUnsupportedFileTypeException extends JPAppRuntimeException {
  private static final String CODE = "filepreview.filetype.unsupported";
  private static final String MESSAGE_TEMPLATE = "Не могу конвертировать файл с расширением '%s' в PDF";

  private JPUnsupportedFileTypeException(String message) {
    super(CODE, message);
  }

  /**
   * Построитель исключения со стандартным сообщением
   *
   * @param extension расширение файла
   */
  public static JPUnsupportedFileTypeException ofExtension(String extension) {
    return new JPUnsupportedFileTypeException(String.format(MESSAGE_TEMPLATE, extension));
  }

  /**
   * Построитель исключения с переданным сообщением
   *
   * @param message сообщение ошибки
   */
  public static JPUnsupportedFileTypeException newException(String message) {
    return new JPUnsupportedFileTypeException(message);
  }
}
