package mp.jprime.metamaps;

import mp.jprime.metamaps.beans.JPAttrMapBean;

/**
 * Конверторы мета-бинов
 */
public final class JPMetaMapConvertor {
  /**
   * Создание
   *
   * @param attr mp.jprime.metamaps.annotations.JPAttrMap
   * @return JPAttrMap
   */
  public static JPAttrMap of(mp.jprime.metamaps.annotations.JPAttrMap attr) {
    return JPAttrMapBean.newBuilder()
        .code(attr.code())
        .map(attr.map())
        .fuzzyMap(attr.fuzzyMap())
        .cs(attr.cs().getCode())
        .readOnly(attr.readOnly())
        .build();
  }
}
