package mp.jprime.caches.services;

import mp.jprime.caches.JPCache;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;

/**
 * Базовая реализация кэша
 */
public abstract class JPBaseCache implements JPCache {
  private final ReentrantLock lock = new ReentrantLock();
  private final AtomicBoolean initCache = new AtomicBoolean();

  @Override
  public void refresh() {
    if (!initCache.get()) { // Если это первая инициализация
      lock.lock();
      try {
        if (!initCache.get()) {
          this.load();
          initCache.set(true);
        }
      } finally {
        lock.unlock();
      }
    } else { // Повторная инициализация
      this.load();
    }
  }

  /**
   * Загрузка кеша
   */
  abstract protected void load();

  protected void waitForLoad() {
    if (!initCache.get() && !lock.isLocked()) {
      refresh();
    } else {
      waitForInit(() -> initCache.get() || lock.isHeldByCurrentThread());
    }
  }

  private void waitForInit(BooleanSupplier cacheEnabled) {
    if (cacheEnabled.getAsBoolean()) {
      return;
    }
    while (!cacheEnabled.getAsBoolean()) {
      // Ждём первичной инициализации кэша
      Thread.onSpinWait();
    }
  }
}
