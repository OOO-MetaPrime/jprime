package mp.jprime.json.versioning.beans;

import mp.jprime.json.versioning.JPJsonBeanVersion;

/**
 * Данные в формате указанной версии
 */
public final class JPJsonBeanVersionBean<T> implements JPJsonBeanVersion<T> {
  /**
   * Номер версии
   */
  private final Integer version;
  /**
   * Данные в формате версии
   */
  private final T data;

  private JPJsonBeanVersionBean(Integer version, T data) {
    this.version = version;
    this.data = data;
  }


  public static <T> JPJsonBeanVersion<T> of(Integer version, T data) {
    return new JPJsonBeanVersionBean<T>(version, data);
  }

  @Override
  public Integer getVersion() {
    return version;
  }

  @Override
  public T getData() {
    return data;
  }
}
