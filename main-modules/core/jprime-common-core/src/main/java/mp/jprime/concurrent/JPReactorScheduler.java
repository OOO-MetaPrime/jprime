package mp.jprime.concurrent;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * {@link Scheduler} для решений на базе JPrime
 * <p/>
 * Рекомендуется к использованию для запуска асинхронных операций
 */
public final class JPReactorScheduler {
  private final static Scheduler REACTOR_SCHEDULER = Schedulers.fromExecutorService(JPForkJoinPoolService.pool(), "JPForkJoinScheduler");

  private JPReactorScheduler() {

  }

  /**
   * Возвращает {@link Scheduler}
   *
   * @return {@link Scheduler}
   */
  public static Scheduler reactorScheduler() {
    return REACTOR_SCHEDULER;
  }
}
