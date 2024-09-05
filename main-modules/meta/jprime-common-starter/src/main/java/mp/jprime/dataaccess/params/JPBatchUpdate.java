package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Запрос множественного обновления
 */
public class JPBatchUpdate extends JPBatchSave {
  private final Map<Comparable, JPMutableData> batches;

  private JPBatchUpdate(String jpClass, Map<Comparable, JPMutableData> batches, Source source, AuthInfo auth) {
    super(jpClass, source, auth);
    this.batches = MapUtils.isEmpty(batches) ? Collections.emptyMap() : new LinkedHashMap<>(batches);
  }

  /**
   * Получить все наборы данных
   *
   * @return наборы данных для обновления, где ключ - идентификатор объекта, значение - данные для обновления
   */
  public Map<Comparable, JPMutableData> getBatches() {
    return batches;
  }

  public void forEach(BiConsumer<? super Comparable, ? super JPMutableData> action) {
    batches.forEach(action);
  }

  /**
   * Данные для обновления
   *
   * @return {@link JPMutableData данные для обновления}
   */
  @Override
  public Collection<JPMutableData> getData() {
    return batches.values();
  }

  /**
   * Признак пустого батча
   *
   * @return Да/Нет
   */
  @Override
  public boolean isEmpty() {
    return batches.isEmpty();
  }

  /**
   * Размер батча
   *
   * @return Количество записей
   */
  @Override
  public int size() {
    return batches.size();
  }

  /**
   * Построитель {@link JPBatchUpdate}
   *
   * @param jpClass Кодовое имя класса
   * @return Построитель {@link JPBatchUpdate}
   */
  public static Builder update(String jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель {@link JPBatchUpdate}
   *
   * @param jpClass Мета класс
   * @return Построитель {@link JPBatchUpdate}
   */
  public static Builder update(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель {@link JPBatchUpdate}
   */
  public static final class Builder extends JPBatchSave.Builder<Builder> {
    private final Map<Comparable, JPMutableData> batches = new LinkedHashMap<>();
    private Comparable id;
    private JPMutableData data = JPMutableData.empty();

    private Builder(String jpClass) {
      super(jpClass);
    }

    /**
     * Построить {@link JPBatchUpdate}
     *
     * @return {@link JPBatchUpdate}
     */
    @Override
    public JPBatchUpdate build() {
      return new JPBatchUpdate(jpClass, batches, source, auth);
    }

    /**
     * Идентификатор объекта
     *
     * @param id Идентификатор объекта
     * @return Построитель {@link JPBatchUpdate}
     */
    public Builder id(Comparable id) {
      this.id = id;
      return this;
    }

    /**
     * Значение атрибута
     *
     * @param attr  атрибут
     * @param value значение атрибута
     * @return Построитель {@link JPBatchUpdate}
     */
    public Builder set(JPAttr attr, Object value) {
      return attr != null ? set(attr.getCode(), value) : this;
    }


    /**
     * Значение атрибута при создании
     *
     * @param attrValues значения атрибутов
     * @return Построитель {@link JPBatchCreate}
     */
    public Builder set(JPMap attrValues) {
      if (attrValues != null && !attrValues.isEmpty()) {
        attrValues.forEach((k, v) -> this.data.put(k, v));
      }
      return this;
    }

    /**
     * Значение атрибута
     *
     * @param attrCode кодовое имя атрибута
     * @param value    значение атрибута
     * @return Построитель {@link JPBatchUpdate}
     */
    public Builder set(String attrCode, Object value) {
      if (attrCode != null && !attrCode.isEmpty()) {
        this.data.put(attrCode, value);
      }
      return this;
    }

    public Builder addBatch() {
      if (id != null && !data.isEmpty()) {
        batches.put(id, data);
      }
      id = null;
      data = JPMutableData.empty();
      return this;
    }

    /**
     * Признак пустого батча
     *
     * @return Да/Нет
     */
    @Override
    public boolean isEmpty() {
      return data.isEmpty();
    }

    /**
     * Размер батча
     *
     * @return Количество записей
     */
    @Override
    public int size() {
      return batches.size();
    }

    /**
     * Возвращает кодовое имя класса
     *
     * @return Кодовое имя класса
     */
    @Override
    public String getJpClass() {
      return jpClass;
    }
  }
}
