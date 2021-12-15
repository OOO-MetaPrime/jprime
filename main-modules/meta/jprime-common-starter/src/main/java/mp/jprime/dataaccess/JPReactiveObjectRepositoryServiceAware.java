package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPReactiveObjectRepositoryService
 */
public interface JPReactiveObjectRepositoryServiceAware extends Aware {
  /**
   * Устанавливает   JPReactiveObjectRepositoryService
   *
   * @param repositoryService JPReactiveObjectRepositoryService
   */
  void setJpReactiveObjectRepositoryService(JPReactiveObjectRepositoryService repositoryService);
}
