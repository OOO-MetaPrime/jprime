package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.query.Filter;
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
  private final Filter where;
  private final boolean autoChangeDate;

  /**
   * Конструктор
   *
   * @param jpId         Идентификатор объекта
   * @param data         Значение атрибута при обновлении
   * @param systemAttrs  Атрибуты, значения которых, указаны от имени системы
   * @param linkedCreate параметры для создания ссылочного объекта
   * @param linkedUpdate параметры для обновления ссылочного объекта
   * @param linkedDelete параметры для удаления ссылочного объекта
   * @param auth         Данные аутентификации
   * @param source       Источник данных
   */
  private JPUpdate(JPId jpId, Map<String, Object> data,
                   Collection<String> systemAttrs,
                   Map<String, Collection<JPCreate>> linkedCreate,
                   Map<String, Collection<JPUpdate>> linkedUpdate,
                   Map<String, Collection<JPDelete>> linkedDelete,
                   Filter where, AuthInfo auth, Source source, boolean autoChangeDate,
                   Map<String, String> props) {
    super(data, systemAttrs, source, auth, props);
    this.jpId = jpId;
    this.linkedCreate = linkedCreate == null ? Collections.emptyMap() : Collections.unmodifiableMap(linkedCreate);
    this.linkedUpdate = linkedUpdate == null ? Collections.emptyMap() : Collections.unmodifiableMap(linkedUpdate);
    this.linkedDelete = linkedDelete == null ? Collections.emptyMap() : Collections.unmodifiableMap(linkedDelete);
    this.where = where;
    this.autoChangeDate = autoChangeDate;
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
   * Устанавливает ли дату изменения
   *
   * @return Да/Нет
   */
  public boolean isAutoChangeDate() {
    return autoChangeDate;
  }

  /**
   * Условия
   *
   * @return Условия
   */
  public Filter getWhere() {
    return where;
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
   *
   * @param jpClass Кодовое имя метаописания класса
   * @param id      Идентификатор объекта
   * @return Builder
   */
  public static Builder update(String jpClass, Object id) {
    return new Builder(JPId.get(jpClass, id));
  }

  /**
   * Построитель JPUpdate
   */
  public static final class Builder extends JPSave.Builder<Builder> {
    private final JPId jpId;
    private final Map<String, Collection<JPCreate>> linkedCreate = new HashMap<>();
    private final Map<String, Collection<JPUpdate>> linkedUpdate = new HashMap<>();
    private final Map<String, Collection<JPDelete>> linkedDelete = new HashMap<>();
    private Filter where;
    private boolean autoChangeDate = true;

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
      return new JPUpdate(jpId, data, systemAttrs, linkedCreate, linkedUpdate, linkedDelete, where,
          auth, source, autoChangeDate, props);
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

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder where(Filter where) {
      this.where = where;
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder andWhere(Filter where) {
      if (where == null) {
        return this;
      }
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.and(this.where, where);
      }
      return this;
    }

    /**
     * Условие выборки
     *
     * @param where Условие выборки
     * @return Builder
     */
    public Builder orWhere(Filter where) {
      if (where == null) {
        return this;
      }
      if (this.where == null) {
        this.where = where;
      } else {
        this.where = Filter.or(this.where, where);
      }
      return this;
    }

    /**
     * Устанавливает ли дату изменения (по умолчанию {@code true})
     *
     * @param autoChangeDate Да/Нет
     * @return Builder
     */
    public Builder autoChangeDate(boolean autoChangeDate) {
      this.autoChangeDate = autoChangeDate;
      return this;
    }
  }
}
