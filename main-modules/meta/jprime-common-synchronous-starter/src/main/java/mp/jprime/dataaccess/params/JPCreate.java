package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * Запрос создания
 */
public class JPCreate extends JPSave {
  private final String jpClass;
  private final Map<String, Collection<JPCreate>> linkedData;
  private final boolean onConflictDoNothing;
  private final boolean upsert;
  private final Collection<String> conflictAttr;
  private final Collection<String> conflictSet;

  /**
   * Конструктор
   *
   * @param jpClass     Кодовое имя класса
   * @param data        Значение атрибута при создании
   * @param systemAttrs Атрибуты, значения которых, указаны от имени системы
   * @param linkedData  параметры для создания ссылочного объекта
   * @param auth        Данные аутентификации
   * @param source      Источник данных
   */
  private JPCreate(String jpClass, Map<String, Object> data,
                   Collection<String> systemAttrs,
                   Map<String, Collection<JPCreate>> linkedData,
                   AuthInfo auth, Source source, Map<String, String> props,
                   boolean onConflictDoNothing, boolean upsert,
                   Collection<String> conflictAttr, Collection<String> conflictSet) {
    super(data, systemAttrs, source, auth, props);
    this.jpClass = jpClass;
    this.linkedData = linkedData;
    this.onConflictDoNothing = onConflictDoNothing;
    this.upsert = upsert;
    if (upsert) {
      this.conflictAttr = Collections.unmodifiableCollection(conflictAttr);
      this.conflictSet = Collections.unmodifiableCollection(conflictSet);
    } else {
      this.conflictAttr = Collections.emptyList();
      this.conflictSet = Collections.emptyList();
    }
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  @Override
  public String getJpClass() {
    return jpClass;
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
   * Построитель {@link JPCreate}
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
   * Построитель JPCreate
   */
  public static final class Builder extends JPSave.Builder<Builder> {
    private final String jpClass;
    private final Map<String, Collection<JPCreate>> linkedData = new HashMap<>();
    private boolean onConflictDoNothing;
    private boolean upsert;
    private Collection<String> conflictAttr = new ArrayList<>();
    private Collection<String> conflictSet = new ArrayList<>();

    private Builder(String jpClass) {
      this.jpClass = jpClass;
    }

    /**
     * Создаем JPCreate
     *
     * @return JPCreate
     */
    @Override
    public JPCreate build() {
      return new JPCreate(jpClass, data, systemAttrs, linkedData, auth, source, props,
          onConflictDoNothing, upsert, conflictAttr, conflictSet);
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
  }
}
