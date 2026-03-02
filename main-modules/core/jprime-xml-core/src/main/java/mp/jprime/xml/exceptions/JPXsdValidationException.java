package mp.jprime.xml.exceptions;

import jakarta.xml.bind.ValidationEvent;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.xml.utils.ValidationUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Ошибка xsd-валидации
 */
public class JPXsdValidationException extends JPAppRuntimeException {
  private static final long serialVersionUID = 1L;
  /**
   * Код ошибки
   */
  public static final String XSD_VALIDATION_ERROR_CODE = "mp.jprime.xml.xsdValidationError";

  /**
   * Элементы, не прошедшие валидацию
   * key - уровень события; value - ошибка
   * 0  - warning
   * 1  - error
   * 2  - fatal
   * -1 - unknowns
   */
  private Map<Integer, Collection<String>> validationEvents = new HashMap<>();


  /**
   * Конструктор
   *
   * @param message сообщение
   */
  public JPXsdValidationException(String message) {
    super(XSD_VALIDATION_ERROR_CODE, message);
  }

  /**
   * Конструктор
   *
   * @param message сообщение
   * @param cause   ошибка
   */
  public JPXsdValidationException(String message, Throwable cause) {
    super(XSD_VALIDATION_ERROR_CODE, message + ". " + cause.getMessage());
  }


  /**
   * Конструктор
   *
   * @param message сообщение
   * @param validationEvents  параметры, не прошедшие валидацию
   */
  public JPXsdValidationException(String message, Map<Integer, Collection<String>> validationEvents) {
    super(XSD_VALIDATION_ERROR_CODE, ValidationUtils.getXmlValidationErrorMessage(message, validationEvents));
    this.validationEvents = validationEvents;
  }

  /**
   * Конструктор
   *
   * @param message сообщение
   * @param validationEvents  параметры, не прошедшие валидацию
   * @param cause   ошибка
   */
  public JPXsdValidationException(String message, Map<Integer, Collection<String>> validationEvents, Throwable cause) {
    super(XSD_VALIDATION_ERROR_CODE, ValidationUtils.getXmlValidationErrorMessage(message, validationEvents));
    this.validationEvents = validationEvents;
  }

  /**
   * Конструктор
   *
   * @param cause ошибка
   */
  public JPXsdValidationException(Throwable cause) {
    super(XSD_VALIDATION_ERROR_CODE, cause.getMessage());
  }


  public Map<Integer, Collection<String>> getValidationEvents() {
    return validationEvents;
  }

  public Collection<String> getWarngings() {
    return validationEvents.get(ValidationEvent.WARNING) != null ? validationEvents.get(ValidationEvent.WARNING) : Collections.emptyList();
  }

  public Collection<String> getErrors() {
    return validationEvents.get(ValidationEvent.ERROR) != null ? validationEvents.get(ValidationEvent.ERROR) : Collections.emptyList();
  }

  public Collection<String> getFatals() {
    return validationEvents.get(ValidationEvent.FATAL_ERROR) != null ? validationEvents.get(ValidationEvent.FATAL_ERROR) : Collections.emptyList();
  }

  public Collection<String> getErrorsAndFatals() {
    return Stream.of(getErrors(), getFatals())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
