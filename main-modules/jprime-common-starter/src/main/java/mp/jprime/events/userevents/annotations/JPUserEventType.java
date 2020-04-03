package mp.jprime.events.userevents.annotations;

import java.lang.annotation.*;

/**
 * Тип пользовательских событий
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JPUserEventType {
  /**
   * Код типа события
   *
   * @return Код типа события
   */
  String code();
}