package mp.jprime.json.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Заголовок для полей объекта не являющимися объектом
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonViewProperty {
  /**
   * Заголовок поля
   */
  String value() default "";

  /**
   * Преобразовать значение в класс
   * @return класс в который преобразовать значение
   */
  Class<?> toClass() default Void.class;
}
