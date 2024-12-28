package mp.jprime.events.userevents;

import java.util.Map;

/**
 * Доп. данные пользовательского события
 */
public interface JPUserEventData {
  /**
   * Доп. данные
   *
   * @return Доп. данные
   */
  Map<String, Object> getAddInfo();
}
