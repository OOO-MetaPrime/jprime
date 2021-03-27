package mp.jprime.utils.annotations;

import java.lang.annotation.*;

/**
 * Тип результатов утилиты
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JPUtilResultType {
  /**
   * Код типа результата
   *
   * @return Код типа результата
   */
  String code();
}
