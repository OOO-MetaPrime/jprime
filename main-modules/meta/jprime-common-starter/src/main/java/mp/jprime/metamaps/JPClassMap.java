package mp.jprime.metamaps;

import java.util.Collection;

/**
 * описание привязки класса к хранилищу
 */
public interface JPClassMap {
  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String getCode();

  /**
   * Кодовое имя хранилища
   *
   * @return Кодовое имя хранилища
   */
  String getStorage();

  /**
   * Мап на хранилище
   *
   * @return Мап на хранилище
   */
  String getMap();

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  Collection<JPAttrMap> getAttrs();

  /**
   * Маппинг атрибутов
   *
   * @return Маппинг атрибутов
   */
  JPAttrMap getAttr(String code);

  /**
   * Признак неизменяемой привязки к хранилищу
   *
   * @return Да/Нет
   */
  default boolean isImmutable() {
    return Boolean.TRUE;
  }
}