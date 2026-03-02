package mp.jprime.application;

/**
 * Слушатель загрузки JPrime-приложения
 * - отрабатывает до JPApplicationInitListener
 * - отрабатывает до JPApplicationStartListener
 */
public interface JPApplicationBootListener {
  /**
   * Обработка загрузка приложения
   */
  void applicationBoot();
}
