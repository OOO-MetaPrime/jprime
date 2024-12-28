package mp.jprime.nsi;

import mp.jprime.lang.JPMap;

import java.util.Collection;
import java.util.Collections;

/**
 * Значение НСИ справочника
 */
public interface JpNsiValue<T extends Comparable<T>> {
  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpNsi<Integer>
   */
  default JpNsiValue<Integer> toIntegerKeyType() {
    return (JpNsiValue<Integer>) this;
  }

  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpNsi<String>
   */
  default JpNsiValue<String> toStringKeyType() {
    return (JpNsiValue<String>) this;
  }

  /**
   * Код значения
   *
   * @return Код значения
   */
  T getId();

  /**
   * Название
   *
   * @return Название
   */
  String getName();

  /**
   * Доп. данные НСИ
   *
   * @return Доп. данные НСИ
   */
  JPMap getProperties();

  /**
   * Возвращает значение свойства
   *
   * @param code Код свойства
   * @return значение свойства
   */
  default Object getProperty(String code) {
    return getProperties().get(code);
  }

  /**
   * Возвращает НСИ и сегменты, в которых используется данное значение
   *
   * @return Список НСИ
   */
  default Collection<JpNsi<T>> nsi() {
    return Collections.emptyList();
  }

  /**
   * Доступ к данным НСИ
   *
   * @return JpNsiAccess
   */
  JpNsiAccess getNsiAccess();
}
