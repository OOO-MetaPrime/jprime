package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.security.AuthInfo;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовый класс для создания/обновления
 */
public abstract class JPBatchSave extends JPBaseParams {
  private final Collection<JPMutableData> data;
  private final boolean onConflictDoNothing;
  private final boolean upsert;
  private final Collection<String> conflictAttr;
  private final Collection<String> conflictSet;

  /**
   * Конструктор
   *
   * @param data                Значение атрибута при создании
   * @param onConflictDoNothing Флаг "При конфликте игнорировать"
   * @param source              Источник данных
   * @param auth                Данные аутентификации
   */
  protected JPBatchSave(Collection<Map<String, Object>> data, boolean onConflictDoNothing, boolean upsert,
                        Collection<String> conflictAttr, Collection<String> conflictSet, Source source, AuthInfo auth) {
    super(source, auth);
    this.onConflictDoNothing = onConflictDoNothing;
    this.upsert = upsert;
    if (upsert) {
      this.conflictAttr = Collections.unmodifiableCollection(conflictAttr);
      this.conflictSet = Collections.unmodifiableCollection(conflictSet);
    } else {
      this.conflictAttr = Collections.emptyList();
      this.conflictSet = Collections.emptyList();
    }
    this.data = data == null ? Collections.emptyList() :
        Collections.unmodifiableCollection(data.stream().map(JPMutableData::of).collect(Collectors.toList()));
  }

  /**
   * Данные для создания
   *
   * @return Данные для создания
   */
  public Collection<JPMutableData> getData() {
    return data;
  }

  /**
   * Флаг "При конфликте игнорировать"
   *
   * @return Да/Нет
   */
  public boolean isOnConflictDoNothing() {
    return onConflictDoNothing;
  }

  /**
   * Флаг upsert
   *
   * @return Да/Нет
   */
  public boolean isUpsert() {
    return upsert;
  }

  /**
   * Конфликты для upsert
   *
   * @return Да/Нет
   */
  public Collection<String> getСonflictAttr() {
    return conflictAttr;
  }

  /**
   * Заменяемые поля upsert
   *
   * @return Да/Нет
   */
  public Collection<String> getConflictSet() {
    return conflictSet;
  }

  /**
   * Признак пустого батча
   *
   * @return Да/Нет
   */
  public boolean isEmpty() {
    return data.isEmpty();
  }

  /**
   * Построитель JPSave
   */
  public abstract static class Builder<T extends Builder> {
    protected Collection<Map<String, Object>> allData = new ArrayList<>();
    protected Map<String, Object> data = new HashMap<>();
    protected boolean onConflictDoNothing = Boolean.FALSE;
    protected boolean upsert = Boolean.FALSE;
    protected Collection<String> conflictAttr = new ArrayList<>();
    protected Collection<String> conflictSet = new ArrayList<>();
    protected AuthInfo auth;
    protected Source source;

    protected Builder() {
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
     * Флаг "При конфликте игнорировать"
     *
     * @param onConflictDoNothing Да/Нет
     * @return Builder
     */
    public T onConflictDoNothing(boolean onConflictDoNothing) {
      this.onConflictDoNothing = onConflictDoNothing;
      return (T) this;
    }

    /**
     * Upsert
     *
     * @param conflictAttr Поля конфликта
     * @param conflictSet  Поля для замены
     * @return Builder
     */
    public T upsert(Collection<String> conflictAttr, Collection<String> conflictSet) {
      if (!CollectionUtils.isEmpty(conflictAttr) && !CollectionUtils.isEmpty(conflictSet)) {
        this.upsert = Boolean.TRUE;
        this.conflictAttr = conflictAttr;
        this.conflictSet = conflictSet;
      } else {
        this.upsert = Boolean.FALSE;
      }
      return (T) this;
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
     * Значение атрибута при создании
     *
     * @param attr  атрибут
     * @param value значение атрибута
     * @return Builder
     */
    public T set(JPAttr attr, Object value) {
      return attr != null ? set(attr.getCode(), value) : (T) this;
    }

    /**
     * Значение атрибута при создании
     *
     * @param attrCode кодовое имя атрибута
     * @param value    значение атрибута
     * @return Builder
     */
    public T set(String attrCode, Object value) {
      if (attrCode != null && !attrCode.isEmpty()) {
        this.data.put(attrCode, value);
      }
      return (T) this;
    }

    public T addBatch() {
      if (MapUtils.isNotEmpty(data)) {
        allData.add(data);
        data = new HashMap<>();
      }
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
  }
}
