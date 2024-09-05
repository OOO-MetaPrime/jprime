package mp.jprime.caches.services;

import mp.jprime.caches.JPCache;
import mp.jprime.exceptions.JPRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Базовая реализация кэша
 */
public abstract class JPBaseCache<C, V> implements JPCache<C, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPBaseCache.class);

  private volatile Map<C, V> cache;

  @Value("${jprime.cache.load.timeout:20}")
  private int timeout;

  @Override
  public void refresh() {
    this.cache = loadCache();
    LOG.info("Cache {} refreshed", getCode());
    afterRefresh();
  }

  @Override
  public Collection<V> getValues() {
    waitForInit();
    return cache.values();
  }

  @Override
  public V getByCode(C code) {
    waitForInit();
    return cache.get(code);
  }

  /**
   * Действие после обновления кеша
   */
  protected void afterRefresh() {

  }

  /**
   * Получить кэшируемые значения
   */
  protected abstract Map<C, V> loadCache();

  /**
   * Получить таймаут загрузки кэша
   *
   * @return Таймаут загрузки кэша
   */
  protected int getLoadTimeout() {
    return timeout;
  }

  /**
   * Неблокирующее ожидание инициализации кэша до {@link JPBaseCache#getLoadTimeout() таймаута}.
   * Если кэш не был проинициализирован до истечения таймаута, бросается {@link JPRuntimeException}
   */
  protected void waitForInit() {
    waitForInit(() -> cache != null);
  }

  /**
   * Неблокирующее ожидание инициализации кэша до {@link JPBaseCache#getLoadTimeout() таймаута}.
   * Если кэш не был проинициализирован до истечения таймаута, бросается {@link JPRuntimeException}
   *
   * @param cacheEnabled функция проверки загрузки кеша
   */
  protected void waitForInit(BooleanSupplier cacheEnabled) {
    if (cacheEnabled.getAsBoolean()) {
      return;
    }
    LocalDateTime deadLine = LocalDateTime.now().plusSeconds(getLoadTimeout());
    while (!cacheEnabled.getAsBoolean()) {
      if (deadLine.isBefore(LocalDateTime.now())) {
        throw new JPRuntimeException("jp.cache.load.timeout", "Истекло время ожидания загрузки кэша \"" + getCode() + '"');
      }
      // Ждём первичной инициализации кэша
      Thread.onSpinWait();
    }
  }
}
