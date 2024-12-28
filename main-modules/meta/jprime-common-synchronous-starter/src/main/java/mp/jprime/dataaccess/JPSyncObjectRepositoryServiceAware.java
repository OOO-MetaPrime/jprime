package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPSyncObjectRepositoryService
 */
public interface JPSyncObjectRepositoryServiceAware extends Aware {
  /**
   * Устанавливает   JPSyncObjectRepositoryService
   *
   * @param repo JPSyncObjectRepositoryService
   */
  void setJpSyncObjectRepositoryService(JPSyncObjectRepositoryService repo);
}
