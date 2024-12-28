package mp.jprime.dataaccess;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение  JPSubQueryService
 */
public interface JPSubQueryServiceAware extends Aware {
  /**
   * Устанавливает   JPSubQueryService
   *
   * @param service JPSubQueryService
   */
  void setJpSubQueryService(JPSubQueryService service);
}
