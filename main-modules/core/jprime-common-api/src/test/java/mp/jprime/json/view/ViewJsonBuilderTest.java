package mp.jprime.json.view;

import mp.jprime.json.modules.JPObjectMapperJavaTimeExpander;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.parsers.ValueParser;
import mp.jprime.parsers.base.*;
import mp.jprime.parsers.services.ParserCommonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = {
    JPJsonMapper.class,
    JPObjectMapperJavaTimeExpander.class
})
@Tag("manualTests")
public class ViewJsonBuilderTest {

  @Test
  public void builderTest() {
    TestResponse testResponse = TestResponse.newBuilder()
        .stringValue("Значение строка")
        .booleanValue(true)
        .integerValue(123)
        .longValue(123321L)
        .doubleValue(123.456d)
        .dateValue(new Date())
        .localDateValue(LocalDate.now())
        .localDateTimeValue(LocalDateTime.now())
        .localTimeValue(LocalTime.now())
        .simpleItems(Arrays.asList(
            TestSimpleItem.of("Простой элемент #1"),
            TestSimpleItem.of("Простой элемент #2"),
            TestSimpleItem.of("Простой элемент #3")
        ))
        .objectItems(Arrays.asList(
            TestObjectItem.of(1, "Составной элемент #1"),
            TestObjectItem.of(2, "Составной элемент #2"),
            TestObjectItem.of(3, "Составной элемент #3")
        ))
        .build();

    String json = testResponse.toJson();

    /*
    Files.write(
        Paths.get("c:\\dev\\tmp\\JsonViewBuilderTest.json"),
        json.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
    );
    */

    Assertions.assertNotNull(json);
  }

  public static class TestResponse {
    private String stringValue;
    private Boolean booleanValue;
    private Integer integerValue;
    private Long longValue;
    private Double doubleValue;
    private Date dateValue;
    private LocalDate localDateValue;
    private LocalDateTime localDateTimeValue;
    private LocalTime localTimeValue;
    private Collection<TestSimpleItem> simpleItems;
    private Collection<TestObjectItem> objectItems;

    private TestResponse(Builder builder) {
      stringValue = builder.stringValue;
      booleanValue = builder.booleanValue;
      integerValue = builder.integerValue;
      longValue = builder.longValue;
      doubleValue = builder.doubleValue;
      dateValue = builder.dateValue;
      localDateValue = builder.localDateValue;
      localDateTimeValue = builder.localDateTimeValue;
      localTimeValue = builder.localTimeValue;
      simpleItems = builder.simpleItems;
      objectItems = builder.objectItems;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public String toJson() {
      return JsonViewBuilder.newBuilder()
          .append("Строка", stringValue)
          .append("Логическое", booleanValue)
          .append("Целочисленное (32 бит)", integerValue)
          .append("Целочисленное (64 бит)", longValue)
          .append("Вещественное", doubleValue)
          .append("Дата", dateValue)
          .append("Дата", localDateValue)
          .append("Дата и время", localDateTimeValue)
          .append("Время", localTimeValue)
          .appendCollection("Коллекция простых элементов", "Простой элемент", simpleItems)
          .appendCollection("Коллекция состовных элементов", "Составной элемент", objectItems)
          .toString();
    }

    public String getStringValue() {
      return stringValue;
    }

    public Boolean getBooleanValue() {
      return booleanValue;
    }

    public Integer getIntegerValue() {
      return integerValue;
    }

    public Long getLongValue() {
      return longValue;
    }

    public Double getDoubleValue() {
      return doubleValue;
    }

    public Date getDateValue() {
      return dateValue;
    }

    public LocalDate getLocalDateValue() {
      return localDateValue;
    }

    public LocalDateTime getLocalDateTimeValue() {
      return localDateTimeValue;
    }

    public LocalTime getLocalTimeValue() {
      return localTimeValue;
    }

    public Collection<TestSimpleItem> getSimpleItems() {
      return simpleItems;
    }

    public Collection<TestObjectItem> getObjectItems() {
      return objectItems;
    }


    public static final class Builder {
      private String stringValue;
      private Boolean booleanValue;
      private Integer integerValue;
      private Long longValue;
      private Double doubleValue;
      private Date dateValue;
      private LocalDate localDateValue;
      private LocalDateTime localDateTimeValue;
      private LocalTime localTimeValue;
      private Collection<TestSimpleItem> simpleItems;
      private Collection<TestObjectItem> objectItems;

      private Builder() {
      }

      public Builder stringValue(String stringValue) {
        this.stringValue = stringValue;
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

      public Builder dateValue(Date dateValue) {
        this.dateValue = dateValue;
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

      public Builder simpleItems(Collection<TestSimpleItem> simpleItems) {
        this.simpleItems = simpleItems;
        return this;
      }

      public Builder objectItems(Collection<TestObjectItem> objectItems) {
        this.objectItems = objectItems;
        return this;
      }

      public TestResponse build() {
        return new TestResponse(this);
      }
    }
  }

  public static class TestSimpleItem {
    private String value;

    public TestSimpleItem(String value) {
      this.value = value;
    }

    public static TestSimpleItem of(String value) {
      return new TestSimpleItem(value);
    }

    @Override
    public String toString() {
      return value;
    }

    public String getValue() {
      return value;
    }
  }

  public static class TestObjectItem implements JsonViewAware {
    private Integer code;
    private String name;

    public TestObjectItem(Integer code, String name) {
      this.code = code;
      this.name = name;
    }

    public static TestObjectItem of(Integer code, String name) {
      return new TestObjectItem(code, name);
    }

    @Override
    public void toJson(JsonViewBuilder jvb) {
      jvb.append("Код", code).appendString("Наименование", name, 1024);
    }

    public Integer getCode() {
      return code;
    }

    public String getName() {
      return name;
    }
  }
}
