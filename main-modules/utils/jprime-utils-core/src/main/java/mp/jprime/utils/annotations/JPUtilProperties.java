package mp.jprime.utils.annotations;

/**
 * Дополнительные свойства утилиты
 */
public @interface JPUtilProperties {
  /**
   * Используемые шаги утилиты
   */
  String[] linkModes() default {};

  /**
   * Используемые настройки конфигуратора компонент
   */
  String[] compConfCodes() default {};
}
