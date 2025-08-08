package mp.jprime.configurations;

import mp.jprime.dataaccess.params.JPSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Базовые настройки запросов
 * jprime.query.queryTimeout - Время ожидания запроса
 * jprime.api.checkLimit     - Проверка максимального количества (limit) в выборке
 * jprime.api.maxLimit       - Максимальное количество в выборке через api
 */
public abstract class JPQuerySettings {
  protected static final Logger LOG = LoggerFactory.getLogger(JPQuerySettings.class);

  @Value("${jprime.query.queryTimeout:}")
  private Integer queryTimeout;
  @Value("${jprime.api.checkLimit:true}")
  private boolean checkLimit;
  @Value("${jprime.api.maxLimit:1000}")
  private Integer maxLimit;

  /**
   * Время ожидания запроса
   *
   * @return Кол-во секунд
   */
  protected Integer getQueryTimeout() {
    return queryTimeout;
  }

  /**
   * Проверка максимального количества (limit) в выборке
   *
   * @return Да/Нет
   */
  protected boolean isCheckLimit() {
    return checkLimit;
  }

  /**
   * Максимальное количество в выборке через api
   *
   * @return Количество записей
   */
  protected Integer getMaxLimit() {
    return maxLimit;
  }

  protected JPSelect checkAndBuild(JPSelect.Builder builder) {
    Integer limit = builder.limit();
    if (checkLimit && limit != null && limit > maxLimit) {
      LOG.warn("Warning. Select query limit for {} exceeded: {}", builder.getJpClass(), limit);
      builder.limit(maxLimit);
    }
    return builder.build();
  }
}
