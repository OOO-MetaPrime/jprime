package mp.jprime.nsi;

import mp.jprime.common.JPOrderDirection;
import mp.jprime.meta.beans.JPType;
import mp.jprime.nsi.consts.JpNsiAttr;

import java.util.Collection;

/**
 * Описание вида НСИ справочника
 */
public interface JpNsiType<T extends Comparable<T>> {
  /**
   * Возвращает тип ключа
   *
   * @return Тип Ключа
   */
  JPType getKeyType();

  /**
   * Код справочника
   *
   * @return Код
   */
  String getCode();

  /**
   * Название справочника
   *
   * @return Название
   */
  String getName();

  /**
   * Список дополнительных свойств
   *
   * @return Дополнительные свойства
   */
  Collection<JpNsiProperty> getProperties();

  /**
   * Список группировок дополнительных свойств
   *
   * @return Дополнительные свойства
   */
  Collection<JpNsiPropertyGroup> getGroups();

  /**
   * Возвращает описание свойства
   *
   * @param code Код свойства
   * @return описание свойства
   */
  JpNsiProperty getProperty(String code);

  /**
   * Сортировка по умолчанию
   *
   * @return Сортировка по умолчанию
   */
  default DefaultSort getDefaultSort() {
    return DefaultSort.NAME_ASC;
  }

  /**
   * Доступ к данным НСИ
   *
   * @return JpNsiAccess
   */
  default JpNsiAccess getNsiAccess() {
    return JpNsiAccess.EMPTY;
  }

  /**
   * Направления сортировок
   */
  enum DefaultSort {
    ID_ASC(JpNsiAttr.ID, JPOrderDirection.ASC),
    ID_DESC(JpNsiAttr.ID, JPOrderDirection.DESC),
    NAME_ASC(JpNsiAttr.NAME, JPOrderDirection.ASC),
    NAME_DESC(JpNsiAttr.NAME, JPOrderDirection.DESC);

    private final String code;
    private final JPOrderDirection orderDirection;

    DefaultSort(String code, JPOrderDirection orderDirection) {
      this.code = code;
      this.orderDirection = orderDirection;
    }

    public String getCode() {
      return code;
    }

    public JPOrderDirection getOrderDirection() {
      return orderDirection;
    }

    public static DefaultSort get(String code, String order) {
      for (DefaultSort defaultSort : DefaultSort.values()) {
        if (defaultSort.code.equalsIgnoreCase(code) && defaultSort.orderDirection.getCode().equalsIgnoreCase(order)) {
          return defaultSort;
        }
      }
      return null;
    }
  }
}
