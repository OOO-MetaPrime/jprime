package mp.jprime.meta.json.beans;

import java.util.Collection;

public final class JsonJPClassMap {
  private String guid;
  private String storage;
  private String map;
  private String schema;
  /**
   * Синхронизация с хранилищем
   */
  private boolean storageSync;
  /**
   * Атрибуты
   */
  private Collection<JsonJPAttrMap> attrMaps;

  public JsonJPClassMap() {

  }

  private JsonJPClassMap(String guid, String storage, String map, String schema, boolean storageSync, Collection<JsonJPAttrMap> attrMaps) {
    this.guid = guid;
    this.storage = storage;
    this.map = map;
    this.schema = schema;
    this.storageSync = storageSync;
    this.attrMaps = attrMaps;
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
   * Код хранилища
   *
   * @return Код хранилища
   */
  public String getStorage() {
    return storage;
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
   * Схема в хранилище
   *
   * @return Схема в хранилище
   */
  public String getSchema() {
    return schema;
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
   * писок мапиинга атрибутов класса
   *
   * @return писок мапиинга атрибутов класса
   */
  public Collection<JsonJPAttrMap> getAttrMaps() {
    return attrMaps;
  }

  /**
   * Построитель JsonJPClassMap
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPClassMap
   */
  public static final class Builder {
    private String guid;
    private String storage;
    private String map;
    private String schema;
    private boolean storageSync;
    private Collection<JsonJPAttrMap> attrMaps;

    private Builder() {

    }

    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    public Builder storage(String storage) {
      this.storage = storage;
      return this;
    }

    public Builder map(String map) {
      this.map = map;
      return this;
    }

    public Builder schema(String schema) {
      this.schema = schema;
      return this;
    }

    public Builder storageSync(boolean storageSync) {
      this.storageSync = storageSync;
      return this;
    }

    public Builder attrMaps(Collection<JsonJPAttrMap> attrMaps) {
      this.attrMaps = attrMaps;
      return this;
    }

    public JsonJPClassMap build() {
      return new JsonJPClassMap(guid, storage, map, schema, storageSync, attrMaps);
    }
  }
}
