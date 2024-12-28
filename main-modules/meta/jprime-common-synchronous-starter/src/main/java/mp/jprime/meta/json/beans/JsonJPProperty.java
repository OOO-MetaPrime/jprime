package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPOrder;
import mp.jprime.json.beans.JsonExpr;
import mp.jprime.json.beans.JsonJPEnum;
import mp.jprime.json.beans.JsonJPOrder;
import mp.jprime.meta.JPMoney;
import mp.jprime.meta.JPProperty;
import mp.jprime.meta.beans.JPMoneyBean;
import mp.jprime.meta.beans.JPPropertyBean;
import mp.jprime.meta.beans.JPStringFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    {
        "code",
        "name",
        "qName",
        "type",
        "money",
        "stringFormat",
        "stringMask",
        "mandatory",
        "length",
        "defValue",
        "refJpClass",
        "refJpAttr",
        "filter",
        "orders",
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
  private JsonJPMoney money;
  private String stringFormat;
  private String stringMask;
  private Integer length;
  private Object defValue;
  private String refJpClass;
  private String refJpAttr;
  private JsonExpr filter;
  private List<JsonJPOrder> orders = new ArrayList<>();
  private List<JsonJPEnum> enums = new ArrayList<>();
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

  public JsonJPMoney getMoney() {
    return money;
  }

  public void setMoney(JsonJPMoney money) {
    this.money = money;
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

  public Object getDefValue() {
    return defValue;
  }

  public void setDefValue(Object defValue) {
    this.defValue = defValue;
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

  public List<JsonJPOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<JsonJPOrder> orders) {
    this.orders = orders;
  }

  public Collection<JsonJPEnum> getEnums() {
    return enums;
  }

  public void setEnums(List<JsonJPEnum> enums) {
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
    private JsonJPMoney money;
    private String stringFormat;
    private String stringMask;
    private Integer length;
    private Object defValue;
    private boolean mandatory;
    private String name;
    private String qName;
    private String refJpClass;
    private String refJpAttr;
    private JsonExpr filter;
    private List<JsonJPOrder> orders;
    private List<JsonJPEnum> enums;
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

    public Builder money(JsonJPMoney money) {
      this.money = money;
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

    public Builder defValue(Object defValue) {
      this.defValue = defValue;
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

    public Builder orders(List<JsonJPOrder> orders) {
      this.orders = orders;
      return this;
    }

    public Builder enums(List<JsonJPEnum> enums) {
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
      result.setMoney(money);
      result.setStringFormat(stringFormat);
      result.setStringMask(stringMask);
      result.setLength(length);
      result.setDefValue(defValue);
      result.setMandatory(mandatory);
      result.setName(name);
      result.setqName(qName);
      result.setRefJpClass(refJpClass);
      result.setRefJpAttr(refJpAttr);
      result.setFilter(filter);
      result.setOrders(orders);
      result.setEnums(enums);
      result.setJpProps(jpProps);
      return result;
    }
  }

  public static JPProperty toJPProperty(JsonJPProperty json) {
    if (json == null) {
      return null;
    }
    JsonJPMoney money = json.getMoney();
    List<JsonJPOrder> orders = json.getOrders();
    Collection<JsonJPEnum> enums = json.getEnums();
    Collection<JsonJPProperty> properties = json.getJpProps();

    return JPPropertyBean.builder()
        .code(json.getCode())
        .type(JPPropertyType.getType(json.getType()))
        .money(money != null ? JPMoneyBean.of(money.getCurrencyCode()) : null)
        .stringFormat(JPStringFormat.getType(json.getStringFormat()))
        .stringMask(json.getStringMask())
        .length(json.getLength())
        .defValue(json.getDefValue())
        .mandatory(json.isMandatory())
        .name(json.getName())
        .qName(json.getqName())
        .refJpClass(json.getRefJpClass())
        .refJpAttr(json.getRefJpAttr())
        .filter(JsonExpr.toFilter(json.getFilter()))
        .orders(orders == null || orders.isEmpty() ? null :
            orders.stream()
                .map(JsonJPOrder::toJPOrder)
                .collect(Collectors.toList()))
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
    JPMoney money = property.getMoney();
    JPStringFormat stringFormat = property.getStringFormat();
    List<JPOrder> orders = property.getOrders();
    List<JPEnum> enums = property.getEnums();
    Collection<JPProperty> properties = property.getJpProps();

    return JsonJPProperty.builder()
        .code(property.getCode())
        .type(type != null ? type.getCode() : null)
        .money(money != null ? JsonJPMoney.of(money.getCurrencyCode()) : null)
        .stringFormat(stringFormat != null ? stringFormat.getCode() : null)
        .stringMask(property.getStringMask())
        .length(property.getLength())
        .defValue(property.getDefValue())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .qName(property.getQName())
        .refJpClass(property.getRefJpClass())
        .refJpAttr(property.getRefJpAttr())
        .filter(JsonExpr.toJson(property.getFilter()))
        .orders(orders == null || orders.isEmpty() ? null :
            orders.stream()
                .map(JsonJPOrder::toJson)
                .collect(Collectors.toList()))
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
