package mp.jprime.json.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestEntity {
  @JsonProperty("specificStringFieldName")
  @JsonViewProperty("Значение строка")
  private String stringValue;

  @JsonViewProperty(value = "Номер карты", toClass = CardNumberValueHolder.class)
  private String cardNumber;
  @JsonViewProperty("Логическое")
  private Boolean booleanValue;
  @JsonViewProperty("Целочисленное (32 бит)")
  private Integer integerValue;
  @JsonViewProperty("Целочисленное (64 бит)")
  private Long longValue;
  @JsonViewProperty("Вещественное")
  private Double doubleValue;
  @JsonViewProperty("Дата")
  private LocalDate localDateValue;
  @JsonViewProperty("Дата и время")
  private LocalDateTime localDateTimeValue;
  @JsonViewProperty("Время")
  private LocalTime localTimeValue;
  @JsonViewProperty("Вложенный объект")
  private TestNestedEntity nestedEntity;
  @JsonViewCollection(value = "Коллекция элементов", entity = "Элемент")
  private Collection<String> collectionItems;
  @JsonViewCollection(value = "Коллекция простых элементов", entity = "Простой элемент")
  private Collection<TestSimpleItem> simpleItems;
  @JsonViewCollection(value = "Коллекция составных элементов", entity = "Составной элемент")
  private Collection<TestObjectItem> objectItems;

  public TestEntity() {
  }

  private TestEntity(Builder builder) {
    stringValue = builder.stringValue;
    cardNumber = builder.cardNumber;
    booleanValue = builder.booleanValue;
    integerValue = builder.integerValue;
    longValue = builder.longValue;
    doubleValue = builder.doubleValue;
    localDateValue = builder.localDateValue;
    localDateTimeValue = builder.localDateTimeValue;
    localTimeValue = builder.localTimeValue;
    nestedEntity = builder.nestedEntity;
    collectionItems = builder.collectionItems;
    simpleItems = builder.simpleItems;
    objectItems = builder.objectItems;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  public Integer getIntegerValue() {
    return integerValue;
  }

  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  public Double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public LocalDate getLocalDateValue() {
    return localDateValue;
  }

  public void setLocalDateValue(LocalDate localDateValue) {
    this.localDateValue = localDateValue;
  }

  public LocalDateTime getLocalDateTimeValue() {
    return localDateTimeValue;
  }

  public void setLocalDateTimeValue(LocalDateTime localDateTimeValue) {
    this.localDateTimeValue = localDateTimeValue;
  }

  public LocalTime getLocalTimeValue() {
    return localTimeValue;
  }

  public void setLocalTimeValue(LocalTime localTimeValue) {
    this.localTimeValue = localTimeValue;
  }

  public TestNestedEntity getNestedEntity() {
    return nestedEntity;
  }

  public void setNestedEntity(TestNestedEntity nestedEntity) {
    this.nestedEntity = nestedEntity;
  }

  public Collection<String> getCollectionItems() {
    return collectionItems;
  }

  public void setCollectionItems(Collection<String> collectionItems) {
    this.collectionItems = collectionItems;
  }

  public Collection<TestSimpleItem> getSimpleItems() {
    return simpleItems;
  }

  public void setSimpleItems(Collection<TestSimpleItem> simpleItems) {
    this.simpleItems = simpleItems;
  }

  @JsonSetter
  public void setSimpleItemsJson(Collection<String> simpleItems) {
    if (simpleItems == null) {
      return;
    }
    this.simpleItems = simpleItems.stream().map(TestSimpleItem::of).collect(Collectors.toList());
  }

  public Collection<TestObjectItem> getObjectItems() {
    return objectItems;
  }

  public void setObjectItems(Collection<TestObjectItem> objectItems) {
    this.objectItems = objectItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestEntity that = (TestEntity) o;
    return Objects.equals(stringValue, that.stringValue)
        // && Objects.equals(cardNumber, that.cardNumber)
        && Objects.equals(booleanValue, that.booleanValue)
        && Objects.equals(integerValue, that.integerValue)
        && Objects.equals(longValue, that.longValue)
        && Objects.equals(doubleValue, that.doubleValue)
        && Objects.equals(localDateValue, that.localDateValue)
        && (
            (localDateTimeValue == that.localDateTimeValue)
            || (localDateTimeValue != null && that.localDateTimeValue != null
                && Duration.between(localDateTimeValue, that.localDateTimeValue).toMillis() < 1_000
            )
        )
        && (
            (localTimeValue == that.localTimeValue)
            || (localDateValue != null && that.localTimeValue != null
                && Duration.between(localTimeValue, that.localTimeValue).toMillis() < 1_000
            )
        )
        && Objects.equals(nestedEntity, that.nestedEntity)
        && Objects.deepEquals(collectionItems, that.collectionItems)
        && Objects.equals(simpleItems, that.simpleItems)
        && Objects.equals(objectItems, that.objectItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stringValue, booleanValue, integerValue, longValue, doubleValue, localDateValue, localDateTimeValue, localTimeValue, nestedEntity, collectionItems, simpleItems, objectItems);
  }

  public static final class Builder {
    private String stringValue;
    private String cardNumber;
    private Boolean booleanValue;
    private Integer integerValue;
    private Long longValue;
    private Double doubleValue;
    private LocalDate localDateValue;
    private LocalDateTime localDateTimeValue;
    private LocalTime localTimeValue;
    private TestNestedEntity nestedEntity;
    private Collection<String> collectionItems;
    private Collection<TestSimpleItem> simpleItems;
    private Collection<TestObjectItem> objectItems;

    private Builder() {
    }

    public Builder stringValue(String stringValue) {
      this.stringValue = stringValue;
      return this;
    }

    public Builder cardNumber(String cardNumber) {
      this.cardNumber = cardNumber;
      return this;
    }

    public Builder booleanValue(Boolean booleanValue) {
      this.booleanValue = booleanValue;
      return this;
    }

    public Builder integerValue(Integer integerValue) {
      this.integerValue = integerValue;
      return this;
    }

    public Builder longValue(Long longValue) {
      this.longValue = longValue;
      return this;
    }

    public Builder doubleValue(Double doubleValue) {
      this.doubleValue = doubleValue;
      return this;
    }

    public Builder localDateValue(LocalDate localDateValue) {
      this.localDateValue = localDateValue;
      return this;
    }

    public Builder localDateTimeValue(LocalDateTime localDateTimeValue) {
      this.localDateTimeValue = localDateTimeValue;
      return this;
    }

    public Builder localTimeValue(LocalTime localTimeValue) {
      this.localTimeValue = localTimeValue;
      return this;
    }

    public Builder nestedEntity(TestNestedEntity nestedEntity) {
      this.nestedEntity = nestedEntity;
      return this;
    }

    public Builder collectionItems(Collection<String> collectionItems) {
      this.collectionItems = collectionItems;
      return this;
    }

    public Builder simpleItems(Collection<TestSimpleItem> simpleItems) {
      this.simpleItems = simpleItems;
      return this;
    }

    public Builder objectItems(Collection<TestObjectItem> objectItems) {
      this.objectItems = objectItems;
      return this;
    }

    public TestEntity build() {
      return new TestEntity(this);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class TestNestedEntity {
    @JsonViewProperty("Значение строка")
    private String stringValue;
    @JsonViewProperty("Логическое")
    private Boolean booleanValue;
    @JsonViewProperty("Целочисленное (32 бит)")
    private Integer integerValue;

    public TestNestedEntity() {
    }

    public TestNestedEntity(String stringValue, Boolean booleanValue, Integer integerValue) {
      this.stringValue = stringValue;
      this.booleanValue = booleanValue;
      this.integerValue = integerValue;
    }

    public static TestNestedEntity of(String stringValue, Boolean booleanValue, Integer integerValue) {
      return new TestNestedEntity(stringValue, booleanValue, integerValue);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestNestedEntity that = (TestNestedEntity) o;
      return Objects.equals(stringValue, that.stringValue) && Objects.equals(booleanValue, that.booleanValue) && Objects.equals(integerValue, that.integerValue);
    }

    @Override
    public int hashCode() {
      return Objects.hash(stringValue, booleanValue, integerValue);
    }

    public String getStringValue() {
      return stringValue;
    }

    public void setStringValue(String stringValue) {
      this.stringValue = stringValue;
    }

    public Boolean getBooleanValue() {
      return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
      this.booleanValue = booleanValue;
    }

    public Integer getIntegerValue() {
      return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
      this.integerValue = integerValue;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class TestSimpleItem {
    private String value;

    public TestSimpleItem() {
    }

    public TestSimpleItem(String value) {
      this.value = value;
    }

    public static TestSimpleItem of(String value) {
      return new TestSimpleItem(value);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestSimpleItem that = (TestSimpleItem) o;
      return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    @JsonViewValue
    public String toString() {
      return value;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class TestObjectItem {
    @JsonViewProperty("Код")
    private Integer code;
    @JsonViewProperty("Наименование")
    private String name;
    @JsonViewProperty("Вложенный объект (TestObjectItem)")
    private TestNestedEntity nestedEntity;

    public TestObjectItem() {
    }

    public TestObjectItem(Integer code, String name, TestNestedEntity nestedEntity) {
      this.code = code;
      this.name = name;
      this.nestedEntity = nestedEntity;
    }

    public static TestObjectItem of(Integer code, String name, TestNestedEntity nestedEntity) {
      return new TestObjectItem(code, name, nestedEntity);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestObjectItem that = (TestObjectItem) o;
      return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(nestedEntity, that.nestedEntity);
    }

    @Override
    public int hashCode() {
      return Objects.hash(code, name, nestedEntity);
    }

    public Integer getCode() {
      return code;
    }

    public void setCode(Integer code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public TestNestedEntity getNestedEntity() {
      return nestedEntity;
    }

    public void setNestedEntity(TestNestedEntity nestedEntity) {
      this.nestedEntity = nestedEntity;
    }
  }

  public static final class CardNumberValueHolder implements Comparable<CardNumberValueHolder> {
    private final String cardNumber;

    private CardNumberValueHolder(String cardNumber) {
      this.cardNumber = cardNumber;
    }

    public static CardNumberValueHolder of(String cardNumber) {
      return new CardNumberValueHolder(cardNumber);
    }

    public String getCardNumber() {
      return cardNumber;
    }

    @Override
    public int compareTo(@NotNull CardNumberValueHolder o) {
      if (cardNumber == null) {
        return -1;
      }
      if (o == null || o.cardNumber == null) {
        return 1;
      }
      return cardNumber.compareTo(o.cardNumber);
    }

    @JsonViewValue
    public String getMaskedCardNumber() {
      if (cardNumber == null || cardNumber.length() < 4) {
        return cardNumber;
      }
      StringBuilder maskedCard = new StringBuilder();
      for (int i = 0; i < cardNumber.length() - 4; i++) {
        maskedCard.append('*');
      }
      maskedCard.append(cardNumber.substring(cardNumber.length() - 4));
      return maskedCard.toString();
    }
  }
}