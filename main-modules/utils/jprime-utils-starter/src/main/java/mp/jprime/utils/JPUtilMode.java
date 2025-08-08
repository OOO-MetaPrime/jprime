package mp.jprime.utils;

import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;

import java.util.Collection;
import java.util.UUID;

/**
 * Интерфейс шага утилиты
 */
public interface JPUtilMode {
  /**
   * Кодовое имя утилиты
   *
   * @return Кодовое имя утилиты
   */
  String getUtilCode();

  /**
   * Кодовое имя шага
   *
   * @return Кодовое имя шага
   */
  String getModeCode();

  /**
   * Возвращает название шага утилиты
   *
   * @return название шага утилиты
   */
  String getTitle();

  /**
   * Возвращает QName шага утилиты
   *
   * @return QName шага утилиты
   */
  String getQName();

  /**
   * Признак логирования действий
   *
   * @return Признак логирования действи
   */
  default boolean isActionLog() {
    return true;
  }

  /**
   * Сообщение для подтверждения
   *
   * @return Сообщение для подтверждения
   */
  String getConfirmMessage();

  /**
   * Признак универсального (для всех классов) шага
   *
   * @return Да/Нет
   */
  boolean isUni();

  /**
   * Классы, обрабатываемые этой утилитой
   *
   * @return Классы, обрабатываемые этой утилитой
   */
  Collection<String> getJpClasses();

  /**
   * Теги классов, обрабатываемые этой утилитой
   *
   * @return Теги классов, обрабатываемые этой утилитой
   */
  Collection<String> getJpClassTags();

  /**
   * Возвращает тип утилиты
   *
   * @return Тип утилиты
   */
  JPAppendType getType();

  /**
   * Атрибуты, обрабатываемые этой утилитой
   *
   * @return Атрибуты, обрабатываемые этой утилитой
   */
  Collection<JPClassAttr> getJpAttrs();

  /**
   * Класс входящих параметров
   *
   * @return Входящие параметры
   */
  Class<?> getInClass();

  /**
   * Описание входных параметров
   *
   * @return Описание входных параметров
   */
  Collection<JPUtilParam> getInParams();

  /**
   * Признак наличия значений по умолчанию
   *
   * @return Признак наличия значений по умолчанию
   */
  default boolean isInParamsDefValues() {
    return false;
  }

  /**
   * Дополнительные свойства утилиты
   *
   * @return Дополнительные свойства утилиты
   */
  Properties getProperties();

  /**
   * Сообщение на форму утилиты
   *
   * @return Сообщение на форму утилиты
   */
  String getInfoMessage();

  /**
   * Признак необходимости валидации
   *
   * @return Признак необходимости валидации
   */
  boolean isValidate();

  /**
   * Тип итогового результата
   *
   * @return Тип итогового результата
   */
  String getResultType();

  /**
   * Список кастомных итоговых параметров
   *
   * @return Список кастомных итоговых параметров
   */
  Collection<JPUtilParam> getOutCustomParams();

  /**
   * Дополнительные свойства утилиты
   */
  interface Properties {
    /**
     * Используемые шаги утилиты
     */
    Collection<String> getLinkModes();

    /**
     * Используемые настройки конфигуратора компонент
     */
    Collection<UUID> getCompConfCodes();
  }
}
