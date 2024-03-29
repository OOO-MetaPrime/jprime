package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание Link для HATEOAS
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonLink {
  /**
   * The rel attribute specifies the relationship between the current document and the linked document.
   * This may be a logical simple name used to reference the link and might have a value of “self” if it is pointing to itself.
   */
  private String rel;
  /**
   * Absolute URI
   */
  private String href;
  /**
   * refClassCode
   */
  private String refClassCode;
  /**
   * Relative Path
   */
  private String path;
  /**
   * The hreflang attribute is used to specify the language the data format is translated into (e.g. French, German, and English etc.).
   */
  private String hreflang = null;
  /**
   * The type attribute specifies the mediatype of the resource exchanged (e.g. application/xml).
   */
  private String type = null;
  private String media = null;
  private String title = null;

  @JsonProperty("rel")
  public String getRel() {
    return rel;
  }

  @JsonProperty("href")
  public String getHref() {
    return href;
  }

  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  @JsonProperty("hreflang")
  public String getHreflang() {
    return hreflang;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("media")
  public String getMedia() {
    return media;
  }

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("refClassCode")
  public String getRefClassCode() {
    return refClassCode;
  }

  /**
   * Конструктор
   */
  public JsonLink() {

  }

  /**
   * Конструктор
   *
   * @param rel                Тип ссылки
   * @param href               Absolute URI
   * @param path               Relative Path
   * @param refClassCode       ref JPClass code
   */
  private JsonLink(String rel, String href, String path, String refClassCode) {
    this.rel = rel;
    this.href = href;
    this.path = path;
    this.refClassCode = refClassCode;
  }

  /**
   * Построитель Link
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
    private String rel;
    private String baseUrl;
    private String restMapping;
    private String classCode;
    private String refClassCode;
    private List<String> blocks = new ArrayList<>();


    private Builder() {
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
     * rel
     *
     * @param rel rel
     * @return Builder
     */
    public Builder rel(String rel) {
      this.rel = rel;
      return this;
    }

    /**
     * refClassCode
     *
     * @param refClassCode refClassCode
     * @return Builder
     */
    public Builder refClassCode(String refClassCode) {
      this.refClassCode = refClassCode;
      return this;
    }

    /**
     * classCode
     *
     * @param classCode classCode
     * @return Builder
     */
    public Builder classCode(String classCode) {
      this.classCode = classCode;
      return this;
    }

    /**
     * block
     *
     * @param block block
     * @return Builder
     */
    public Builder block(String block) {
      if (block != null) {
        this.blocks.add(block);
      }
      return this;
    }

    /**
     * Создаем Link
     *
     * @return Link
     */
    public JsonLink build() {
      String sBlocks = String.join("/", blocks);
      String path = restMapping + "/" + classCode + "/" + sBlocks;
      return new JsonLink(rel,
          (baseUrl != null ? baseUrl : "") + "/" + path,
          path, refClassCode);
    }
  }
}
