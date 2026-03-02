package mp.jprime.utils;

import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;
import mp.jprime.common.JPParam;
import mp.jprime.utils.annotations.JPUtilProperties;

import java.util.Collection;

/**
 * Настройки шага утилиты
 */
public interface JPUtilModeSettings {
  /**
   * Кодовое имя шага
   *
   * @return Кодовое имя шага
   */
  String getCode();

  /**
   * Название шага
   *
   * @return Название шага
   */
  String getTitle();

  /**
   * QName утилиты
   *
   * @return QName утилиты
   */
  String getQName();

  /**
   * Класс входящих параметров
   *
   * @return Класс входящих параметров
   */
  Class<?> getParamInClass();

  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  String getJpPackage();

  /**
   * Роли, имеющиеся доступ к этому шагу
   *
   * @return Список ролей
   */
  String[] getAuthRoles();

  /**
   * Признак логирования действий
   *
   * @return Признак логирования действия
   */
  boolean useActionLog();

  /**
   * Тип доступности шага
   *
   * @return Тип доступности шага
   */
  JPAppendType getType();

  /**
   * Настройки доступа на атрибутах
   *
   * @return Настройки доступа на атрибутах
   */
  Collection<JPClassAttr> getJpAttrs();

  /**
   * Сообщение перед запуском шага
   */
  String getConfirm();

  /**
   * Сообщение на форму утилиты
   *
   * @return Сообщение на форму утилиты
   */
  String getInfoMessage();

  /**
   * Список входящих параметров
   *
   * @return Список входящих параметров
   */
  Collection<JPParam> getInParams();

  /**
   * Дополнительные свойства утилиты
   */
  JPUtilProperties getProperties();

  /**
   * Выходной класс параметров
   *
   * @return Выходной класс параметров
   */
  String getResultType();

  /**
   * Список кастомных исходящих параметров
   *
   * @return Список кастомных исходящих параметров
   */
  Collection<JPParam> getOutCustomParams();

  /**
   * Признак наличия значений по умолчанию
   *
   * @return Да/Нет
   */
  boolean useInParamsDefValues();

  /**
   * Признак определения динамических параметров
   *
   * @return Да/Нет
   */
  boolean useDynamicParams();

  /**
   * Признак необходимости валидации
   *
   * @return Да/Нет
   */
  boolean useValidate();
}
