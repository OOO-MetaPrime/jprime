package mp.jprime.util;

/**
 * Сервис завершения работы приложения
 */
public interface JPApplicationShutdownManager {
  /**
   * Завершить работу приложения с кодом {@code 0}
   */
  void exit();

  /**
   * Завершить работу приложения с кодом {@code 1}
   */
  void exitWithError();
}
