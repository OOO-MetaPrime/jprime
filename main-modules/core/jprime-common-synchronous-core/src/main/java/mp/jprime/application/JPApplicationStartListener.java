package mp.jprime.application;

/**
 * Слушатель запуска JPrime-приложения
 * - отрабатывает после JPApplicationBootListener
 * - отрабатывает после JPApplicationInitListener
 */
public interface JPApplicationStartListener {
  /**
   * Обработка запуска приложения
   */
  void applicationStart();
}
