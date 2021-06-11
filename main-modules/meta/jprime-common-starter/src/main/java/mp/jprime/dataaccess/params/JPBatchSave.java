package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.security.AuthInfo;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовые класс для создания/обновления
 */
public abstract class JPBatchSave extends JPBaseParams {
  private final Collection<JPMutableData> data;

  /**
   * Конструктор
   *
   * @param data   Значение атрибута при создании
   * @param source Источник данных
   * @param auth   Данные аутентификации
   */
  protected JPBatchSave(Collection<Map<String, Object>> data, Source source, AuthInfo auth) {
    super(source, auth);
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
   * Построитель JPSave
   */
  public abstract static class Builder<T extends Builder> {
    protected Collection<Map<String, Object>> allData = new ArrayList<>();
    protected Map<String, Object> data = new HashMap<>();
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
