package mp.jprime.globalsettings;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.formats.JPStringFormat;

import java.util.Collection;

/**
 * Метаописание свойства
 */
public interface JPGlobalProperty {
  /**
   * Кодовое имя свойства
   *
   * @return Кодовое имя свойства
   */
  String getCode();

  /**
   * Название свойства
   *
   * @return Название свойства
   */
  String getName();

  /**
   * Описание свойства
   *
   * @return Описание свойства
   */
  String getDescription();

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  JPPropertyType getType();

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Возвращает признак "только на чтение"
   *
   * @return Да/Нет
   */
  boolean isReadonly();

  /**
   * Минимальное значение
   *
   * @return Минимальное значение
   */
  Integer getMin();

  /**
   * Максимальное значение
   *
   * @return Максимальное значение
   */
  Integer getMax();

  /**
   * Тип строкового поля
   *
   * @return Тип строкового поля
   */
  JPStringFormat getStringFormat();

  /**
   * Маска строкового поля
   *
   * @return Маска строкового поля
   */
  String getStringMask();

  /**
   * Перечислимые значения
   *
   * @return Перечислимые значения
   */
  Collection<Enum> getEnums();

  /**
   * Перечислимое значение
   */
  interface Enum {
    /**
     * Код
     *
     * @return код
     */
    Object getValue();

    /**
     * Название
     *
     * @return Название
     */
    String getName();
  }
}
