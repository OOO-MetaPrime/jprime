package mp.jprime.loaders;

import java.util.Collection;

/**
 * Логика базовых загрузчиков
 */
public interface JPMultiLoader<T> {
  /**
   * Читает данные
   * @return данные
   */
  Collection<T> load();
}