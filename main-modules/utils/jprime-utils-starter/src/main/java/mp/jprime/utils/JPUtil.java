package mp.jprime.utils;

import mp.jprime.security.AuthInfo;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Интерфейс утилиты
 */
public interface JPUtil {
  Logger LOG = LoggerFactory.getLogger(JPUtil.class);

  @JPUtilModeLink(code = "check",
      title = "Проверка запуска",
      outClass = JPUtilCheckOutParams.class
  )
  default Mono<JPUtilCheckOutParams> check(JPUtilCheckInParams in, AuthInfo authInfo) {
    return Mono.just(JPUtilCheckOutParams.newBuilder()
        .denied(false)
        .description("Запуск разрешен")
        .build()
    );
  }

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

  /**
   * Теги классов, обрабатываемые этой утилитой
   *
   * @return Теги классов, обрабатываемые этой утилитой
   */
  default String[] getJpClassTags() {
    JPUtilLink anno = this.getClass().getAnnotation(JPUtilLink.class);
    return anno != null ? anno.jpClassTags() : null;
  }
}
