package mp.jprime.json.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPJsonMapperTest.Config.class)
public class JPJsonMapperTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers", "mp.jprime.json.modules", "mp.jprime.json.services"}
  )
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private JPJsonMapper jpJsonMapper;

  @Test
  void validTrimString() {
    String json = """
        {
          "surname": " Иванов \n",
          "name": " Иван "
        }""";

    Map<String, Object> out = jpJsonMapper.toMap(json);
    assertNotNull(out);
    assertEquals(2, out.size());
    assertEquals("Иванов", out.get("surname"));
    assertEquals("Иван", out.get("name"));
  }
}
