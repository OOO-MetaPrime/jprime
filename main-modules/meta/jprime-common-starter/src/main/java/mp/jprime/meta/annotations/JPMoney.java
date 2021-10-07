package mp.jprime.meta.annotations;

/**
 * Описание денежнного типа
 */
public @interface JPMoney {
  /**
   * Код типа валюты
   *
   * @return Код типа валюты
   */
  String currency();
}