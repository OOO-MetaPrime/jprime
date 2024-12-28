package mp.jprime.meta;

import mp.jprime.meta.beans.JPType;

/**
 * Путь виртуальной ссылки
 */
public interface JPVirtualPath {
  /**
   * Кодовое имя ссылочного атрибута, по которому строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  String getRefAttrCode();

  /**
   * Кодовое имя целевого атрибута, на который строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  String getTargerAttrCode();

  /**
   * Тип виртуальной ссылки
   *
   * @return Тип виртуальной ссылки
   */
  JPType getType();
}
