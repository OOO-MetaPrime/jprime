package mp.jprime.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ForkJoinPool} для решений на базе JPrime
 * <p/>
 * Рекомендуется к использованию для запуска асинхронных операций
 */
public final class JPForkJoinPoolService {
  private final static int MAX_POOL_PARALLELISM = 64;
  private final static ForkJoinPool POOL = ofThreads("JPForkJoinWorker", MAX_POOL_PARALLELISM);


  private static ForkJoinPool ofThreads(String prefix, int parallelism) {
    return new ForkJoinPool(
        Math.min(parallelism, Runtime.getRuntime().availableProcessors()),
        new JPForkJoinWorkerThreadFactory(prefix),
        null,
        false
    );
  }

  /**
   * Возвращает {@link ForkJoinPool}
   *
   * @return {@link ForkJoinPool}
   */
  public static ForkJoinPool pool() {
    return POOL;
  }

  /**
   * Возвращает {@link ForkJoinPool} с указанным параллелизмом, но не более {@link Runtime#availableProcessors}
   *
   * @param name        имя группы потоков
   * @param parallelism параллелизм
   * @return {@link ForkJoinPool}
   */
  public static ForkJoinPool pool(String name, int parallelism) {
    return ofThreads(name, parallelism);
  }

  private static final class JPForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
    private final AtomicInteger sequence = new AtomicInteger(1);
    private final String prefix;

    private JPForkJoinWorkerThreadFactory(String prefix) {
      this.prefix = prefix;
    }

    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
      return new JPForkJoinWorkerThread(prefix + '-' + sequence.getAndIncrement(), pool);
    }
  }

  private static final class JPForkJoinWorkerThread extends ForkJoinWorkerThread {
    JPForkJoinWorkerThread(String name, ForkJoinPool pool) {
      super(pool);
      this.setName(name);
      this.setContextClassLoader(this.getClass().getClassLoader());
    }
  }
}
