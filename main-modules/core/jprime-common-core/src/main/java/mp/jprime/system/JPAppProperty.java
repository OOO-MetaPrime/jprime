package mp.jprime.system;

/**
 * Параметры приложения
 */
public interface JPAppProperty {
  /**
   * Имя текущего spring сервиса
   *
   * @return Имя текущего spring сервиса
   */
  String serviceName();

  /**
   * Код текущего jprime сервиса
   *
   * @return Код текущего jprime сервиса
   */
  String applicationCode();

  /**
   * Код подсистемы (по умолчанию - код текущего сервиса)
   *
   * @return Код подсистемы (по умолчанию - код текущего сервиса)
   */
  String systemCode();

  /**
   * Название текущего jprime сервиса
   *
   * @return Название текущего jprime сервиса
   */
  String applicationTitle();
}
