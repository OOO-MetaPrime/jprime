package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.json.beans.JsonExpr;
import mp.jprime.json.beans.JsonJPEnum;
import mp.jprime.meta.JPProperty;
import mp.jprime.meta.beans.JPPropertyBean;
import mp.jprime.meta.beans.JPStringFormat;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    {
        "code",
        "name",
        "qName",
        "type",
        "stringFormat",
        "stringMask",
        "mandatory",
        "length",
        "refJpClass",
        "refJpAttr",
        "filter",
        "enums",
        "jpProps",
    }
)
public final class JsonJPProperty {
  private String code;
  private String qName;
  private String name;
  private boolean mandatory;
  private String type;
  private String stringFormat;
  private String stringMask;
  private Integer length;
  private String refJpClass;
  private String refJpAttr;
  private JsonExpr filter;
  private Collection<JsonJPEnum> enums;
  private Collection<JsonJPProperty> jpProps;

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

  public String getStringFormat() {
    return stringFormat;
  }

  public void setStringFormat(String stringFormat) {
    this.stringFormat = stringFormat;
  }

  public String getStringMask() {
    return stringMask;
  }

  public void setStringMask(String stringMask) {
    this.stringMask = stringMask;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
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

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public String getRefJpClass() {
    return refJpClass;
  }

  public void setRefJpClass(String refJpClass) {
    this.refJpClass = refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public void setRefJpAttr(String refJpAttr) {
    this.refJpAttr = refJpAttr;
  }

  public JsonExpr getFilter() {
    return filter;
  }

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }

  public Collection<JsonJPEnum> getEnums() {
    return enums;
  }

  public void setEnums(Collection<JsonJPEnum> enums) {
    this.enums = enums;
  }

  public Collection<JsonJPProperty> getJpProps() {
    return jpProps;
  }

  public void setJpProps(Collection<JsonJPProperty> jpProps) {
    this.jpProps = jpProps == null ? null : Collections.unmodifiableCollection(jpProps);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String type;
    private String stringFormat;
    private String stringMask;
    private Integer length;
    private boolean mandatory;
    private String name;
    private String qName;
    private String refJpClass;
    private String refJpAttr;
    private JsonExpr filter;
    private Collection<JsonJPEnum> enums;
    private Collection<JsonJPProperty> jpProps;

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

    public Builder stringFormat(String stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
      return this;
    }

    public Builder length(Integer length) {
      this.length = length;
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

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    public Builder filter(JsonExpr filter) {
      this.filter = filter;
      return this;
    }

    public Builder enums(Collection<JsonJPEnum> enums) {
      this.enums = enums;
      return this;
    }

    public Builder jpProps(Collection<JsonJPProperty> jpProps) {
      this.jpProps = jpProps;
      return this;
    }

    public JsonJPProperty build() {
      JsonJPProperty result = new JsonJPProperty();
      result.setCode(code);
      result.setType(type);
      result.setStringFormat(stringFormat);
      result.setStringMask(stringMask);
      result.setLength(length);
      result.setMandatory(mandatory);
      result.setName(name);
      result.setqName(qName);
      result.setRefJpClass(refJpClass);
      result.setRefJpAttr(refJpAttr);
      result.setFilter(filter);
      result.setEnums(enums);
      result.setJpProps(jpProps);
      return result;
    }
  }

  public static JPProperty toJPProperty(JsonJPProperty json) {
    if (json == null) {
      return null;
    }
    Collection<JsonJPEnum> enums = json.getEnums();
    Collection<JsonJPProperty> properties = json.getJpProps();

    return JPPropertyBean.builder()
        .code(json.getCode())
        .type(JPPropertyType.getType(json.getType()))
        .stringFormat(JPStringFormat.getType(json.getStringFormat()))
        .stringMask(json.getStringMask())
        .length(json.getLength())
        .mandatory(json.isMandatory())
        .name(json.getName())
        .qName(json.getqName())
        .refJpClass(json.getRefJpClass())
        .refJpAttr(json.getRefJpAttr())
        .filter(JsonExpr.toFilter(json.getFilter()))
        .enums(enums == null || enums.isEmpty() ? null :
            enums.stream()
                .map(JsonJPEnum::toJPEnum)
                .collect(Collectors.toList()))
        .jpProps(properties == null || properties.isEmpty() ? null :
            properties.stream()
                .map(JsonJPProperty::toJPProperty)
                .collect(Collectors.toList()))
        .build();
  }

  public static JsonJPProperty toJson(JPProperty property) {
    if (property == null) {
      return null;
    }
    JPPropertyType type = property.getType();
    JPStringFormat stringFormat = property.getStringFormat();
    Collection<JPEnum> enums = property.getEnums();
    Collection<JPProperty> properties = property.getJpProps();

    return JsonJPProperty.builder()
        .code(property.getCode())
        .type(type != null ? type.getCode() : null)
        .stringFormat(stringFormat != null ? stringFormat.getCode() : null)
        .stringMask(property.getStringMask())
        .length(property.getLength())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .qName(property.getQName())
        .refJpClass(property.getRefJpClass())
        .refJpAttr(property.getRefJpAttr())
        .filter(JsonExpr.toJson(property.getFilter()))
        .enums(enums == null || enums.isEmpty() ? null :
            enums.stream()
                .map(JsonJPEnum::toJson)
                .collect(Collectors.toList()))
        .jpProps(properties == null || properties.isEmpty() ? null :
            properties.stream()
                .map(JsonJPProperty::toJson)
                .collect(Collectors.toList()))
        .build();
  }
}
