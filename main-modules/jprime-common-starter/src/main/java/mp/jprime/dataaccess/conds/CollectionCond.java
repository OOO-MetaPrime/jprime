package mp.jprime.dataaccess.conds;

import java.util.Collection;

/**
 * Условие с массивом
 *
 * @param <T>
 */
public interface CollectionCond<T> extends ValueCond<Collection<T>> {
  /**
   * Проверяет значение на пересечение
   *
   * @param value значение
   * @return Да/Нет
   */
  boolean check(Collection<T> value);

  /**
   * Проверяет значение на содержимое
   *
   * @param value значение
   * @return Да/Нет
   */
  boolean check(T value);
}
