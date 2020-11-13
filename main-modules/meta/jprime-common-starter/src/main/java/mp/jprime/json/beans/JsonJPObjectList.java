package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Список объектов
 */
@JsonPropertyOrder({
    "offset",
    "limit",
    "totalCount",
    "objectsCount",
    "classCode",
    "pluralCode",
    "objects"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObjectList {
  @JsonProperty("offset")
  private Integer offset;
  @JsonProperty("limit")
  private Integer limit;
  @JsonProperty("totalCount")
  private Long totalCount;
  @JsonProperty("classCode")
  private String classCode;
  @JsonProperty("pluralCode")
  private String pluralCode;
  @JsonProperty("objects")
  private Collection<JsonJPObject> objects = new ArrayList<>();

  /**
   * Конструктор
   */
  public JsonJPObjectList() {

  }

  /**
   * Конструктор
   *
   * @param offset     Смещение начала выборки
   * @param limit      Объектов в выборке
   * @param totalCount Точное кол-во объектов
   * @param classCode  Кодовое имя класса объектов
   * @param pluralCode Множественное кодовое имя класса объектов
   * @param objects    Объекты
   */
  private JsonJPObjectList(Integer offset, Integer limit, Long totalCount, String classCode, String pluralCode,
                           Collection<JsonJPObject> objects) {
    this.offset = offset;
    this.limit = limit;
    this.totalCount = totalCount;
    this.classCode = classCode;
    this.pluralCode = pluralCode;
    this.objects = Collections.unmodifiableCollection(objects == null ? Collections.emptyList() : objects);
  }

  /**
   * Смещение начала выборки
   *
   * @return Смещение начала выборки
   */
  public Integer getOffset() {
    return offset;
  }

  /**
   * Объектов в выборке
   *
   * @return Объектов в выборке
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Точное кол-во объектов
   *
   * @return Точное кол-во объектов
   */
  public Long getTotalCount() {
    return totalCount;
  }

  /**
   * Кодовое имя класса объектов
   *
   * @return Кодовое имя класса объектов
   */
  public String getClassCode() {
    return classCode;
  }

  /**
   * Множественное кодовое имя класса объектов
   *
   * @return Множественное кодовое имя класса объектов
   */
  public String getPluralCode() {
    return pluralCode;
  }

  /**
   * Количество объектов
   *
   * @return Количество объектов
   */
  @JsonProperty("objectsCount")
  public Integer getObjectsCount() {
    return objects.size();
  }

  /**
   * Объекты
   *
   * @return Объекты
   */
  public Collection<JsonJPObject> getObjects() {
    return objects;
  }

  /**
   * Построитель ListResult
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель ListResult
   */
  public static final class Builder {
    private Integer offset;
    private Integer limit;
    private Long totalCount;
    private String classCode;
    private String pluralCode;
    private Collection<JsonJPObject> objects = new ArrayList<>();

    private Builder() {
    }

    /**
     * Создаем ListResult
     *
     * @return ListResult
     */
    public JsonJPObjectList build() {
      return new JsonJPObjectList(offset, limit, totalCount, classCode, pluralCode, objects);
    }

    /**
     * Смещение начала выборки
     *
     * @param offset Смещение начала выборки
     * @return Builder
     */
    public Builder offset(Integer offset) {
      this.offset = offset;
      return this;
    }

    /**
     * Объектов в выборке
     *
     * @param limit Объектов в выборке
     * @return Builder
     */
    public Builder limit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Точное кол-во объектов
     *
     * @param totalCount Точное кол-во объектов
     * @return Builder
     */
    public Builder totalCount(Long totalCount) {
      this.totalCount = totalCount;
      return this;
    }

    /**
     * Кодовое имя класса объектов
     *
     * @param classCode Кодовое имя класса объектов
     * @return Builder
     */
    public Builder classCode(String classCode) {
      this.classCode = classCode;
      return this;
    }

    /**
     * Множественное кодовое имя класса объектов
     *
     * @param pluralCode Множественное кодовое имя класса объектов
     * @return Builder
     */
    public Builder pluralCode(String pluralCode) {
      this.pluralCode = pluralCode;
      return this;
    }

    /**
     * Список JsonJPObject
     *
     * @param objects Список JsonJPObject
     * @return Builder
     */
    public Builder objects(Collection<JsonJPObject> objects) {
      if (objects != null) {
        this.objects.addAll(objects);
      }
      return this;
    }

    /**
     * JsonJPObject
     *
     * @param object JsonJPObject
     * @return Builder
     */
    public Builder object(JsonJPObject object) {
      if (object != null) {
        this.objects.add(object);
      }
      return this;
    }
  }
}
