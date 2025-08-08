package mp.jprime.dataaccess.transaction;

import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPTransactionEventManager;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

@Service
public class ChainedTransactionManager implements PlatformTransactionManager {
  private final static Logger LOG = LoggerFactory.getLogger(ChainedTransactionManager.class);

  private static final ThreadLocal<TransactionInfo> TRANSACTION_INFO_THREAD_LOCAL = new NamedThreadLocal<>("Current transaction");

  private final LinkedHashMap<String, PlatformTransactionManager> transactionManagerMap = new LinkedHashMap<>();
  private final Set<PlatformTransactionManager> transactionManagers = new LinkedHashSet<>();
  private final List<PlatformTransactionManager> reverseTransactionManagers = new ArrayList<>();

  private JPTransactionEventManager transactionEventManager;

  private ChainedTransactionManager() {

  }

  @Autowired
  private void setTransactionEventManager(JPTransactionEventManager transactionEventManager) {
    this.transactionEventManager = transactionEventManager;
  }

  @Autowired
  private void setRepositoryGlobalStorage(RepositoryGlobalStorage repositoryStorage) {
    for (JPStorage storage : repositoryStorage.getStorages()) {
      TransactionManager tm = storage.getTransactionManager();
      if (tm instanceof PlatformTransactionManager) {
        transactionManagers.add((PlatformTransactionManager) tm);
        transactionManagerMap.put(storage.getCode(), (PlatformTransactionManager) tm);
      }
    }
    reverseTransactionManagers.addAll(transactionManagers);
    Collections.reverse(reverseTransactionManagers);
  }

  private TransactionInfo currentTransactionInfo() {
    return TRANSACTION_INFO_THREAD_LOCAL.get();
  }

  public void addCommitEvent(JPTransactionEvent event) {
    TransactionInfo info = currentTransactionInfo();
    if (info == null) {
      return;
    }
    info.addCommitEvent(event);
  }

  /*
   * При открытии транзакции сразу резервируются соединения во всех зарегистрированных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public MultiTransactionStatus getTransactionStatus() throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), (String[]) null);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public MultiTransactionStatus getTransactionStatus(String... dbCodes) throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), dbCodes);
  }

  /*
   * При открытии транзакции сразу резервируются соединения в указанных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public MultiTransactionStatus getTransactionStatus(Collection<String> dbCodes) throws TransactionException {
    return getTransaction(TransactionDefinition.withDefaults(), dbCodes);
  }

  /*
   * При открытии транзакции сразу резервируются соединения во всех зарегистрированных хранилищах
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public MultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
    return getTransaction(definition, (String[]) null);
  }

  /*
   * При открытии транзакциитранзакции сразу резервируются соединения в указанных хранилищах (или всех, если dbCodes = null)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public MultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition, String... dbCodes) throws TransactionException {
    Collection<String> codes = dbCodes != null && dbCodes.length > 0 ? Arrays.asList(dbCodes) : null;
    return getTransaction(definition, codes);
  }

  /*
   * При открытии транзакциитранзакции сразу резервируются соединения в указанных хранилищах (или всех, если dbCodes = null)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   * @param dbCodes Коды хранилищ для распределенной транзакции
   */
  public MultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition, Collection<String> dbCodes) throws TransactionException {
    // Определяем список хранилищ для транзакционности
    Set<PlatformTransactionManager> managers = new LinkedHashSet<>();
    for (Map.Entry<String, PlatformTransactionManager> entry : transactionManagerMap.entrySet()) {
      if (dbCodes != null && !dbCodes.contains(entry.getKey())) {
        continue;
      }
      managers.add(entry.getValue());
    }
    MultiTransactionStatus mts = new MultiTransactionStatus(managers.iterator().next());
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

      for (PlatformTransactionManager transactionManager : transactionManagers) {
        try {
          TransactionStatus status = transactionStatuses.get(transactionManager);
          if (status != null) {
            transactionManager.rollback(status);
          }
        } catch (Exception ex2) {
          LOG.warn("Rollback exception (" + transactionManager + ") " + ex2.getMessage(), ex2);
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
    MultiTransactionStatus mts = (MultiTransactionStatus) status;

    boolean commit = true;
    Exception commitException = null;
    PlatformTransactionManager commitExceptionTransactionManager = null;

    for (PlatformTransactionManager transactionManager : reverseTransactionManagers) {
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
          LOG.warn("Rollback exception (after commit) (" + transactionManager + ") " + ex.getMessage(), ex);
        }
      }
    }

    if (mts.isPrimaryTransaction()) {
      commitTransaction();
    }

    if (commitException != null) {
      boolean firstTransactionManagerFailed = commitExceptionTransactionManager == getLastTransactionManager();
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

    MultiTransactionStatus mts = (MultiTransactionStatus) status;

    for (PlatformTransactionManager transactionManager : reverseTransactionManagers) {
      try {
        mts.rollback(transactionManager);
      } catch (Exception ex) {
        if (rollbackException == null) {
          rollbackException = ex;
          rollbackExceptionTransactionManager = transactionManager;
        } else {
          LOG.warn("Rollback exception (" + transactionManager + ") " + ex.getMessage(), ex);
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

  private PlatformTransactionManager getLastTransactionManager() {
    return reverseTransactionManagers.get(0);
  }

  private void setTransaction() {
    TRANSACTION_INFO_THREAD_LOCAL.set(JPTransactionInfo.newInstance());
  }

  private void commitTransaction() {
    TransactionInfo info = currentTransactionInfo();
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