package mp.jprime.json.versioning;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.json.versioning.beans.JsonV1Bean;
import mp.jprime.json.versioning.beans.JsonV2Bean;
import mp.jprime.json.versioning.beans.JsonV3Bean;
import mp.jprime.lang.JPJsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPJsonVersionTest.Config.class)
public class JPJsonVersionTest {

  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers", "mp.jprime.json.modules", "mp.jprime.json.services", "mp.jprime.json.versioning"}
  )
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private JPJsonMapper jsonMapper;
  @Autowired
  private JPJsonVersionService jsonVersionService;

  @Test
  void testJsonToV1Bean() {
    String json = "{\"version\": 1, \"data\": {\"field1\": \"value1\"}}";

    JPJsonNode jsonNode = jsonMapper.toJPJsonNode(json);
    Object out = jsonVersionService.toObject("test", jsonNode);
    assertInstanceOf(JsonV1Bean.class, out);
    assertEquals("value1", ((JsonV1Bean) out).getField1());
  }

  @Test
  void testJsonToV2Bean() {
    String json = "{\"version\": 2, \"data\": {\"field2\": \"value2\"}}";

    JPJsonNode jsonNode = jsonMapper.toJPJsonNode(json);
    Object out = jsonVersionService.toObject("test", jsonNode);
    assertInstanceOf(JsonV2Bean.class, out);
    assertEquals("value2", ((JsonV2Bean) out).getField2());
  }

  @Test
  void testJsonToV3Bean() {
    String json = "{\"version\": 3, \"data\": {\"field3\": \"value3\"}}";

    JPJsonNode jsonNode = jsonMapper.toJPJsonNode(json);
    Object out = jsonVersionService.toObject("test", jsonNode);
    assertInstanceOf(JsonV3Bean.class, out);
    assertEquals("value3", ((JsonV3Bean) out).getField3());
  }

  @Test
  void testV1ToV13Bean() {
    String json = "{\"version\": 1, \"data\": {\"field1\": \"value1\"}}";

    JPJsonNode jsonNode = jsonMapper.toJPJsonNode(json);
    Object out = jsonVersionService.toLatestObject("test", jsonNode);
    assertInstanceOf(JsonV3Bean.class, out);
    assertEquals("value1", ((JsonV3Bean) out).getField3());
  }
}
