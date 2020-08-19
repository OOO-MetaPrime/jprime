package mp.jprime.security.abac.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация настроек ABAC
 * Применяется к классам, помеченным интерфейсом JPSecuritySettings
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPPolicySets {
  /**
   * Список настроек ABAC
   *
   * @return Список настроек ABAC
   */
  JPPolicySet[] value() default {};
}
