package mp.jprime.json.view;

import mp.jprime.json.modules.JPObjectMapperJavaTimeExpander;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.ParserServiceAwareConfiguration;
import mp.jprime.parsers.ValueParser;
import mp.jprime.parsers.base.*;
import mp.jprime.parsers.services.ParserCommonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@SpringBootTest
@ContextConfiguration(classes = {
    JPJsonMapper.class,
    JPObjectMapperJavaTimeExpander.class,
    ParserCommonService.class,
    ParserServiceAwareConfiguration.class,
    ValueParser.class,
    StringToStringParser.class,
    StringToIntegerParser.class,
    IntegerToIntegerParser.class,
    StringToLongParser.class,
    LongToLongParser.class,
    LongToIntegerParser.class,
    StringToDoubleParser.class,
    JPJsonNodeToStringParser.class,
    JPJsonNodeToJPJsonStringParser.class,
    StringToCardNumberHolderParser.class
})
@Tag("manualTests")
public class ViewJsonTransformationTest {

  private JPJsonMapper jsonMapper;

  @Autowired
  public void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  private static final TestEntity TEST_ENTITY = TestEntity.newBuilder()
      .stringValue("Значение строка")
      .cardNumber("1234 4321 1234 4321")
      .booleanValue(true)
      .integerValue(123)
      .longValue(123321L)
      .doubleValue(123.456d)
      .localDateValue(LocalDate.now())
      .localDateTimeValue(LocalDateTime.now())
      .localTimeValue(LocalTime.now())
      .nestedEntity(TestEntity.TestNestedEntity.of("nested", true, 12345))
      .collectionItems(Arrays.asList(
          "Элемент #1",
          "Элемент #2",
          "Элемент #3"
      ))
      .simpleItems(Arrays.asList(
          TestEntity.TestSimpleItem.of("Простой элемент #1"),
          TestEntity.TestSimpleItem.of("Простой элемент #2"),
          TestEntity.TestSimpleItem.of("Простой элемент #3")
      ))
      .objectItems(Arrays.asList(
          TestEntity.TestObjectItem.of(1, "Составной элемент #1",
              TestEntity.TestNestedEntity.of("str1", true, 123)
          ),
          TestEntity.TestObjectItem.of(2, "Составной элемент #2",
              TestEntity.TestNestedEntity.of("str2", false, 321)),
          TestEntity.TestObjectItem.of(3, "Составной элемент #3", null)
      ))
      .build();

  @Test
  public void transform() throws IOException {

    JPJsonNode viewJson = PlainToViewJsonTransformer.transformToViewJson(TEST_ENTITY);

    // System.out.println(ValueParser.parseTo(String.class, viewJson));

    JPJsonString plainJson = ViewToPlainJsonTransformer.toPlainJson(
        ValueParser.parseTo(JPJsonString.class, viewJson)
    );

    // System.out.println(plainJson);

    TestEntity testEntity = jsonMapper.toObject(TestEntity.class, plainJson.toString());

    Assertions.assertNotNull(testEntity);
    Assertions.assertEquals(testEntity, TEST_ENTITY);
  }
}
