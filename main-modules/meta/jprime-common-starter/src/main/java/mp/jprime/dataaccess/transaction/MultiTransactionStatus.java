package mp.jprime.dataaccess.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link TransactionStatus} implementation to orchestrate {@link TransactionStatus} instances for multiple
 * {@link PlatformTransactionManager} instances.
 */
class MultiTransactionStatus implements TransactionStatus {
  private final PlatformTransactionManager mainTransactionManager;
  private final Map<PlatformTransactionManager, TransactionStatus> transactionStatuses = new ConcurrentHashMap<>();

  private boolean primaryTransaction;

  /**
   * Creates a new {@link MultiTransactionStatus} for the given {@link PlatformTransactionManager}.
   *
   * @param mainTransactionManager must not be {@literal null}.
   */
  MultiTransactionStatus(PlatformTransactionManager mainTransactionManager) {
    Assert.notNull(mainTransactionManager, "TransactionManager must not be null!");
    this.mainTransactionManager = mainTransactionManager;
  }

  Map<PlatformTransactionManager, TransactionStatus> getTransactionStatuses() {
    return transactionStatuses;
  }

  void setPrimaryTransaction() {
    this.primaryTransaction = true;
  }

  boolean isPrimaryTransaction() {
    return primaryTransaction;
  }

  void registerTransactionManager(TransactionDefinition definition, PlatformTransactionManager transactionManager) {
    getTransactionStatuses().put(transactionManager, transactionManager.getTransaction(definition));
  }

  void commit(PlatformTransactionManager transactionManager) {
    TransactionStatus status = getTransactionStatus(transactionManager);
    if (!status.isCompleted()) {
      transactionManager.commit(status);
    }
  }

  /**
   * Rolls back the {@link TransactionStatus} registered for the given {@link PlatformTransactionManager}.
   *
   * @param transactionManager must not be {@literal null}.
   */
  void rollback(PlatformTransactionManager transactionManager) {
    TransactionStatus status = getTransactionStatus(transactionManager);
    if (!status.isCompleted()) {
      transactionManager.rollback(status);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#isRollbackOnly()
   */
  @Override
  public boolean isRollbackOnly() {
    return getMainTransactionStatus().isRollbackOnly();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#isCompleted()
   */
  @Override
  public boolean isCompleted() {
    return getMainTransactionStatus().isCompleted();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#isNewTransaction()
   */
  @Override
  public boolean isNewTransaction() {
    return getMainTransactionStatus().isNewTransaction();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#hasSavepoint()
   */
  @Override
  public boolean hasSavepoint() {
    return getMainTransactionStatus().hasSavepoint();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#setRollbackOnly()
   */
  @Override
  public void setRollbackOnly() {
    for (TransactionStatus ts : transactionStatuses.values()) {
      ts.setRollbackOnly();
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.SavepointManager#createSavepoint()
   */
  @Override
  public Object createSavepoint() throws TransactionException {

    SavePoints savePoints = new SavePoints();

    for (TransactionStatus transactionStatus : transactionStatuses.values()) {
      savePoints.save(transactionStatus);
    }
    return savePoints;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.SavepointManager#rollbackToSavepoint(java.lang.Object)
   */
  @Override
  public void rollbackToSavepoint(Object savepoint) throws TransactionException {
    SavePoints savePoints = (SavePoints) savepoint;
    savePoints.rollback();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.SavepointManager#releaseSavepoint(java.lang.Object)
   */
  @Override
  public void releaseSavepoint(Object savepoint) throws TransactionException {
    ((SavePoints) savepoint).release();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.TransactionStatus#flush()
   */
  @Override
  public void flush() {
    for (TransactionStatus transactionStatus : transactionStatuses.values()) {
      transactionStatus.flush();
    }
  }

  private TransactionStatus getMainTransactionStatus() {
    return transactionStatuses.get(mainTransactionManager);
  }

  private TransactionStatus getTransactionStatus(PlatformTransactionManager transactionManager) {
    return this.getTransactionStatuses().get(transactionManager);
  }

  private static class SavePoints {

    private final Map<TransactionStatus, Object> savepoints = new HashMap<>();

    private void addSavePoint(TransactionStatus status, Object savepoint) {

      Assert.notNull(status, "TransactionStatus must not be null!");
      this.savepoints.put(status, savepoint);
    }

    private void save(TransactionStatus transactionStatus) {
      Object savepoint = transactionStatus.createSavepoint();
      addSavePoint(transactionStatus, savepoint);
    }

    public void rollback() {
      for (TransactionStatus transactionStatus : savepoints.keySet()) {
        transactionStatus.rollbackToSavepoint(savepointFor(transactionStatus));
      }
    }

    private Object savepointFor(TransactionStatus transactionStatus) {
      return savepoints.get(transactionStatus);
    }

    void release() {
      for (TransactionStatus transactionStatus : savepoints.keySet()) {
        transactionStatus.releaseSavepoint(savepointFor(transactionStatus));
      }
    }
  }
}
