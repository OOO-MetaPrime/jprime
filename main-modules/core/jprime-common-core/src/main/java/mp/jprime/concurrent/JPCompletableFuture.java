package mp.jprime.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Обертка над CompletableFuture с использованием JPForkJoinPoolService
 */
public final class JPCompletableFuture {
  /**
   * Вызов runAsync на пуле JPForkJoinPoolService
   *
   * @param runnable Runnable
   * @return CompletableFuture
   */
  public static CompletableFuture<Void> runAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, JPForkJoinPoolService.pool());
  }

  /**
   * Вызов supplyAsync на пуле JPForkJoinPoolService
   *
   * @param supplier Supplier
   * @return CompletableFuture
   */
  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
    return CompletableFuture.supplyAsync(supplier, JPForkJoinPoolService.pool());
  }
}
