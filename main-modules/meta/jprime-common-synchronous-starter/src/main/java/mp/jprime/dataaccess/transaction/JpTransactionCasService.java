package mp.jprime.dataaccess.transaction;

import java.util.Collection;

/**
 * Транзакционный CAS
 */
public interface JpTransactionCasService {
  /**
   * Выполнение
   *
   * @param storages Хранилища, к которым идет обращение
   */
  void execute(Collection<String> storages);
              // prepareAction,
             //  caseAction,
             //  setAction,);
}
