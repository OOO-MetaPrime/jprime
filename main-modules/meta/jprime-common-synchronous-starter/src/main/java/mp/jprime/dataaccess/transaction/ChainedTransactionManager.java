package mp.jprime.dataaccess.transaction;

import jakarta.annotation.Nullable;
import mp.jprime.dataaccess.transaction.beans.JpTransactionInfoBean;
import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPTransactionEventManager;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

@Service
public class ChainedTransactionManager implements PlatformTransactionManager {
  private final static Logger LOG = LoggerFactory.getLogger(ChainedTransactionManager.class);

  private final static ThreadLocal<JpTransactionInfo> TRANSACTION_INFO_THREAD_LOCAL = new NamedThreadLocal<>("Current transaction");

  private final Map<String, JPStorage> transactionManagerMap = new LinkedHashMap<>();
  private final JPTransactionEventManager transactionEventManager;

  private ChainedTransactionManager(@Autowired JPTransactionEventManager transactionEventManager,
                                    @Autowired RepositoryGlobalStorage repositoryStorage) {
    this.transactionEventManager = transactionEventManager;

    for (JPStorage storage : repositoryStorage.getStorages()) {
      if (!storage.isTransactionSupport()) {
        continue;
      }
      transactionManagerMap.put(storage.getCode(), storage);
    }
  }

  private JpTransactionInfo currentTransactionInfo() {
    return TRANSACTION_INFO_THREAD_LOCAL.get();
  }

  public void addCommitEvent(JPTransactionEvent event) {
    JpTransactionInfo info = currentTransactionInfo();
    if (info == null) {
      return;
    }
    info.addCommitEvent(event);
  }

  /*
   * При открытии транзакции сразу резервируются соединения во всех зарегистрированных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public JpMultiTransactionStatus getTransactionStatus() throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), (String[]) null);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public JpMultiTransactionStatus getTransactionStatus(String... dbCodes) throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), dbCodes);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public JpMultiTransactionStatus getTransactionStatus(Collection<String> dbCodes) throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), dbCodes);
  }

  /*
   * При открытии транзакции сразу резервируются соединения во всех зарегистрированных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public JpMultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
    return getTransaction(definition, (String[]) null);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах (или всех, если dbCodes = null)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public JpMultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition, String... dbCodes) throws TransactionException {
    Collection<String> codes = dbCodes != null && dbCodes.length > 0 ? Arrays.asList(dbCodes) : null;
    return getTransaction(definition, codes);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах (или всех, если dbCodes = null)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public JpMultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition, Collection<String> dbCodes) throws TransactionException {
    // Определяем список хранилищ для транзакционности
    Collection<PlatformTransactionManager> managers = new LinkedHashSet<>();
    for (Map.Entry<String, JPStorage> entry : transactionManagerMap.entrySet()) {
      if (dbCodes != null && !dbCodes.contains(entry.getKey())) {
        continue;
      }
      TransactionManager tm = entry.getValue().getTransactionManager();
      if (tm instanceof PlatformTransactionManager x) {
        managers.add(x);
      }
    }

    JpMultiTransactionStatus mts = new JpMultiTransactionStatus(managers.iterator().next());
    if (definition == null) {
      return mts;
    }
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.initSynchronization();
      mts.setPrimaryTransaction();
    }
    try {
      // Открываем соединения
      for (PlatformTransactionManager ptm : managers) {
        mts.registerTransactionManager(definition, ptm);
      }
      if (mts.isPrimaryTransaction()) {
        setTransaction();
      }
    } catch (Exception ex) {
      Map<PlatformTransactionManager, TransactionStatus> transactionStatuses = mts.getTransactionStatuses();

      for (PlatformTransactionManager transactionManager : managers) {
        try {
          TransactionStatus status = transactionStatuses.get(transactionManager);
          if (status != null) {
            transactionManager.rollback(status);
          }
        } catch (Exception ex2) {
          LOG.warn("Rollback exception ({}) {}", transactionManager, ex2.getMessage(), ex2);
        }
      }
      if (mts.isPrimaryTransaction()) {
        removeTransaction();
      }
      throw new CannotCreateTransactionException(ex.getMessage(), ex);
    }
    return mts;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.PlatformTransactionManager#commit(org.springframework.transaction.TransactionStatus)
   */
  public void commit(TransactionStatus status) throws TransactionException {
    boolean commit = true;
    Exception commitException = null;
    PlatformTransactionManager commitExceptionTransactionManager = null;

    JpMultiTransactionStatus mts = (JpMultiTransactionStatus) status;

    List<PlatformTransactionManager> managers = new ArrayList<>(mts.getTransactionManagerList());
    Collections.reverse(managers);

    for (PlatformTransactionManager transactionManager : managers) {
      if (commit) {
        try {
          mts.commit(transactionManager);
        } catch (Exception ex) {
          commit = false;
          commitException = ex;
          commitExceptionTransactionManager = transactionManager;
        }
      } else {
        try {
          mts.rollback(transactionManager);
        } catch (Exception ex) {
          LOG.warn("Rollback exception (after commit) ({}) {}", transactionManager, ex.getMessage(), ex);
        }
      }
    }

    if (mts.isPrimaryTransaction()) {
      commitTransaction();
    }

    if (commitException != null) {
      boolean firstTransactionManagerFailed = commitExceptionTransactionManager == managers.get(0);
      int transactionState = firstTransactionManagerFailed ?
          HeuristicCompletionException.STATE_ROLLED_BACK : HeuristicCompletionException.STATE_MIXED;
      throw new HeuristicCompletionException(transactionState, commitException);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.PlatformTransactionManager#rollback(org.springframework.transaction.TransactionStatus)
   */
  public void rollback(TransactionStatus status) throws TransactionException {
    Exception rollbackException = null;
    PlatformTransactionManager rollbackExceptionTransactionManager = null;

    JpMultiTransactionStatus mts = (JpMultiTransactionStatus) status;

    List<PlatformTransactionManager> managers = new ArrayList<>(mts.getTransactionManagerList());
    Collections.reverse(managers);

    for (PlatformTransactionManager transactionManager : managers) {
      try {
        mts.rollback(transactionManager);
      } catch (Exception ex) {
        if (rollbackException == null) {
          rollbackException = ex;
          rollbackExceptionTransactionManager = transactionManager;
        } else {
          LOG.warn("Rollback exception ({}) {}", transactionManager, ex.getMessage(), ex);
        }
      }
    }

    if (mts.isPrimaryTransaction()) {
      removeTransaction();
    }

    if (rollbackException != null) {
      throw new UnexpectedRollbackException("Rollback exception, originated at (" + rollbackExceptionTransactionManager
          + ") " + rollbackException.getMessage(), rollbackException);
    }
  }

  private void setTransaction() {
    TRANSACTION_INFO_THREAD_LOCAL.set(JpTransactionInfoBean.newInstance());
  }

  private void commitTransaction() {
    JpTransactionInfo info = currentTransactionInfo();
    Collection<JPTransactionEvent> events = info != null ? info.getCommitEvents() : null;
    if (events != null && !events.isEmpty()) {
      transactionEventManager.fireEvents(events);
    }
    removeTransaction();
  }

  private void removeTransaction() {
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.clearSynchronization();
    }
    TRANSACTION_INFO_THREAD_LOCAL.remove();
  }
}