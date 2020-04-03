package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос создания
 */
public class JPCreate extends JPBaseCRUD {
  private final String jpClass;
  private final JPMutableData data;
  private final Map<String, Collection<JPCreate>> linkedData;
  private final AuthInfo auth;

  /**
   * Конструктор
   *
   * @param jpClass    Кодовое имя класса
   * @param data       Значение атрибута при создании
   * @param linkedData параметры для создания ссылочного объекта
   * @param auth       Данные аутентификации
   * @param source     Источник данных
   */
  private JPCreate(String jpClass, Map<String, Object> data, Map<String,
      Collection<JPCreate>> linkedData, AuthInfo auth, Source source) {
    super(source);
    this.jpClass = jpClass;
    this.data = new JPMutableData(data);
    this.linkedData = linkedData;
    this.auth = auth;
  }

  /**
   * Данные аутентификации
   *
   * @return Данные аутентификации
   */
  public AuthInfo getAuth() {
    return auth;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClass() {
    return jpClass;
  }

  /**
   * Данные для создания
   *
   * @return Данные для создания
   */
  public JPMutableData getData() {
    return data;
  }

  /**
   * Данные для создания ссылочных объектов
   *
   * @return Данные для создания ссылочных объектов
   */
  public Map<String, Collection<JPCreate>> getLinkedData() {
    return linkedData;
  }

  /**
   * Построитель JPCreate
   *
   * @param jpClass Мета класс
   * @return Builder
   */
  public static Builder create(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель JPCreate
   *
   * @param jpClass Кодовое имя класса
   * @return Builder
   */
  public static Builder create(String jpClass) {
    return new Builder(jpClass);
  }


  /**
   * Построитель JPCreate
   */
  public static final class Builder {
    private String jpClass;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Collection<JPCreate>> linkedData = new HashMap<>();
    private AuthInfo auth;
    private Source source;

    private Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPCreate
     *
     * @return JPCreate
     */
    public JPCreate build() {
      return new JPCreate(jpClass, data, linkedData, auth, source);
    }

    /**
     * Аутентификация
     *
     * @param auth Аутентификация
     * @return Builder
     */
    public Builder auth(AuthInfo auth) {
      this.auth = auth;
      return this;
    }

    /**
     * Значение атрибута при создании
     *
     * @param attr  атрибут
     * @param value значение атрибута
     * @return Builder
     */
    public Builder set(JPAttr attr, Object value) {
      return attr != null ? set(attr.getCode(), value) : this;
    }

    /**
     * Значение атрибута при создании
     *
     * @param attrCode кодовое имя атрибута
     * @param value    значение атрибута
     * @return Builder
     */
    public Builder set(String attrCode, Object value) {
      this.data.put(attrCode, value);
      return this;
    }

    /**
     * Значение атрибута при создании
     *
     * @param attr   атрибут
     * @param create параметры для создания ссылочного объекта
     * @return Builder
     */
    public Builder addWith(JPAttr attr, JPCreate create) {
      return attr != null ? set(attr.getCode(), create) : this;
    }

    /**
     * Значение сатрибута при создании
     *
     * @param attrCode кодовое имя атрибута
     * @param create   параметры для создания ссылочного объекта
     * @return Builder
     */
    public Builder addWith(String attrCode, JPCreate create) {
      this.linkedData.computeIfAbsent(attrCode, x -> new ArrayList<>()).add(create);
      return this;
    }

    /**
     * Источник данных
     *
     * @param source Источник данных
     * @return Builder
     */
    public Builder source(Source source) {
      this.source = source;
      return this;
    }
  }
}
