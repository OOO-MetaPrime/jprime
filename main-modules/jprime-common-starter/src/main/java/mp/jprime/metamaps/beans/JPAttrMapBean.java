package mp.jprime.metamaps.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.metamaps.JPAttrMap;

/**
 * описание привязки атрибута к хранилищу
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JPAttrMapBean implements JPAttrMap {
  private final String code;
  private final String map;
  private final String fuzzyMap;
  private final String cs;
  private final boolean readOnly;

  /**
   * Конструктор
   *
   * @param code     Кодовое имя атрибута
   * @param map      Мап на БД
   * @param fuzzyMap Мап на поле с индексами нечеткого поиска
   * @param cs       Регистр значений
   * @param readOnly Запрет на изменение значений
   */
  private JPAttrMapBean(String code, String map, String fuzzyMap, String cs, Boolean readOnly) {
    this.code = code != null && !code.isEmpty() ? code : null;
    this.map = map != null && !map.isEmpty() ? map : null;
    this.fuzzyMap = fuzzyMap != null && !fuzzyMap.isEmpty() ? fuzzyMap : null;
    this.cs = cs != null && !cs.isEmpty() ? cs : null;
    this.readOnly = readOnly != null && readOnly;
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getCode() {
    return code;
  }

  /**
   * Мап на БД
   *
   * @return Мап на БД
   */
  public String getMap() {
    return map;
  }

  /**
   * Мап на поле с индексами нечеткого поиска
   *
   * @return Мап на поле с индексами нечеткого поиска
   */
  public String getFuzzyMap() {
    return fuzzyMap;
  }

  /**
   * Возвращает регистр значений
   *
   * @return Регистр значений
   */
  public String getCs() {
    return cs;
  }

  /**
   * Регистр значений
   *
   * @return Регистр значений
   */
  public JPCase getCase() {
    return JPCase.getCase(cs);
  }

  /**
   * Запрет на изменение значений
   *
   * @return Да/Нет
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Построитель JPAttrMap
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "JPAttrMap{" +
        "code='" + code + '\'' +
        ", map='" + map + '\'' +
        ", cs='" + cs + '\'' +
        ", readOnly='" + readOnly + '\'' +
        '}';
  }

  /**
   * Построитель JPAttrMap
   */
  public static final class Builder {
    private String code;
    private String map;
    private String fuzzyMap;
    private String cs;
    private Boolean readOnly;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPAttrMapBean build() {
      return new JPAttrMapBean(code, map, fuzzyMap, cs, readOnly);
    }

    /**
     * Мап на БД
     *
     * @param map Мап на БД
     * @return Builder
     */
    public Builder map(String map) {
      this.map = map;
      return this;
    }

    /**
     * Мап на поле с индексами нечеткого поиска
     *
     * @param fuzzyMap Мап на поле с индексами нечеткого поиска
     * @return Builder
     */
    public Builder fuzzyMap(String fuzzyMap) {
      this.fuzzyMap = fuzzyMap;
      return this;
    }


    /**
     * Кодовое имя атрибута
     *
     * @param code Кодовое имя атрибута
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Регистр значений
     *
     * @param cs Регистр значений
     * @return Builder
     */
    public Builder cs(String cs) {
      this.cs = cs;
      return this;
    }

    /**
     * Запрет на изменение значений
     *
     * @param readOnly Да/Нет
     * @return Builder
     */
    public Builder readOnly(Boolean readOnly) {
      this.readOnly = readOnly;
      return this;
    }
  }
}