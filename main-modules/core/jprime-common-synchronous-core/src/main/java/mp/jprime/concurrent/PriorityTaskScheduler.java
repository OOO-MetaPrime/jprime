package mp.jprime.concurrent;

import mp.jprime.exceptions.JPRuntimeException;

import java.util.Collection;
import java.util.concurrent.*;

/**
 * Планировщик задач с приоритетом
 */
public class PriorityTaskScheduler {
  private final ExecutorService poolExecutor;
  private final ExecutorService taskScheduler = Executors.newSingleThreadExecutor();
  private final PriorityBlockingQueue<PriorityTask> priorityQueue;

  public PriorityTaskScheduler(Integer poolSize, String threadsNamePrefix, Integer initialQueueCapacity) {
    poolExecutor = Executors.newFixedThreadPool(
        poolSize,
        ContextClassLoaderThreadFactoryWrapper.of(
            NamedThreadFactory.of(threadsNamePrefix),
            this.getClass().getClassLoader()
        )
    );
    priorityQueue = new PriorityBlockingQueue<>(initialQueueCapacity, PriorityTask::compareTo);
  }

  /**
   * Запускает планировщик
   */
  public void run() {
    taskScheduler.execute(() -> {
      while (true) {
        try {
          poolExecutor.execute(priorityQueue.take());
        } catch (InterruptedException e) {
          break;
        }
      }
    });
  }

  /**
   * Останавливает планировщик
   */
  public void shutdown() {
    Exception e = null;
    try {
      taskScheduler.shutdownNow();
    } catch (Exception ex) {
      e = ex;
    }
    try {
      poolExecutor.shutdown();
    } catch (Exception ex) {
      if (e != null) {
        e = new IllegalStateException(ex.getMessage(), new RuntimeException(e.getMessage(), e));
      } else {
        e = ex;
      }
    }
    if (e != null) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Добавляет задачу в стек выполнения
   * @param task задача
   */
  public void schedule(PriorityTask task) {
    priorityQueue.add(task);
  }

  /**
   * Добавляет задачи в стек выполнения
   * @param tasks задачи
   */
  public void schedule(Collection<PriorityTask> tasks) {
    priorityQueue.addAll(tasks);
  }
}