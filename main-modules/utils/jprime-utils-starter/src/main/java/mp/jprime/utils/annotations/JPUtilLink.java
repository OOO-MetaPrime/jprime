package mp.jprime.utils.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для отметки утилит
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPUtilLink {
  /**
   * Уникальный код утилиты
   *
   * @return Уникальный код утилиты
   */
  String code();

  /**
   * Название утилиты
   *
   * @return Название утилиты
   */
  String title();

  /**
   * QName утилиты
   *
   * @return QName утилиты
   */
  String qName() default "";

  /**
   * Признак универсального применения
   */
  boolean uni() default false;

  /**
   * Классы, обрабатываемые этой утилитой
   *
   * @return Кодовые имена классов, обрабатываемых этой утилитой
   */
  String[] jpClasses() default {};

  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  String jpPackage() default "";

  /**
   * Роли, имеющиеся доступ к этой утилите
   *
   * @return Список ролей
   */
  String[] authRoles() default {};
}
