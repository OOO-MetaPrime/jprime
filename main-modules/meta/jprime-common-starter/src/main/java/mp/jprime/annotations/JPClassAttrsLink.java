package mp.jprime.annotations;

import mp.jprime.common.annotations.JPClassAttr;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Привязки к определенным атрибутам
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPClassAttrsLink {
  /**
   * Привязка к атрибутам
   *
   * @return Настройки привязки к атрибутам
   */
  JPClassAttr[] jpAttrs() default {};
}
