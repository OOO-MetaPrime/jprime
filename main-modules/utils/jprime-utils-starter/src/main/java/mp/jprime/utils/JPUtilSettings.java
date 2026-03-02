package mp.jprime.utils;

import mp.jprime.common.JPParam;
import mp.jprime.common.beans.JPCommonParam;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс настроек утилиты
 */
public interface JPUtilSettings {
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

  Map<String, JPParam> DEFAULT_PARAMS = Map.of(
      JPParam.ROOT_OBJECT_CLASS_CODE, JPCommonParam.newBuilder()
          .code(JPParam.ROOT_OBJECT_CLASS_CODE)
          .type(JPType.STRING)
          .description("кодовое имя метакласса корневого объекта")
          .build(),
      JPParam.ROOT_OBJECT_ID, JPCommonParam.newBuilder()
          .code(JPParam.ROOT_OBJECT_ID)
          .type(JPType.STRING)
          .description("идентификатор корневого объекта")
          .build(),
      JPParam.OBJECT_CLASS_CODE, JPCommonParam.newBuilder()
          .code(JPParam.OBJECT_CLASS_CODE)
          .type(JPType.STRING)
          .description("Кодовое имя метакласса объекта")
          .build(),
      JPParam.OBJECT_IDS, JPCommonParam.newBuilder()
          .code(JPParam.OBJECT_IDS)
          .type(JPType.STRING)
          .description("Идентификатор объекта")
          .multiple(true)
          .build()
  );


  /**
   * Возвращает урл. Уникальный код утилиты
   *
   * @return Урл утилиты
   */
  String getCode();

  /**
   * Возвращает название утилиты
   *
   * @return название утилиты
   */
  String getTitle();

  /**
   * Возвращает QName утилиты
   *
   * @return QName утилиты
   */
  String getQName();

  /**
   * Настройка доступа к этой утилите
   *
   * @return Роли, имеющиеся доступ к этой утилите
   */
  String getJpPackage();

  /**
   * Роли, имеющиеся доступ к этой утилите
   *
   * @return Роли, имеющиеся доступ к этой утилите
   */
  String[] getAuthRoles();

  /**
   * Признак универсальной утилиты
   *
   * @return Да/Нет
   */
  boolean isUni();

  /**
   * Классы, обрабатываемые этой утилитой
   *
   * @return Классы, обрабатываемые этой утилитой
   */
  String[] getJpClasses();

  /**
   * Теги классов, обрабатываемые этой утилитой
   *
   * @return Теги классов, обрабатываемые этой утилитой
   */
  String[] getJpClassTags();

  /**
   * Теги утилиты
   *
   * @return Теги утилиты
   */
  String[] getJpUtilTags();

  /**
   * Java обработчик
   *
   * @return JPUtilAction
   */
  JPUtilAction getAction();

  /**
   * Список шагов утилиты
   *
   * @return Шаги утилиты
   */
  Collection<JPUtilModeSettings> getModeList();
}
