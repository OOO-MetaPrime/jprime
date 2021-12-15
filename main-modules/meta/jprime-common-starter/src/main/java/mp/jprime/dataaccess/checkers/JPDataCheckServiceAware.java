package mp.jprime.dataaccess.checkers;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение JPDataCheckService
 */
public interface JPDataCheckServiceAware extends Aware {
  /**
   * Устанавливает  JPDataCheckService
   *
   * @param jpDataCheckService JPDataCheckService
   */
  void setJpDataCheckService(JPDataCheckService jpDataCheckService);
}
