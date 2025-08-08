package mp.jprime.reactor.core.publisher;

import mp.jprime.concurrent.JPReactorScheduler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

/**
 * Обертка над reactor.core.publisher.Mono
 */
public final class JPMono {
  public static <T> Mono<T> fromCallable(Callable<? extends T> supplier) {
    Mono<T> mono = Mono.fromCallable(supplier);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Mono<T> fromRunnable(Runnable runnable) {
    Mono<T> mono = Mono.fromRunnable(runnable);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Mono<T> from(Publisher<? extends T> source) {
    Mono<T> mono = Mono.from(source);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }
}
