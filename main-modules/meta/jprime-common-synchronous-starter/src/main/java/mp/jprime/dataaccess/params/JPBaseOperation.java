package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.security.AuthInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс для параметров CRUD операций
 */
abstract public class JPBaseOperation {
  private final Source source;
  private final Map<String, String> props;
  private final AuthInfo auth;

  protected JPBaseOperation(Source source, AuthInfo auth, Map<String, String> props) {
    this.source = source;
    this.auth = auth;
    this.props = props == null ? new HashMap<>() : new HashMap<>(props);
  }

  /**
   * Источник данных
   *
   * @return Источник данных
   */
  public Source getSource() {
    return source != null ? source : Source.SYSTEM;
  }

  /**
   * Добавляет дополнительное свойство
   *
   * @param key   Ключ
   * @param value Значение
   */
  public void setProperty(String key, String value) {
    props.put(key, value);
  }

  /**
   * Возвращает дополнительное свойство
   *
   * @param key Ключ
   * @return Значение
   */
  public String getProperty(String key) {
    return props.get(key);
  }

  /**
   * Признак заполнености свойства
   *
   * @param key Ключ
   * @return Да/Нет
   */
  public boolean containsProperty(String key) {
    return props.containsKey(key);
  }

  /**
   * Данные авторизации
   * Могут быть не указаны
   *
   * @return Данные авторизации
   */
  public AuthInfo getAuth() {
    return auth;
  }

  public abstract static class Builder<T extends Builder<?>> {
    protected AuthInfo auth;
    protected Source source;
    protected Map<String, String> props;

    /**
     * Создание JPBaseOperation
     *
     * @return JPBaseOperation
     */
    abstract public JPBaseOperation build();

    /**
     * Аутентификация
     *
     * @return Аутентификация
     */
    public AuthInfo getAuth() {
      return auth;
    }

    /**
     * Источник данных
     *
     * @return Источник данных
     */
    public Source getSource() {
      return source;
    }

    /**
     * Дополнительное свойство
     *
     * @return Builder
     */
    public Map<String, String> getProps() {
      if (props == null) {
        return Collections.emptyMap();
      }
      return props;
    }

    /**
     * Аутентификация
     *
     * @param auth Аутентификация
     * @return Builder
     */
    public T auth(AuthInfo auth) {
      this.auth = auth;
      return (T) this;
    }

    /**
     * Источник данных
     *
     * @param source Источник данных
     * @return Builder
     */
    public T source(Source source) {
      this.source = source;
      return (T) this;
    }

    /**
     * Дополнительное свойство
     *
     * @param key   Ключ
     * @param value Значение
     * @return Builder
     */
    public T setProperty(String key, String value) {
      if (props == null) {
        props = new HashMap<>();
      }
      props.put(key, value);
      return (T) this;
    }
  }
}
