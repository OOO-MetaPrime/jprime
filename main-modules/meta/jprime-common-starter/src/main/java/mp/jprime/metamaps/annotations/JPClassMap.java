package mp.jprime.metamaps.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация описания мапинга метакласса
 * Применяется к описанию наследника класса JPMeta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPClassMap {
  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String code();

  /**
   * Кодовое имя хранилища
   *
   * @return Кодовое имя хранилища
   */
  String storage();

  /**
   * Мап на хранилище
   *
   * @return Мап на хранилище
   */
  String map() default "";

  /**
   * Список маппинга атрибутов класса
   *
   * @return Список маппинга атрибутов класса
   */
  JPAttrMap[] attrs() default {};
}
