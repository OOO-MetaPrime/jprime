package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPObjectAccessService
 */
public interface JPObjectAccessServiceAware extends Aware {
  /**
   * Устанавливает   JPObjectAccessService
   *
   * @param accessService JPObjectAccessService
   */
  void setJpObjectAccessService(JPObjectAccessService accessService);
}
