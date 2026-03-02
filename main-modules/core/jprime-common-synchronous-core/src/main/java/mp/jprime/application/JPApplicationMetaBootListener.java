package mp.jprime.application;

/**
 * Слушатель загрузки меты JPrime-приложения
 * - отрабатывает до JPApplicationBootListener
 * - отрабатывает до JPApplicationInitListener
 * - отрабатывает до JPApplicationStartListener
 */
public interface JPApplicationMetaBootListener {
  /**
   * Обработка загрузка приложения
   */
  void applicationBoot();
}
