package mp.jprime.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для привязки сервисов к метаописанию классов
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPClassesLink {
  /**
   * Признак универсального применения
   */
  boolean uni() default false;

  /**
   * Список классов для привязки
   *
   * @return Массив кодовых имен
   */
  String[] jpClasses() default {};
}
