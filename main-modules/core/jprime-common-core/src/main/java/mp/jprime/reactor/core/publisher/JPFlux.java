package mp.jprime.reactor.core.publisher;

import mp.jprime.concurrent.JPReactorScheduler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Обертка над reactor.core.publisher.Flux
 */
public final class JPFlux {
  private JPFlux() {

  }

  public static <T> Flux<T> fromStream(Supplier<Stream<? extends T>> streamSupplier) {
    Flux<T> flux = Flux.fromStream(streamSupplier);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Flux<T> fromStream(Stream<? extends T> s) {
    Flux<T> flux = Flux.fromStream(s);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Flux<T> fromCallable(Callable<Iterable<? extends T>> supplier) {
    Mono<Iterable<? extends T>> mono = JPMono.fromCallable(supplier);
    return mono
        .subscribeOn(JPReactorScheduler.reactorScheduler())
        .flatMapMany(Flux::fromIterable);
  }

  public static <T> Flux<T> concat(Iterable<? extends Publisher<? extends T>> sources) {
    Flux<T> flux = Flux.concat(sources);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Flux<T> defer(Supplier<? extends Publisher<T>> supplier) {
    Flux<T> flux = Flux.defer(supplier);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  public static <T> Flux<T> merge(Iterable<? extends Publisher<? extends T>> sources) {
    Flux<T> flux = Flux.merge(sources);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }

  @SafeVarargs
  public static <T> Flux<T> concat(Publisher<? extends T>... sources) {
    Flux<T> flux = Flux.concat(sources);
    return flux
        .subscribeOn(JPReactorScheduler.reactorScheduler());
  }
}
