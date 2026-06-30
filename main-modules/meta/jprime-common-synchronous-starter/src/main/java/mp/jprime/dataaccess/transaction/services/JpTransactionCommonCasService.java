package mp.jprime.dataaccess.transaction.services;

import mp.jprime.dataaccess.transaction.ChainedTransactionManager;
import mp.jprime.dataaccess.transaction.JpTransactionCasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.Collection;

@Service
public final class JpTransactionCommonCasService implements JpTransactionCasService {
  private final ChainedTransactionManager transactionManager;

  private JpTransactionCommonCasService(@Autowired ChainedTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public void execute(Collection<String> storages) {
    TransactionStatus ts = transactionManager.getTransactionStatus(storages);
    try {
      transactionManager.commit(ts);
    } catch (Exception e) {
      transactionManager.rollback(ts);
      throw e;
    }
  }
}
