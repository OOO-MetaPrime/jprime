package mp.jprime.exceptions;

import java.util.Collection;

/**
 * Интерфейс обобщенного Exception
 * @param <T> Тип Exception
 */
public interface CompositeException<T extends Exception> {
  /**
   * Данные ошибок
   *
   * @return Данные ошибок
   */
  Collection<T> getExceptions();
}
