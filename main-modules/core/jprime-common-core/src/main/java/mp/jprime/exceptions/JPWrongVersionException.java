package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибки работы с версией объектов
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class JPWrongVersionException extends JPAppRuntimeException {
  private final String updatedUserDescription;
  private final String updatedUserLogin;

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPWrongVersionException() {
    super();
    updatedUserDescription = null;
    updatedUserLogin = null;
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param code        Код объекта
   */
  public JPWrongVersionException(String messageCode, String code) {
    this(messageCode, "Версия объекта с кодом \"" + code + "\" не соответствует актуальной", null, null);
  }

  /**
   * Конструктор
   *
   * @param messageCode            Код ошибки
   * @param code                   Код объекта
   * @param updatedUserDescription Описание последнего редактора
   * @param updatedUserLogin       Имя последнего редактора
   */
  public JPWrongVersionException(String messageCode, String code, String updatedUserDescription, String updatedUserLogin) {
    super(messageCode, "Версия объекта с кодом \"" + code + "\" не соответствует актуальной");
    this.updatedUserDescription = updatedUserDescription;
    this.updatedUserLogin = updatedUserLogin;
  }

  /**
   * Получить Описание последнего редактора
   *
   * @return Описание последнего редактора
   */
  public String getUpdatedUserDescription() {
    return updatedUserDescription;
  }

  /**
   * Получить Имя последнего редактора
   *
   * @return Имя последнего редактора
   */
  public String getUpdatedUserLogin() {
    return updatedUserLogin;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String messageCode;
    private String code;
    private String updatedUserDescription;
    private String updatedUserLogin;

    private Builder() {
    }

    public Builder messageCode(String messageCode) {
      this.messageCode = messageCode;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder updatedUserDescription(String updatedUserDescription) {
      this.updatedUserDescription = updatedUserDescription;
      return this;
    }

    public Builder updatedUserLogin(String updatedUserLogin) {
      this.updatedUserLogin = updatedUserLogin;
      return this;
    }

    public JPWrongVersionException build() {
      return new JPWrongVersionException(messageCode, code, updatedUserDescription, updatedUserLogin);
    }
  }
}
