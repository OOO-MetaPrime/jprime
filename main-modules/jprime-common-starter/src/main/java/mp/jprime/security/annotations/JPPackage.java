package mp.jprime.security.annotations;

/**
 * Аннотация настроек доступа
 */
public @interface JPPackage {
  /**
   * Кодовое имя пакета
   *
   * @return Кодовое имя пакета
   */
  String code();

  /**
   * Название пакета
   *
   * @return Название пакета
   */
  String name();

  /**
   * Описание пакета
   *
   * @return Описание пакета
   */
  String description();

  /**
   * qName пакета
   *
   * @return qName пакета
   */
  String qName() default "";

  /**
   * Список настроек доступа
   *
   * @return Список настроек доступа
   */
  JPAccess[] access() default {};
}
