package mp.jprime.loaders;

import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * Логика базовых загрузчиков
 */
public interface JPMultiLoader<T> {
  /**
   * Читает данные
   * @return данные
   */
  Collection<Flux<T>> load();
}