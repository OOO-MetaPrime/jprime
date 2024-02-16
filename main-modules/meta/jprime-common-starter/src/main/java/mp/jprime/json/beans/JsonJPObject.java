package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Модель данных ответа получения объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObject {
  private Object id;
  private String classCode;
  private Map<String, Object> data;
  private Map<String, JsonJPObject> linkedData;
  private Collection<JsonLink> links;
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
   * @param baseUrl     String
   * @param restMapping String
   */
  private JsonJPObject(JPMetaStorage metaStorage, JPId jpId, JPData jpData, JPLinkedData linkedData,
                       JsonChangeAccess access, String baseUrl, String restMapping, boolean addLinks) {
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
                      null, baseUrl, null, addLinks
                  )
              )
          );
    } else {
      this.linkedData = null;
    }

    if (!addLinks) {
      return;
    }

    JPClass cls = metaStorage != null && classCode != null ? metaStorage.getJPClassByCode(classCode) : null;
    if (jpId == null || cls == null || restMapping == null) {
      return;
    }
    String sId = String.valueOf(jpId.getId());
    this.links = new ArrayList<>();
    // Сам объект
    this.links.add(JsonLink.newBuilder()
        .rel("self")
        .baseUrl(baseUrl)
        .restMapping(restMapping)
        .classCode(cls.getCode())
        .block(sId)
        .build());

    if (jpData == null) {
      return;
    }
    // Все его ссылочные
    this.links.addAll(cls.getAttrs()
        .stream()
        .filter(x -> x.getRefJpClassCode() != null)
        .map(x -> {
          JsonLink.Builder builder = JsonLink.newBuilder()
              .rel(x.getCode())
              .baseUrl(baseUrl)
              .restMapping(restMapping);

          JPClass refClass = metaStorage.getJPClassByCode(x.getRefJpClassCode());
          if (refClass == null) {
            return null;
          }
          JPAttr refAttr = refClass.getAttr(x.getRefJpAttrCode());
          if (refAttr == null) {
            return null;
          }
          if (x.getType() == JPType.BACKREFERENCE) {
            builder
                .classCode(cls.getCode())
                .block(sId)
                .block(x.getCode())
                .refClassCode(refClass.getCode());
          } else if (!refAttr.isIdentifier()) {
            Object val = jpData.get(x);
            if (val == null) {
              return null;
            }
            builder
                .classCode(cls.getCode())
                .block(sId)
                .block(x.getCode())
                .block(String.valueOf(val))
                .refClassCode(refClass.getCode());
          } else {
            Object val = jpData.get(x);
            if (val == null) {
              return null;
            }
            builder
                .classCode(refClass.getCode())
                .block(String.valueOf(val))
                .refClassCode(refClass.getCode());
          }
          return builder.build();
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList()));
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

  @JsonProperty("links")
  public Collection<JsonLink> getLinks() {
    return links;
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
    private String baseUrl;
    private String restMapping;
    private boolean addLinks = false;
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
     * baseUrl
     *
     * @param baseUrl baseUrl
     * @return Builder
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * restMapping
     *
     * @param restMapping restMapping
     * @return Builder
     */
    public Builder restMapping(String restMapping) {
      this.restMapping = restMapping;
      return this;
    }

    /**
     * addLinks
     *
     * @param addLinks признак добавления блока links (по умолчанию - {@code false})
     * @return Builder
     */
    public Builder addLinks(boolean addLinks) {
      this.addLinks = addLinks;
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
      return new JsonJPObject(metaStorage, jpId, jpData, jpLinkedData, access, baseUrl, restMapping, addLinks);
    }
  }
}