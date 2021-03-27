package mp.jprime.repositories;

import org.springframework.transaction.TransactionManager;

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
   * TransactionManager
   *
   * @return TransactionManager
   */
  default TransactionManager getTransactionManager() {
    return null;
  }
}