package mp.jprime.globalsettings.events;

import org.springframework.context.ApplicationEvent;

/**
 * Событие окончания загрузки настроек системы
 */
public class JPGlobalSettingsLoadFinishEvent extends ApplicationEvent {
  private JPGlobalSettingsLoadFinishEvent() {
    super("");
  }

  /**
   * Конструктор
   *
   * @return Событие
   */
  public static JPGlobalSettingsLoadFinishEvent newEvent() {
    return new JPGlobalSettingsLoadFinishEvent();
  }
}
