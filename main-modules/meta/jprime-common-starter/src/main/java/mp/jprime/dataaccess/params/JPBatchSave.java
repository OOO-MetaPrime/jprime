package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Базовый класс для создания/обновления
 */
public abstract class JPBatchSave extends JPBaseParams {
  private final String jpClass;

  /**
   * Конструктор
   *
   * @param jpClass Код класса
   * @param source  Источник данных
   * @param auth    Данные аутентификации
   */
  protected JPBatchSave(String jpClass, Source source, AuthInfo auth) {
    super(source, auth);
    this.jpClass = jpClass;
  }

  /**
   * Код класса
   *
   * @return код класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Данные для сохранения
   *
   * @return {@link JPMutableData данные для сохранения}
   */
  public abstract Collection<JPMutableData> getData();

  /**
   * Признак пустого батча
   *
   * @return Да/Нет
   */
  public abstract boolean isEmpty();

  /**
   * Размер батча
   *
   * @return Количество записей
   */
  public abstract int size();

  /**
   * Построитель {@link JPBatchSave}
   */
  public abstract static class Builder<T extends Builder<T>> {
    protected final String jpClass;
    protected AuthInfo auth;
    protected Source source;

    protected Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPSave
     *
     * @return JPSave
     */
    abstract public JPBatchSave build();

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    abstract public String getJpClass();

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
     * Признак пустого батча
     *
     * @return Да/Нет
     */
    public abstract boolean isEmpty();

    /**
     * Размер батча
     *
     * @return Количество записей
     */
    public abstract int size();

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
  }
}
