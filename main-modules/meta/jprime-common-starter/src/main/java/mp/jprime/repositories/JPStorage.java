package mp.jprime.repositories;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * Описание типового хранилища
 */
public interface JPStorage {
  /**
   * Возвращает кодовое имя хранилища
   *
   * @return Кодовое имя
   */
  String getCode();

  /**
   * Возвращает название хранилища
   *
   * @return Название
   */
  String getTitle();

  /**
   * DataSourceTransactionManager
   *
   * @return DataSourceTransactionManager
   */
  default PlatformTransactionManager getTransactionManager() {
    return null;
  }
}