package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Запрос множественного создания
 */
public class JPBatchCreate extends JPBatchSave {
  private final Collection<JPMutableData> data;
  private final boolean onConflictDoNothing;
  private final boolean upsert;
  private final Collection<String> conflictAttr;
  private final Collection<String> conflictSet;

  private JPBatchCreate(Collection<Map<String, Object>> data, boolean onConflictDoNothing, boolean upsert,
                        Collection<String> conflictAttr, Collection<String> conflictSet, Source source, AuthInfo auth, String jpClass) {
    super(jpClass, source, auth);
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
        Collections.unmodifiableCollection(data.stream()
            .map(JPMutableData::of)
            .collect(Collectors.toList())
        );
  }

  /**
   * Данные для сохранения
   *
   * @return {@link JPMutableData данные для сохранения}
   */
  @Override
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
   * @return коллекция кодов атрибутов, по которым проверяется конфликт
   */
  public Collection<String> getConflictAttr() {
    return conflictAttr;
  }

  /**
   * Заменяемые поля upsert
   *
   * @return коллекция кодов атрибутов для обновления при конфликте
   */
  public Collection<String> getConflictSet() {
    return conflictSet;
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
    return data.size();
  }

  /**
   * Построитель {@link JPBatchCreate}
   *
   * @param jpClass Кодовое имя класса
   * @return Построитель {@link JPBatchCreate}
   */
  public static Builder create(String jpClass) {
    return new Builder(jpClass);
  }

  /**
   * Построитель {@link JPBatchCreate}
   *
   * @param jpClass Мета класс
   * @return Построитель {@link JPBatchCreate}
   */
  public static Builder create(JPClass jpClass) {
    return new Builder(jpClass == null ? null : jpClass.getCode());
  }

  /**
   * Построитель {@link JPBatchCreate}
   */
  public static final class Builder extends JPBatchSave.Builder<Builder> {
    private final Collection<Map<String, Object>> allData = new ArrayList<>();
    private Map<String, Object> data = new HashMap<>();
    private boolean onConflictDoNothing = Boolean.FALSE;
    private boolean upsert = Boolean.FALSE;
    private Collection<String> conflictAttr = new ArrayList<>();
    private Collection<String> conflictSet = new ArrayList<>();

    private Builder(String jpClass) {
      super(jpClass);
    }

    /**
     * Построить {@link JPBatchCreate}
     *
     * @return {@link JPBatchCreate}
     */
    @Override
    public JPBatchCreate build() {
      return new JPBatchCreate(allData, onConflictDoNothing, upsert, conflictAttr, conflictSet, source, auth, jpClass);
    }

    /**
     * Флаг "При конфликте игнорировать"
     *
     * @param onConflictDoNothing Да/Нет
     * @return Builder
     */
    public Builder onConflictDoNothing(boolean onConflictDoNothing) {
      this.onConflictDoNothing = onConflictDoNothing;
      return this;
    }

    /**
     * Upsert
     *
     * @param conflictAttr Поля конфликта
     * @param conflictSet  Поля для замены
     * @return Построитель {@link JPBatchCreate}
     */
    public Builder upsert(Collection<String> conflictAttr, Collection<String> conflictSet) {
      if (CollectionUtils.isNotEmpty(conflictAttr) && CollectionUtils.isNotEmpty(conflictSet)) {
        this.upsert = Boolean.TRUE;
        this.conflictAttr = conflictAttr;
        this.conflictSet = conflictSet;
      } else {
        this.upsert = Boolean.FALSE;
      }
      return this;
    }

    /**
     * Значение атрибута при создании
     *
     * @param attr  атрибут
     * @param value значение атрибута
     * @return Построитель {@link JPBatchCreate}
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
     * Значение атрибута при создании
     *
     * @param attrCode кодовое имя атрибута
     * @param value    значение атрибута
     * @return Построитель {@link JPBatchCreate}
     */
    public Builder set(String attrCode, Object value) {
      if (attrCode != null && !attrCode.isEmpty()) {
        this.data.put(attrCode, value);
      }
      return this;
    }

    public Builder addBatch() {
      if (MapUtils.isNotEmpty(data)) {
        allData.add(data);
        data = new HashMap<>();
      }
      return this;
    }

    /**
     * Признак пустого батча
     *
     * @return Да/Нет
     */
    @Override
    public boolean isEmpty() {
      return allData.isEmpty();
    }

    /**
     * Размер батча
     *
     * @return Количество записей
     */
    @Override
    public int size() {
      return allData.size();
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
