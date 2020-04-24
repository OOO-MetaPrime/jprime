package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.meta.JPAttr;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос обновления
 */
public class JPUpdate extends JPBaseCRUD {
  private final JPId jpId;
  private final JPMutableData data;
  private final Map<String, Collection<JPCreate>> linkedCreate;
  private final Map<String, Collection<JPUpdate>> linkedUpdate;
  private final Map<String, Collection<JPDelete>> linkedDelete;

  /**
   * Конструктор
   *
   * @param jpId         Идентификатор объекта
   * @param data         Значение атрибута при обновлении
   * @param linkedCreate параметры для создания ссылочного объекта
   * @param linkedUpdate параметры для обновления ссылочного объекта
   * @param linkedDelete параметры для удаления ссылочного объекта
   * @param auth         Данные аутентификации
   * @param source       Источник данных
   */
  private JPUpdate(JPId jpId, Map<String, Object> data,
                   Map<String, Collection<JPCreate>> linkedCreate,
                   Map<String, Collection<JPUpdate>> linkedUpdate,
                   Map<String, Collection<JPDelete>> linkedDelete,
                   AuthInfo auth, Source source) {
    super(source, auth);
    this.jpId = jpId;
    this.data = JPMutableData.of(data);
    this.linkedCreate = Collections.unmodifiableMap(linkedCreate == null ? Collections.emptyMap() : linkedCreate);
    this.linkedUpdate = Collections.unmodifiableMap(linkedUpdate == null ? Collections.emptyMap() : linkedUpdate);
    this.linkedDelete = Collections.unmodifiableMap(linkedDelete == null ? Collections.emptyMap() : linkedDelete);
  }

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  public JPId getJpId() {
    return jpId;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getJpClass() {
    JPId id = getJpId();
    return id != null ? id.getJpClass() : null;
  }

  /**
   * Данные для обновления
   *
   * @return Данные для обновления
   */
  public JPMutableData getData() {
    return data;
  }

  /**
   * Данные для создания ссылочных объектов
   *
   * @return Данные для создания ссылочных объектов
   */
  public Map<String, Collection<JPCreate>> getLinkedCreate() {
    return linkedCreate;
  }

  /**
   * Данные для обновления ссылочных объектов
   *
   * @return Данные для обновления ссылочных объектов
   */
  public Map<String, Collection<JPUpdate>> getLinkedUpdate() {
    return linkedUpdate;
  }

  /**
   * Данные для удаления ссылочных объектов
   *
   * @return Данные для удаления ссылочных объектов
   */
  public Map<String, Collection<JPDelete>> getLinkedDelete() {
    return linkedDelete;
  }

  /**
   * Построитель JPUpdate
   *
   * @param jpId JPId
   * @return Builder
   */
  public static Builder update(JPId jpId) {
    return new Builder(jpId);
  }

  /**
   * Построитель JPUpdate
   */
  public static final class Builder {
    private final JPId jpId;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Collection<JPCreate>> linkedCreate = new HashMap<>();
    private Map<String, Collection<JPUpdate>> linkedUpdate = new HashMap<>();
    private Map<String, Collection<JPDelete>> linkedDelete = new HashMap<>();
    private AuthInfo auth;
    private Source source;

    private Builder(JPId jpId) {
      this.jpId = jpId;
    }

    /**
     * Создаем JPUpdate
     *
     * @return JPUpdate
     */
    public JPUpdate build() {
      return new JPUpdate(jpId, data, linkedCreate, linkedUpdate, linkedDelete, auth, source);
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
     * Значение атрибута при обновлении
     *
     * @param attr  атрибут
     * @param value значение атрибута
     * @return Builder
     */
    public Builder set(JPAttr attr, Object value) {
      return attr != null ? set(attr.getCode(), value) : this;
    }

    /**
     * Значение атрибута при обновлении
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
     * Значение атрибута при обновлении
     *
     * @param attr   атрибут
     * @param create параметры для создания ссылочного объекта
     * @return Builder
     */
    public Builder addWith(JPAttr attr, JPCreate create) {
      return attr != null ? addWith(attr.getCode(), create) : this;
    }

    /**
     * Значение сатрибута при обновлении
     *
     * @param attrCode кодовое имя атрибута
     * @param create   параметры для создания ссылочного объекта
     * @return Builder
     */
    public Builder addWith(String attrCode, JPCreate create) {
      this.linkedCreate.computeIfAbsent(attrCode, x -> new ArrayList<>()).add(create);
      return this;
    }

    /**
     * Значение атрибута при обновлении
     *
     * @param attr   атрибут
     * @param update параметры для обновления ссылочного объекта
     * @return Builder
     */
    public Builder addWith(JPAttr attr, JPUpdate update) {
      return attr != null ? addWith(attr.getCode(), update) : this;
    }

    /**
     * Значение сатрибута при обновлении
     *
     * @param attrCode кодовое имя атрибута
     * @param update   параметры для обновления ссылочного объекта
     * @return Builder
     */
    public Builder addWith(String attrCode, JPUpdate update) {
      this.linkedUpdate.computeIfAbsent(attrCode, x -> new ArrayList<>()).add(update);
      return this;
    }

    /**
     * Значение атрибута при удалении
     *
     * @param attr   атрибут
     * @param delete параметры для удаления ссылочного объекта
     * @return Builder
     */
    public Builder addWith(JPAttr attr, JPDelete delete) {
      return attr != null ? addWith(attr.getCode(), delete) : this;
    }

    /**
     * Значение сатрибута при удалении
     *
     * @param attrCode кодовое имя атрибута
     * @param delete   параметры для удаления ссылочного объекта
     * @return Builder
     */
    public Builder addWith(String attrCode, JPDelete delete) {
      this.linkedDelete.computeIfAbsent(attrCode, x -> new ArrayList<>()).add(delete);
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
