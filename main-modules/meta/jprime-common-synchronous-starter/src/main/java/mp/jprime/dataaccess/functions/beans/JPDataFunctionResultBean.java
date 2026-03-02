package mp.jprime.dataaccess.functions.beans;

import mp.jprime.dataaccess.functions.JPDataFunctionResult;

/**
 * Результат работы функции
 *
 * @param <V> Тип значения
 */
public class JPDataFunctionResultBean<V> implements JPDataFunctionResult<V> {
  private final V result;
  private final String format;

  private JPDataFunctionResultBean(V result, String format) {
    this.result = result;
    this.format = format;
  }

  public static <V> JPDataFunctionResult<V> of(V result, String format) {
    return new JPDataFunctionResultBean<>(result, format);
  }

  public static <V> JPDataFunctionResult<V> of(V result) {
    return new JPDataFunctionResultBean<>(result, null);
  }

  @Override
  public V getResult() {
    return result;
  }

  @Override
  public String getFormat() {
    return format;
  }
}
