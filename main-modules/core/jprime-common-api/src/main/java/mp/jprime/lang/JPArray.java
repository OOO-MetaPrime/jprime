package mp.jprime.lang;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Базовая логика для массивов
 *
 * @param <T> Класс значений
 */
public abstract class JPArray<T extends Comparable> implements Serializable, Comparable<JPArray<T>> {
  private final List<T> values;

  protected JPArray(List<T> values) {
    this.values = values == null ? Collections.emptyList() : Collections.unmodifiableList(values);
  }

  protected JPArray(T[] values) {
    this.values = values == null || values.length == 0 ? Collections.emptyList() : Collections.unmodifiableList(Arrays.asList(values));
  }

  protected List<T> getValues() {
    return values;
  }

  /**
   * Конвертация в массив
   *
   * @return Массив значений
   */
  public abstract T[] toArray();

  /**
   * Конвертация в список
   *
   * @return Список значений
   */
  public List<T> toList() {
    return values;
  }

  @Override
  public int compareTo(JPArray<T> o) {
    if (o == null) {
      return -1;
    }
    return CollectionUtils.isEqualCollection(values, o.toList()) ? 0 : -1;
  }
}
