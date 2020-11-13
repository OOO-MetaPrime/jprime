package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание Link для HATEOAS
 */
@JsonPropertyOrder({
    "rel",
    "href",
    "hreflang",
    "type",
    "media",
    "title",
    "path",
    "refClassCode",
    "refClassPluralCode"
})
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
   * refClassPluralCode
   */
  private String refClassPluralCode;
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

  @JsonProperty("refClassPluralCode")
  public String getRefClassPluralCode() {
    return refClassPluralCode;
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
   * @param refClassPluralCode ref JPClass plural code
   */
  private JsonLink(String rel, String href, String path, String refClassCode, String refClassPluralCode) {
    this.rel = rel;
    this.href = href;
    this.path = path;
    this.refClassCode = refClassCode;
    this.refClassPluralCode = refClassPluralCode;
  }

  /**
   * Построитель Link
   *
   * @return Builder
   */
  public static JsonLink.Builder newBuilder() {
    return new JsonLink.Builder();
  }

  /**
   * Построитель ListResult
   */
  public static final class Builder {
    private String rel;
    private String baseUrl;
    private String restMapping;
    private String classPluralCode;
    private String refClassCode;
    private String refClassPluralCode;
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
     * refClassCode
     *
     * @param refClassPluralCode refClassPluralCode
     * @return Builder
     */
    public Builder refClassPluralCode(String refClassPluralCode) {
      this.refClassPluralCode = refClassPluralCode;
      return this;
    }

    /**
     * classPluralCode
     *
     * @param classPluralCode classPluralCode
     * @return Builder
     */
    public Builder classPluralCode(String classPluralCode) {
      this.classPluralCode = classPluralCode;
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
      String path = restMapping + "/" + classPluralCode + "/" + sBlocks;
      return new JsonLink(rel,
          (baseUrl != null ? baseUrl : "") + "/" + path,
          path, refClassCode, refClassPluralCode);
    }
  }
}
