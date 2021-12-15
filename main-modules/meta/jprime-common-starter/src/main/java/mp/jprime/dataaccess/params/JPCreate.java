package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.Source;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.*;

/**
 * Запрос создания
 */
public class JPCreate extends JPSave {
  private final String jpClass;
  private final Map<String, Collection<JPCreate>> linkedData;

  /**
   * Конструктор
   *
   * @param jpClass    Кодовое имя класса
   * @param data       Значение атрибута при создании
   * @param linkedData параметры для создания ссылочного объекта
   * @param auth       Данные аутентификации
   * @param source     Источник данных
   */
  private JPCreate(String jpClass, Map<String, Object> data,
                   Map<String, Collection<JPCreate>> linkedData,
                   AuthInfo auth, Source source) {
    super(data, source, auth);
    this.jpClass = jpClass;
    this.linkedData = linkedData;
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
   * Построитель JPCreate
   */
  public static final class Builder extends JPSave.Builder<Builder> {
    private String jpClass;
    private Map<String, Collection<JPCreate>> linkedData = new HashMap<>();

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
      return new JPCreate(jpClass, data, linkedData, auth, source);
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
  }
}
