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
   * Признак поддержки транзакции
   */
  default boolean isTransactionSupport() {
    return false;
  }

  /**
   * TransactionManager
   *
   * @return TransactionManager
   */
  default TransactionManager getTransactionManager() {
    return null;
  }
}