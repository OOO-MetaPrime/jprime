package mp.jprime.meta.annotations;

/**
 * Описание денежного типа
 */
public @interface JPMoney {
  /**
   * Код типа валюты
   *
   * @return Код типа валюты
   */
  String currency();
}