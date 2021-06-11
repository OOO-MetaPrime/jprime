package mp.jprime.loaders;

import reactor.core.publisher.Flux;

/**
 * Логика базовых загрузчиков
 */
public interface JPLoader<T> {
  /**
   * Читает данные
   * @return данные
   */
  Flux<T> load();
}