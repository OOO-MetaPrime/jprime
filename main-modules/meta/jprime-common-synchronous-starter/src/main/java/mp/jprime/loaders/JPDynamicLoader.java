package mp.jprime.loaders;

import java.util.function.Consumer;

/**
 * Логика динамических загрузчиков
 */
public interface JPDynamicLoader<T> {
  /**
   * Подписка на данные
   *
   * @param consumer Обработчик
   */
  void subscribe(Consumer<T> consumer);
}