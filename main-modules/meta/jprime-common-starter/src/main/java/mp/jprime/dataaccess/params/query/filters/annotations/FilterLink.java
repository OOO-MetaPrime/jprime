package mp.jprime.dataaccess.params.query.filters.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для отметки обработчиков
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface FilterLink {
  /**
   * Класс условия
   *
   * @return Класс условия
   */
  Class exprClass();
}
