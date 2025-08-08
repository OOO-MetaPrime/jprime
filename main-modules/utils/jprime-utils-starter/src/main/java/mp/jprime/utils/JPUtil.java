package mp.jprime.utils;

import mp.jprime.json.beans.JsonValidateResult;
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

  /**
   * Типовые коды режимов
   */
  interface Mode {
    /**
     * Режим проверки доступности по умолчанию
     */
    String CHECK_MODE = "check";

    /**
     * Режим получения дефолтных значений по умолчанию
     */
    String IN_PARAMS_DEF_VALUES = "inParamsDefValues";

    /**
     * Режим валидации входящих параметров утилиты
     */
    String VALIDATE_MODE = "validate";
  }

  @JPUtilModeLink(
      code = Mode.CHECK_MODE,
      title = "Проверка запуска",
      outClass = JPUtilCheckOutParams.class
  )
  default Mono<JPUtilCheckOutParams> check(JPUtilCheckInParams in, AuthInfo auth) {
    return Mono.just(JPUtilCheckOutParams.newBuilder()
        .denied(false)
        .description("Запуск разрешен")
        .build()
    );
  }

  @JPUtilModeLink(
      code = Mode.IN_PARAMS_DEF_VALUES,
      title = "Получение значений по умолчанию для утилиты",
      outClass = JPUtilDefValuesOutParams.class
  )
  default Mono<JPUtilDefValuesOutParams> inParamsDefValues(DefaultInParams in, AuthInfo auth) {
    return Mono.just(JPUtilDefValuesOutParams.newBuilder()
        .description("Значения по умолчанию отсутствуют")
        .build()
    );
  }

  @JPUtilModeLink(
      code = Mode.VALIDATE_MODE,
      title = "Валидация входящих параметров утилиты",
      outClass = JPUtilValidateOutParams.class
  )
  default Mono<JPUtilValidateOutParams> validate(DefaultInParams in, AuthInfo auth) {
    return Mono.just(JPUtilValidateOutParams.newBuilder()
        .result(JsonValidateResult.valid())
        .description("Валидация параметров отсутствует")
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
