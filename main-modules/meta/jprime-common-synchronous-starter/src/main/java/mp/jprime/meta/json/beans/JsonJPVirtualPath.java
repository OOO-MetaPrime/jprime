package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonJPVirtualPath {
  /**
   * Кодовое имя ссылочного атрибута, по которому строится ссылка
   */
  private String refAttrCode;
  /**
   * Кодовое имя целевого атрибута, на который строится ссылка
   */
  private String targetAttrCode;
  /**
   * Тип виртуальной ссылки
   */
  private String virtualType;

  public String getRefAttrCode() {
    return refAttrCode;
  }

  public String getTargetAttrCode() {
    return targetAttrCode;
  }

  public String getVirtualType() {
    return virtualType;
  }

  public JsonJPVirtualPath() {
  }


  private JsonJPVirtualPath(Builder builder) {
    refAttrCode = builder.refAttrCode;
    targetAttrCode = builder.targetAttrCode;
    virtualType = builder.virtualType;
  }

  /**
   * Построитель JsonJPVirtualPath
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPVirtualPath
   */
  public static final class Builder {
    private String refAttrCode;
    private String targetAttrCode;
    private String virtualType;

    private Builder() {
    }

    public Builder refAttrCode(String refAttrCode) {
      this.refAttrCode = refAttrCode;
      return this;
    }

    public Builder targetAttrCode(String targerAttrCode) {
      this.targetAttrCode = targerAttrCode;
      return this;
    }

    public Builder virtualType(String virtualType) {
      this.virtualType = virtualType;
      return this;
    }

    public JsonJPVirtualPath build() {
      return new JsonJPVirtualPath(this);
    }
  }
}
