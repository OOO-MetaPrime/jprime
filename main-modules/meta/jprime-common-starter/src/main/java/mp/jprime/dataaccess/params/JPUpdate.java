package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.meta.JPAttr;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос обновления
 */
public class JPUpdate extends JPSave {
  private final JPId jpId;
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
    super(data, source, auth);
    this.jpId = jpId;
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
  public static final class Builder extends JPSave.Builder<Builder> {
    private final JPId jpId;
    private Map<String, Collection<JPCreate>> linkedCreate = new HashMap<>();
    private Map<String, Collection<JPUpdate>> linkedUpdate = new HashMap<>();
    private Map<String, Collection<JPDelete>> linkedDelete = new HashMap<>();

    private Builder(JPId jpId) {
      this.jpId = jpId;
    }

    /**
     * Создаем JPUpdate
     *
     * @return JPUpdate
     */
    @Override
    public JPUpdate build() {
      return new JPUpdate(jpId, data, linkedCreate, linkedUpdate, linkedDelete, auth, source);
    }

    /**
     * Возвращает идентификатор объекта
     *
     * @return идентификатор объекта
     */
    public JPId getJpId() {
      return jpId;
    }

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    @Override
    public String getJpClass() {
      return jpId.getJpClass();
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
  }
}
