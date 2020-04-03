package mp.jprime.security.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация настроек доступа
 * Применяется к классам, помеченным интерфейсом JPSecuritySettings
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPPackages {
  /**
   * Список настроек доступа
   *
   * @return Список настроек доступа
   */
  JPPackage[] value() default {};
}
