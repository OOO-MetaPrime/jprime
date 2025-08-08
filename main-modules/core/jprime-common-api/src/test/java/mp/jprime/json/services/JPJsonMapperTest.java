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

import java.util.LinkedHashMap;
import java.util.List;
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
  void testToString() {
    Map<String, String> data = new LinkedHashMap<>() {{
      put("k1", "v1");
      put("k2", "v2");
      put("k3", "v3");
    }};

    String out = jpJsonMapper.toString(data);
    assertNotNull(out);
    assertEquals("""
        {"k1":"v1","k2":"v2","k3":"v3"}""", out);
  }

  @Test
  void testToStringWithIgnore() {
    Map<String, String> data = new LinkedHashMap<>() {{
      put("k1", "v1");
      put("k2", "v2");
      put("k3", "v3");
    }};

    String out = jpJsonMapper.toString(data, List.of("k1", "k2"));
    assertNotNull(out);
    assertEquals("""
        {"k3":"v3"}""", out);

    record Record(String k1, String k2, String k3) {
    }

    String recordOut = jpJsonMapper.toString(new Record("v1", "v2", "v3"), List.of("k1", "k2"));
    assertNotNull(recordOut);
    assertEquals("""
        {"k3":"v3"}""", recordOut);
  }
}
