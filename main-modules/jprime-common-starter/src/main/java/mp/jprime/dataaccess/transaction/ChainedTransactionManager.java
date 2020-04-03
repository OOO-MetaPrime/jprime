package mp.jprime.dataaccess.transaction;

import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.services.RepositoryGlobalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ChainedTransactionManager implements PlatformTransactionManager {
  private final static Logger LOG = LoggerFactory.getLogger(ChainedTransactionManager.class);

  private final List<PlatformTransactionManager> transactionManagers = new ArrayList<>();
  private final List<PlatformTransactionManager> reverseTransactionManagers = new ArrayList<>();

  private ChainedTransactionManager() {

  }

  @Autowired
  private void setRepositoryGlobalStorage(RepositoryGlobalStorage repositoryStorage) {
    repositoryStorage.getStorages()
        .stream()
        .map(JPStorage::getTransactionManager)
        .filter(Objects::nonNull)
        .forEach(transactionManagers::add);
    reverseTransactionManagers.addAll(transactionManagers);
    Collections.reverse(reverseTransactionManagers);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public MultiTransactionStatus getTransactionStatus() throws TransactionException {
    return getTransaction(new DefaultTransactionDefinition());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.transaction.PlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
   */
  public MultiTransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
    MultiTransactionStatus mts = new MultiTransactionStatus(transactionManagers.get(0));
    if (definition == null) {
      return mts;
    }
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.initSynchronization();
      mts.setNewSynchonization();
    }
    try {
      transactionManagers.forEach(x -> mts.registerTransactionManager(definition, x));
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
      if (mts.isNewSynchonization()) {
        TransactionSynchronizationManager.clearSynchronization();
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
    MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;

    boolean commit = true;
    Exception commitException = null;
    PlatformTransactionManager commitExceptionTransactionManager = null;

    for (PlatformTransactionManager transactionManager : reverseTransactionManagers) {
      if (commit) {
        try {
          multiTransactionStatus.commit(transactionManager);
        } catch (Exception ex) {
          commit = false;
          commitException = ex;
          commitExceptionTransactionManager = transactionManager;
        }
      } else {
        try {
          multiTransactionStatus.rollback(transactionManager);
        } catch (Exception ex) {
          LOG.warn("Rollback exception (after commit) (" + transactionManager + ") " + ex.getMessage(), ex);
        }
      }
    }

    if (multiTransactionStatus.isNewSynchonization()) {
      TransactionSynchronizationManager.clearSynchronization();
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

    MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;

    for (PlatformTransactionManager transactionManager : reverseTransactionManagers) {
      try {
        multiTransactionStatus.rollback(transactionManager);
      } catch (Exception ex) {
        if (rollbackException == null) {
          rollbackException = ex;
          rollbackExceptionTransactionManager = transactionManager;
        } else {
          LOG.warn("Rollback exception (" + transactionManager + ") " + ex.getMessage(), ex);
        }
      }
    }

    if (multiTransactionStatus.isNewSynchonization()) {
      TransactionSynchronizationManager.clearSynchronization();
    }

    if (rollbackException != null) {
      throw new UnexpectedRollbackException("Rollback exception, originated at (" + rollbackExceptionTransactionManager
          + ") " + rollbackException.getMessage(), rollbackException);
    }
  }

  private PlatformTransactionManager getLastTransactionManager() {
    return transactionManagers.get(lastTransactionManagerIndex());
  }

  private int lastTransactionManagerIndex() {
    return transactionManagers.size() - 1;
  }
}