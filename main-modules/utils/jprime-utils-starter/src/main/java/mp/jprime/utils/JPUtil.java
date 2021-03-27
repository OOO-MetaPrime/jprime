package mp.jprime.utils;

import mp.jprime.utils.annotations.JPUtilLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Интерфейс утилиты
 */
public interface JPUtil {
  Logger LOG = LoggerFactory.getLogger(JPUtil.class);

  /**
   * Возвращает название утилиты
   *
   * @return название утилиты
   */
  default String getTitle() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.title() : null;
  }

  /**
   * Возвращает QName утилиты
   *
   * @return QName утилиты
   */
  default String getQName() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.qName() : null;
  }

  /**
   * Возвращает урл. Уникальный код утилиты
   *
   * @return Урл утилиты
   */
  default String getUrl() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.code() : null;
  }

  /**
   * Настройка доступа к этой утилите
   *
   * @return Роли, имеющиеся доступ к этой утилите
   */
  default String getJpPackage() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.jpPackage() : null;
  }

  /**
   * Роли, имеющиеся доступ к этой утилите
   *
   * @return Роли, имеющиеся доступ к этой утилите
   */
  default String[] getAuthRoles() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.authRoles() : null;
  }

  /**
   * Признак универсальной утилиты
   *
   * @return Да/Нет
   */
  default boolean isUni() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null && anno.uni();
  }

  /**
   * Классы, обрабатываемые этой утилитой
   *
   * @return Классы, обрабатываемые этой утилитой
   */
  default String[] getJpClasses() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.jpClasses() : null;
  }
}
