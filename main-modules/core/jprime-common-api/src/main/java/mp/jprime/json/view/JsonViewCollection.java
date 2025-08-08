package mp.jprime.json.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для поля объекта наследника коллекции
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonViewCollection {
  /**
   * Заголовок коллекции
   */
  String value() default "";
  /**
   * Заголовок элемента коллекции
   */
  String entity() default "";
}
