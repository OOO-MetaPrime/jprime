package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.meta.services.JPMetaStorage;

import java.util.Map;
import java.util.stream.Collectors;

/*
 * Модель данных ответа получения объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonJPObject {
  private Object id;
  private String classCode;
  private Map<String, Object> data;
  private Map<String, JsonJPObject> linkedData;
  private JsonChangeAccess access;

  public JsonJPObject() {

  }

  /**
   * Конструктор
   *
   * @param metaStorage MetaStorage
   * @param jpId        JPId
   * @param jpData      JPData
   * @param access      JsonAccess
   */
  private JsonJPObject(JPMetaStorage metaStorage, JPId jpId, JPData jpData, JPLinkedData linkedData,
                       JsonChangeAccess access) {
    this.id = jpId != null ? jpId.getId() : null;
    this.classCode = jpId != null ? jpId.getJpClass() : null;
    this.data = jpData != null ? jpData.toMap() : null;
    this.access = access;

    if (linkedData != null) {
      this.linkedData = linkedData
          .toMap()
          .entrySet()
          .stream()
          .collect(
              Collectors.toMap(
                  Map.Entry::getKey,
                  e -> new JsonJPObject(metaStorage,
                      e.getValue().getJpId(), e.getValue().getData(), e.getValue().getLinkedData(),
                      null
                  )
              )
          );
    } else {
      this.linkedData = null;
    }
  }

  @JsonProperty("id")
  public Object getId() {
    return id;
  }

  @JsonProperty("classCode")
  public String getClassCode() {
    return classCode;
  }

  @JsonProperty("data")
  public Map<String, Object> getData() {
    return data;
  }

  @JsonProperty("linkedData")
  public Map<String, JsonJPObject> getLinkedData() {
    return linkedData;
  }

  @JsonProperty("access")
  public JsonChangeAccess getAccess() {
    return access;
  }

  /**
   * Построитель {@link JsonJPObject}
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель {@link JsonJPObject}
   */
  public static final class Builder {
    private JPMetaStorage metaStorage;
    private JPId jpId;
    private JPData jpData;
    private JPLinkedData jpLinkedData;
    private JsonChangeAccess access;

    private Builder() {
    }

    /**
     * MetaStorage
     *
     * @param metaStorage MetaStorage
     * @return Builder
     */
    public Builder metaStorage(JPMetaStorage metaStorage) {
      this.metaStorage = metaStorage;
      return this;
    }

    /**
     * JPId
     *
     * @param jpId JPId
     * @return Builder
     */
    public Builder jpId(JPId jpId) {
      this.jpId = jpId;
      return this;
    }

    /**
     * JPData
     *
     * @param jpData JPData
     * @return Builder
     */
    public Builder jpData(JPData jpData) {
      this.jpData = jpData;
      return this;
    }

    /**
     * JPLinkedData
     *
     * @param jpLinkedData JPLinkedData
     * @return Builder
     */
    public Builder jpLinkedData(JPLinkedData jpLinkedData) {
      this.jpLinkedData = jpLinkedData;
      return this;
    }

    /**
     * Настройки доступа к текущему объекту
     *
     * @param access JsonAccess
     * @return Builder
     */
    public Builder access(JsonChangeAccess access) {
      this.access = access;
      return this;
    }

    /**
     * Создаем {@link JsonJPObject}
     *
     * @return {@link JsonJPObject}
     */
    public JsonJPObject build() {
      return new JsonJPObject(metaStorage, jpId, jpData, jpLinkedData, access);
    }
  }
}