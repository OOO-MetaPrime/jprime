package mp.jprime.events.systemevents.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PasswordExpirationWarningEvent {
  /**
   * Событие отправки уведомления о смене пароля
   */
  public static final String CODE = "passwordExpirationWarningEvent";

  private final JPSystemEvent jpSystemEvent;

  private PasswordExpirationWarningEvent(JPSystemEvent jpSystemEvent) {
    this.jpSystemEvent = jpSystemEvent;
  }

  public static PasswordExpirationWarningEvent from(JPSystemEvent jpSystemEvent) {
    return new PasswordExpirationWarningEvent(jpSystemEvent);
  }

  /**
   * Разделитель для форматирования списка ролей пользователя
   */
  private static final String DELIMETER = ",";

  /**
   * Параметры события
   */
  public interface Params {
    /**
     * Идентификатор пользователя
     */
    String USER_ID = "user_id";
    /**
     * Список ролей пользователя
     */
    String USER_ROLES  = "roles";
    /**
     * Дата окончания действия пароля пользователя
     */
    String PASSWORD_EXPIRATION_DATE = "passwordExpirationDate";
  }

  /**
   * Возвращает идентификатор пользователя
   *
   * @return Идентификатор пользователя
   */
  public String getUserId() {
    return jpSystemEvent.getData().get(Params.USER_ID);
  }

  /**
   * Возвращает список ролей пользователя
   *
   * @return Список ролей пользователя
   */
  public Collection<String> getRoles() {
    return Arrays.asList(jpSystemEvent.getData().get(Params.USER_ROLES).split(DELIMETER));
  }

  /**
   * Возвращает дату окончания действия пароля пользователя
   *
   * @return Дата окончания действия пароля пользователя
   */
  public LocalDate getPasswordExpirationDate() {
    return LocalDate.parse(jpSystemEvent.getData().get(Params.PASSWORD_EXPIRATION_DATE), getDateFormatter());
  }

  /**
   * Событие отправки уведомления о смене пароля
   */
  public static JPSystemEvent newEvent(String userId, Collection<String> userRoles, LocalDate passwordDate) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(false)
        .data(new HashMap<>() {
          {
            put(Params.USER_ID, userId);
            put(Params.USER_ROLES, String.join(DELIMETER, userRoles));
            put(Params.PASSWORD_EXPIRATION_DATE, passwordDate.format(getDateFormatter()));
          }
        })
        .build();
  }

  /**
   * Возвращает DateFormatter для форматирования даты окончания действия пароля
   *
   * @return DateFormatter
   */
  private static DateTimeFormatter getDateFormatter(){
    return DateTimeFormatter.ISO_LOCAL_DATE;
  }

}
