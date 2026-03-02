package mp.jprime.reactor.core.publisher;

import mp.jprime.concurrent.JPReactorScheduler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Обертка над reactor.core.publisher.Mono
 */
public final class JPMono {
  public static <T> Mono<T> create(Consumer<MonoSink<T>> callback) {
    Mono<T> mono = Mono.create(callback);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Mono<T> defer(Supplier<? extends Mono<? extends T>> supplier) {
    Mono<T> mono = Mono.defer(supplier);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Mono<T> from(Publisher<? extends T> source) {
    Mono<T> mono = Mono.from(source);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

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

  public static <T1, T2, O> Mono<O> zip(Mono<? extends T1> p1,
                                        Mono<? extends T2> p2,
                                        BiFunction<? super T1, ? super T2, ? extends O> combinator) {
    Mono<O> mono = Mono.zip(p1, p2, combinator);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <R> Mono<R> zip(Iterable<? extends Mono<?>> monos, Function<? super Object[], ? extends R> combinator) {
    Mono<R> mono = Mono.zip(monos, combinator);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }
}
