package mp.jprime.meta.json.beans;

public class JsonJPAttrMap {
  /**
   * Глобальный идентификатор
   */
  private String guid;
  /**
   * Имя в хранилище
   */
  private String map;
  /**
   * Признак автоинкремента
   */
  private boolean autoincrement;
  /**
   * Сихронизация с хранилищем
   */
  private boolean storageSync;
  /**
   * Кодовое имя атрибута
   */
  private String code;
  /**
   * Мап на поле с индексами нечеткого поиска
   */
  private String fuzzyMap;
  /**
   * Регистр значений
   */
  private String cs;
  /**
   * Запрет на изменение значений
   */
  private boolean readonly;

  public JsonJPAttrMap() {

  }

  private JsonJPAttrMap(String guid, String map, String code, boolean autoincrement, boolean storageSync,
                        String fuzzyMap, String cs, boolean readonly) {
    this.guid = guid;
    this.map = map;
    this.code = code;
    this.autoincrement = autoincrement;
    this.storageSync = storageSync;
    this.fuzzyMap = fuzzyMap;
    this.cs = cs;
    this.readonly = readonly;
  }

  /**
   * Глобальный идентификатор
   *
   * @return Глобальный идентификатор
   */
  public String getGuid() {
    return guid;
  }

  /**
   * Имя в хранилище
   *
   * @return Имя в хранилище
   */
  public String getMap() {
    return map;
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
   * Признак автоинкремента
   *
   * @return Признак автоинкремента
   */
  public boolean isAutoincrement() {
    return autoincrement;
  }

  /**
   * Сихронизация с хранилищем
   *
   * @return Сихронизация с хранилищем
   */
  public boolean isStorageSync() {
    return storageSync;
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
   * Регистр значений
   *
   * @return Регистр значений
   */
  public String getCs() {
    return cs;
  }

  /**
   * Запрет на изменение значений
   *
   * @return Запрет на изменение значений
   */
  public boolean isReadonly() {
    return readonly;
  }

  /**
   * Построитель JsonJPAttrMap
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPAttrMap
   */
  public static final class Builder {
    private String guid;
    private String map;
    private String code;
    private boolean autoincrement;
    private boolean storageSync;
    private String fuzzyMap;
    private String cs;
    private boolean readonly;

    private Builder() {

    }

    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    public Builder map(String map) {
      this.map = map;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder autoincrement(boolean autoincrement) {
      this.autoincrement = autoincrement;
      return this;
    }

    public Builder storageSync(boolean storageSync) {
      this.storageSync = storageSync;
      return this;
    }

    public Builder fuzzyMap(String fuzzyMap) {
      this.fuzzyMap = fuzzyMap;
      return this;
    }

    public Builder cs(String cs) {
      this.cs = cs;
      return this;
    }

    public Builder readonly(boolean readonly) {
      this.readonly = readonly;
      return this;
    }

    public JsonJPAttrMap build() {
      return new JsonJPAttrMap(guid, map, code, autoincrement, storageSync, fuzzyMap, cs, readonly);
    }
  }
}
