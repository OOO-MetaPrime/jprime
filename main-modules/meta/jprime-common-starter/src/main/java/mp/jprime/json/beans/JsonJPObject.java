package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.dataaccess.beans.JPObject;
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
@JsonPropertyOrder({
    "id",
    "classCode",
    "title",
    "data",
    "linkedData",
    "links"
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObject {
  private Object id;
  private String classCode;
  private Map<String, Object> data;
  private Map<String, JsonJPObject> linkedData;
  private Collection<JsonLink> links = new ArrayList<>();

  public JsonJPObject() {

  }

  /**
   * Конструктор
   *
   * @param metaStorage MetaStorage
   * @param jpObject    JPObject
   * @param baseUrl     String
   * @param restMapping String
   */
  private JsonJPObject(JPMetaStorage metaStorage, JPObject jpObject, String baseUrl, String restMapping) {
    JPId jpId = jpObject != null ? jpObject.getJpId() : null;
    this.id = jpId != null ? jpId.getId() : null;
    this.data = jpObject != null ? jpObject.getData().toMap() : null;
    this.classCode = jpObject != null ? jpObject.getJpClassCode() : null;

    JPLinkedData linkedData = jpObject != null ? jpObject.getLinkedData() : null;
    if (linkedData != null) {
      this.linkedData = linkedData
          .toMap()
          .entrySet()
          .stream()
          .collect(
              Collectors.toMap(
                  Map.Entry::getKey,
                  e -> new JsonJPObject(metaStorage, e.getValue(), baseUrl, null)
              )
          );
    } else {
      this.linkedData = null;
    }
    JPClass cls = metaStorage != null && jpObject != null ? metaStorage.getJPClassByCode(jpObject.getJpClassCode()) : null;
    if (cls == null || restMapping == null) {
      return;
    }
    String sId = String.valueOf(jpObject.getJpId().getId());
    // Сам объект
    this.links.add(JsonLink.newBuilder()
        .rel("self")
        .baseUrl(baseUrl)
        .restMapping(restMapping)
        .classPluralCode(cls.getPluralCode())
        .block(sId)
        .build());
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
                .classPluralCode(cls.getPluralCode())
                .block(sId)
                .block(x.getCode())
                .refClassCode(refClass.getCode())
                .refClassPluralCode(refClass.getPluralCode());
          } else if (!refAttr.isIdentifier()) {
            Object val = jpObject.getData().get(x);
            if (val == null) {
              return null;
            }
            builder
                .classPluralCode(cls.getPluralCode())
                .block(sId)
                .block(x.getCode())
                .block(String.valueOf(val))
                .refClassCode(refClass.getCode())
                .refClassPluralCode(refClass.getPluralCode());
          } else {
            Object val = jpObject.getData().get(x);
            if (val == null) {
              return null;
            }
            builder
                .classPluralCode(refClass.getPluralCode())
                .block(String.valueOf(val))
                .refClassCode(refClass.getCode())
                .refClassPluralCode(refClass.getPluralCode());
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

  @JsonProperty("title")
  public String getTitle() {
    return null;
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

  /**
   * Построитель JsonJPObject
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPObject
   */
  public static final class Builder {
    private JPMetaStorage metaStorage;
    private JPObject jpObject;
    private String baseUrl;
    private String restMapping;

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
     * JPObject
     *
     * @param jpObject JPObject
     * @return Builder
     */
    public Builder jpObject(JPObject jpObject) {
      this.jpObject = jpObject;
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
     * Создаем JsonJPObject
     *
     * @return JsonJPObject
     */
    public JsonJPObject build() {
      return new JsonJPObject(metaStorage, jpObject, baseUrl, restMapping);
    }
  }
}