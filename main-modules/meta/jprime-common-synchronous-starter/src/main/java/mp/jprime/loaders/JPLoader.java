package mp.jprime.loaders;

/**
 * Логика статических загрузчиков
 */
public interface JPLoader<T> {
  /**
   * Читает данные
   * @return данные
   */
  T load();
}