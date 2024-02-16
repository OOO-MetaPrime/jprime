package mp.jprime.imex.csvwriter;

import java.io.Closeable;
import java.util.Collection;

/**
 * Сервис выгрузки данных в CSV
 */
public interface JPCsvWriter<T> extends Closeable {
  /**
   * Записать и очистить буфер
   *
   * @param values Значения, которые нужно записать
   */
  void write(Collection<T> values);
}
