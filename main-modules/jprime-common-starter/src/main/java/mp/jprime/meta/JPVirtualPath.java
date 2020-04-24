package mp.jprime.meta;

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
   * Кодовое имя целевого атрибута, на которыйф строится ссылка
   *
   * @return Кодовое имя атрибута
   */
  String getTargerAttrCode();
}
