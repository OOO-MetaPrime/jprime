package mp.jprime.repositories;

import mp.jprime.exceptions.JPQueryServiceException;
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

  /**
   * TransactionManager
   *
   * @return TransactionManager
   */
  default TransactionManager getTransactionManagerOrThrow() {
    TransactionManager transactionManager = getTransactionManager();
    if (transactionManager == null) {
      throw JPQueryServiceException.fromTransactionManager(getCode());
    }
    return transactionManager;
  }
}