package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Базовый класс для создания/обновления
 */
public abstract class JPBatchSave extends JPBaseOperation {
  private final String jpClass;

  /**
   * Конструктор
   *
   * @param jpClass Код класса
   * @param source  Источник данных
   * @param auth    Данные аутентификации
   */
  protected JPBatchSave(String jpClass, Source source, AuthInfo auth, Map<String, String> props) {
    super(source, auth, props);
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
  public abstract static class Builder<T extends Builder<?>> extends JPBaseOperation.Builder<T> {
    protected final String jpClass;

    protected Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    public String getJpClass() {
      return jpClass;
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

  }
}
