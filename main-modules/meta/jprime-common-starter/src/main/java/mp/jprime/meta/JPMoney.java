package mp.jprime.meta;

/**
 * Метаописание хранения денежного типа
 */
public interface JPMoney {
  /**
   * Атрибут для хранения
   *
   * @return Кодовое имя атрибута
   */
  String getAttrCode();

  /**
   * Код валюты
   *
   * @return Код валюты
   */
  String getCurrencyCode();
}
