package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPProperty {
  private String code;
  private String qName;
  private String name;
  private String shortName;
  private String description;
  private boolean mandatory;
  private boolean multiple;
  private String type;
  private Integer length;
  private String refJpClassCode;
  private String refJpAttrCode;
  @JsonProperty(value = "jpProps")
  private Collection<JsonJPProperty> schemaProps;

  public JsonJPProperty() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public String getRefJpClassCode() {
    return refJpClassCode;
  }

  public void setRefJpClassCode(String refJpClassCode) {
    this.refJpClassCode = refJpClassCode;
  }

  public String getRefJpAttrCode() {
    return refJpAttrCode;
  }

  public void setRefJpAttrCode(String refJpAttrCode) {
    this.refJpAttrCode = refJpAttrCode;
  }

  public Collection<JsonJPProperty> getSchemaProps() {
    return schemaProps;
  }

  public void setSchemaProps(Collection<JsonJPProperty> schemaProps) {
    this.schemaProps = schemaProps == null ? null : Collections.unmodifiableCollection(schemaProps);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String type;
    private Integer length;
    private boolean multiple;
    private boolean mandatory;
    private String name;
    private String shortName;
    private String description;
    private String qName;
    private String refJpClassCode;
    private String refJpAttrCode;
    private Collection<JsonJPProperty> schemaProps;

    private Builder() {
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder length(Integer length) {
      this.length = length;
      return this;
    }

    public Builder multiple(boolean multiple) {
      this.multiple = multiple;
      return this;
    }

    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder shortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder refJpClassCode(String refJpClassCode) {
      this.refJpClassCode = refJpClassCode;
      return this;
    }

    public Builder refJpAttrCode(String refJpAttrCode) {
      this.refJpAttrCode = refJpAttrCode;
      return this;
    }

    public Builder schemaProps(Collection<JsonJPProperty> schemaProps) {
      this.schemaProps = schemaProps;
      return this;
    }

    public JsonJPProperty build() {
      JsonJPProperty jsonJPProperty = new JsonJPProperty();
      jsonJPProperty.setCode(code);
      jsonJPProperty.setType(type);
      jsonJPProperty.setLength(length);
      jsonJPProperty.setMultiple(multiple);
      jsonJPProperty.setMandatory(mandatory);
      jsonJPProperty.setName(name);
      jsonJPProperty.setShortName(shortName);
      jsonJPProperty.setDescription(description);
      jsonJPProperty.setRefJpClassCode(refJpClassCode);
      jsonJPProperty.setRefJpAttrCode(refJpAttrCode);
      jsonJPProperty.setSchemaProps(schemaProps);
      jsonJPProperty.setqName(qName);
      return jsonJPProperty;
    }
  }
}
