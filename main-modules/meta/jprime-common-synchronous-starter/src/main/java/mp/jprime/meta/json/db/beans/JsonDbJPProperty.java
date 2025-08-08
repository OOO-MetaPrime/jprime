package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPOrder;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.json.beans.JsonExpr;
import mp.jprime.json.db.beans.JsonDbJPEnum;
import mp.jprime.json.db.beans.JsonDbJPOrder;
import mp.jprime.meta.JPMoney;
import mp.jprime.meta.JPProperty;
import mp.jprime.meta.beans.JPMoneyBean;
import mp.jprime.meta.beans.JPPropertyBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDbJPProperty {
  private String code;
  private String qName;
  private String name;
  private boolean mandatory;
  private String type;
  private JsonDbJPMoney money;
  private String stringFormat;
  private String stringMask;
  private Integer length;
  private Object defValue;
  private String refJpClass;
  private String refJpAttr;
  private JsonExpr filter;
  private List<JsonDbJPOrder> orders = new ArrayList<>();
  private List<JsonDbJPEnum> enums = new ArrayList<>();
  private Collection<JsonDbJPProperty> jpProps;

  public JsonDbJPProperty() {
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

  public JsonDbJPMoney getMoney() {
    return money;
  }

  public void setMoney(JsonDbJPMoney money) {
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

  public Collection<JsonDbJPEnum> getEnums() {
    return enums;
  }

  public void setEnums(List<JsonDbJPEnum> enums) {
    this.enums = enums;
  }

  public Collection<JsonDbJPOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<JsonDbJPOrder> orders) {
    this.orders = orders;
  }

  public Collection<JsonDbJPProperty> getJpProps() {
    return jpProps;
  }

  public void setJpProps(Collection<JsonDbJPProperty> jpProps) {
    this.jpProps = jpProps == null ? null : Collections.unmodifiableCollection(jpProps);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String type;
    private JsonDbJPMoney money;
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
    private List<JsonDbJPOrder> orders;
    private List<JsonDbJPEnum> enums;
    private Collection<JsonDbJPProperty> jpProps;

    private Builder() {
    }

    /**
     * Кодовое имя свойства
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Тип свойства
     */
    public Builder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * Тип денежного поля
     */
    public Builder money(JsonDbJPMoney money) {
      this.money = money;
      return this;
    }

    /**
     * Формат строкового поля
     */
    public Builder stringFormat(String stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    /**
     * Маска строкового поля
     */
    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
      return this;
    }

    /**
     * Длина (для строковых полей)
     */
    public Builder length(Integer length) {
      this.length = length;
      return this;
    }

    /**
     * Значение по умолчанию
     */
    public Builder defValue(Object defValue) {
      this.defValue = defValue;
      return this;
    }

    /**
     * Признак обязательности
     */
    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    /**
     * Название свойства
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Полный код свойства
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Кодовое имя класса, на который ссылается
     */
    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    /**
     * Кодовое имя атрибута ссылочного класса
     */
    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    /**
     * Условие, на объекты класса
     */
    public Builder filter(JsonExpr filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Настройки сортировки
     */
    public Builder orders(List<JsonDbJPOrder> orders) {
      this.orders = orders;
      return this;
    }

    /**
     * Перечислимые значения
     */
    public Builder enums(List<JsonDbJPEnum> enums) {
      this.enums = enums;
      return this;
    }

    /**
     * Схема свойств псевдо-меты
     */
    public Builder jpProps(Collection<JsonDbJPProperty> jpProps) {
      this.jpProps = jpProps;
      return this;
    }

    public JsonDbJPProperty build() {
      JsonDbJPProperty result = new JsonDbJPProperty();
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

  public static JPProperty toJPProperty(JsonDbJPProperty json) {
    if (json == null) {
      return null;
    }

    JsonDbJPMoney money = json.getMoney();
    Collection<JsonDbJPOrder> orders = json.getOrders();
    Collection<JsonDbJPEnum> enums = json.getEnums();
    Collection<JsonDbJPProperty> properties = json.getJpProps();

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
                .map(JsonDbJPOrder::toJPOrder)
                .collect(Collectors.toList()))
        .enums(enums == null || enums.isEmpty() ? null :
            enums.stream()
                .map(JsonDbJPEnum::toJPEnum)
                .collect(Collectors.toList()))
        .jpProps(properties == null || properties.isEmpty() ? null :
            properties.stream()
                .map(JsonDbJPProperty::toJPProperty)
                .collect(Collectors.toList()))
        .build();
  }

  public static JsonDbJPProperty toJson(JPProperty property) {
    if (property == null) {
      return null;
    }
    JPPropertyType type = property.getType();
    JPMoney money = property.getMoney();
    JPStringFormat stringFormat = property.getStringFormat();
    Collection<JPOrder> orders = property.getOrders();
    Collection<JPEnum> enums = property.getEnums();
    Collection<JPProperty> properties = property.getJpProps();

    return JsonDbJPProperty.builder()
        .code(property.getCode())
        .type(type != null ? type.getCode() : null)
        .money(money != null ? JsonDbJPMoney.of(money.getCurrencyCode()) : null)
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
                .map(JsonDbJPOrder::toJson)
                .collect(Collectors.toList()))
        .enums(enums == null || enums.isEmpty() ? null :
            enums.stream()
                .map(JsonDbJPEnum::toJson)
                .collect(Collectors.toList()))
        .jpProps(properties == null || properties.isEmpty() ? null :
            properties.stream()
                .map(JsonDbJPProperty::toJson)
                .collect(Collectors.toList()))
        .build();
  }
}
