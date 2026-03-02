package mp.jprime.application;

/**
 * Слушатель инициализации JPrime-приложения
 * - отрабатывает после JPApplicationBootListener
 * - отрабатывает до JPApplicationStartListener
 */
public interface JPApplicationInitListener {
  /**
   * Обработка инициализации приложения
   */
  void applicationInit();
}
