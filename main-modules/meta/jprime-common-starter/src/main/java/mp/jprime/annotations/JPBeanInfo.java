package mp.jprime.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для общих свойства метакласса
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPBeanInfo {
  /**
   * Список атрибутов по умолчанию
   * Используется при автоматическом получении дополнительных атрибутов ссылочнных классов
   *
   * @return список атрибутов по умолчанию
   */
  String[] defaultJpAttrCollection() default {};
}
