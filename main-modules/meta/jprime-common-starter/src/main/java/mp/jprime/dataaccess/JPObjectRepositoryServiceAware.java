package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPObjectRepositoryService
 */
public interface JPObjectRepositoryServiceAware extends Aware {
  /**
   * Устанавливает   JPObjectRepositoryService
   *
   * @param repositoryService JPObjectRepositoryService
   */
  void setJpObjectRepositoryService(JPObjectRepositoryService repositoryService);
}
