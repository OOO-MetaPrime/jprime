package mp.jprime.nsi;

import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Collections;

/**
 * Описание НСИ справочника
 */
public interface JpNsi<T extends Comparable<T>> extends JpNsiType<T> {
  /**
   * Разделитель для сегмента
   */
  String SEGMENT_SEPARATOR = "-";

  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpNsi<Integer>
   */
  default JpNsi<Integer> toIntegerKeyType() {
    return (JpNsi<Integer>) this;
  }

  /**
   * Приводит тип НСИ к указанному
   *
   * @return JpNsi<String>
   */
  default JpNsi<String> toStringKeyType() {
    return (JpNsi<String>) this;
  }

  /**
   * НСИ значения
   *
   * @return НСИ значения
   */
  Collection<JpNsiValue<T>> getValues();

  /**
   * Возвращает НСИ значения
   *
   * @param auth AuthInfo
   * @return Список значений НСИ
   */
  default Collection<JpNsiValue<T>> getValuesByAuth(AuthInfo auth) {
    if (!this.getNsiAccess().accept(auth)) {
      return Collections.emptyList();
    }
    Collection<JpNsiValue<T>> values = this.getValues();
    return values.stream()
        .filter(x -> x.getNsiAccess().accept(auth))
        .toList();
  }

  /**
   * Возвращает значение по id
   *
   * @param id Код справочника
   * @return Значение
   */
  JpNsiValue<T> getValueById(T id);
}
