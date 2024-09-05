package mp.jprime.exceptions;

/**
 * Файл не найден
 */
public class JPFileNotFoundException extends JPNotFoundException {
  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPFileNotFoundException(String messageCode, String message) {
    super(messageCode, message);
  }

  /**
   * Конструктор
   */
  public JPFileNotFoundException() {
    this("file.notFound", "Файл не найден");
  }
}
