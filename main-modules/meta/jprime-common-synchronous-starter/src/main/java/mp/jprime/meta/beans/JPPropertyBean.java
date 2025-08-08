package mp.jprime.meta.beans;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPOrder;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.JPMoney;
import mp.jprime.meta.JPProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Описание свойства псевдо-меты
 */
public final class JPPropertyBean implements JPProperty {
  private final String code;
  private final JPPropertyType type;
  private final JPMoney money;
  private final JPStringFormat stringFormat;
  private final String stringMask;
  private final Integer length;
  private final Object defValue;
  private final boolean mandatory;
  private final String name;
  private final String qName;
  private final String refJpClass;
  private final String refJpAttr;
  private final Filter filter;
  private final List<JPOrder> orders;
  private final List<JPEnum> enums;
  private final Collection<JPProperty> jpProps;

  private JPPropertyBean(String code, JPPropertyType type, JPMoney money,
                         JPStringFormat stringFormat, String stringMask, Integer length, Object defValue,
                         boolean mandatory, String name, String qName,
                         String refJpClass, String refJpAttr, Filter filter, List<JPOrder> orders,
                         List<JPEnum> enums, Collection<JPProperty> jpProps) {
    this.code = code;
    this.type = type;
    this.money = money;
    this.stringFormat = stringFormat;
    this.stringMask = stringMask;
    this.length = length;
    this.defValue = defValue;
    this.mandatory = mandatory;
    this.name = name;
    this.qName = qName;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
    this.filter = filter;
    this.orders = orders == null ? Collections.emptyList() : Collections.unmodifiableList(orders);
    this.enums = enums == null ? Collections.emptyList() : Collections.unmodifiableList(enums);
    this.jpProps = jpProps == null ? Collections.emptyList() : Collections.unmodifiableCollection(jpProps);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public JPPropertyType getType() {
    return type;
  }

  @Override
  public JPMoney getMoney() {
    return money;
  }

  @Override
  public JPStringFormat getStringFormat() {
    return stringFormat;
  }

  @Override
  public String getStringMask() {
    return stringMask;
  }

  @Override
  public Integer getLength() {
    return length;
  }

  @Override
  public Object getDefValue() {
    return defValue;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getQName() {
    return qName;
  }

  @Override
  public String getRefJpClass() {
    return refJpClass;
  }

  @Override
  public String getRefJpAttr() {
    return refJpAttr;
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  @Override
  public List<JPOrder> getOrders() {
    return orders;
  }

  @Override
  public List<JPEnum> getEnums() {
    return enums;
  }

  @Override
  public Collection<JPProperty> getJpProps() {
    return jpProps;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private JPPropertyType type;
    private JPMoney money;
    private JPStringFormat stringFormat;
    private String stringMask;
    private Integer length;
    private Object defValue;
    private boolean mandatory;
    private String name;
    private String qName;
    private String refJpClass;
    private String refJpAttr;
    private Filter filter;
    private List<JPOrder> orders;
    private List<JPEnum> enums;
    private Collection<JPProperty> jpProps;

    private Builder() {
    }

    /**
     * Кодовое имя свойства
     *
     * @return Кодовое имя свойства
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Тип свойства
     */
    public Builder type(JPPropertyType type) {
      this.type = type;
      return this;
    }

    /**
     * настройки денежного типа
     */
    public Builder money(JPMoney money) {
      this.money = money;
      return this;
    }

    /**
     * Тип строкового поля
     */
    public Builder stringFormat(JPStringFormat stringFormat) {
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
     * Длина (для строк)
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
     * Уникальный qName свойства
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Код класса, на который ссылается
     */
    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    /**
     * Код атрибута, на который ссылается
     */
    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    /**
     * Условие, на объекты класса
     */
    public Builder filter(Filter filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Настройка сортировки
     */
    public Builder orders(List<JPOrder> orders) {
      this.orders = orders;
      return this;
    }


    /**
     * Перечислимые значения
     */
    public Builder enums(List<JPEnum> enums) {
      this.enums = enums;
      return this;
    }

    /**
     * Список вложенных свойств
     */
    public Builder jpProps(Collection<JPProperty> jpProps) {
      this.jpProps = jpProps;
      return this;
    }

    public JPPropertyBean build() {
      return new JPPropertyBean(code, type, money, stringFormat, stringMask, length, defValue,
          mandatory, name, qName, refJpClass, refJpAttr, filter, orders, enums, jpProps);
    }
  }
}
