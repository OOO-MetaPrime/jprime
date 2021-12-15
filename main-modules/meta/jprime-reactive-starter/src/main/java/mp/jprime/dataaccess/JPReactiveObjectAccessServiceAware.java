package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPReactiveObjectAccessService
 */
public interface JPReactiveObjectAccessServiceAware extends Aware {
  /**
   * Устанавливает   JPReactiveObjectAccessService
   *
   * @param accessService JPReactiveObjectAccessService
   */
  void setJpReactiveObjectAccessService(JPReactiveObjectAccessService accessService);
}
