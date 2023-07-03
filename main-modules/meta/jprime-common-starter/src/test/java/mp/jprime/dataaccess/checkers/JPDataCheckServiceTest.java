package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.Filter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPDataCheckServiceTest.Config.class)
class JPDataCheckServiceTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {
          "mp.jprime.dataaccess.checkers",
          "mp.jprime.dataaccess.params.query",
          "mp.jprime.dataaccess.templatevalues",
          "mp.jprime.parsers"
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private JPDataCheckService jpDataCheckServiceTest;

  @Test
  void testEmptyDataWithEmptyFilter() {
    Object out = jpDataCheckServiceTest.check(Filter.and(Collections.emptyList()), JPMutableData.empty());
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testSimpleEqData() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.or(
            Filter.attr("attr1").eq(1),
            Filter.attr("attr1").eq("2")
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testSimpleEqDataFailure() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.or(
            Filter.attr("attr1").eq(3),
            Filter.attr("attr1").eq(4)
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.FALSE, out);
  }


  @Test
  void testSimpleNeqData() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.and(
            Filter.attr("attr1").neq(4),
            Filter.attr("attr2").neq(5)
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testSimpleInData() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.and(
            Filter.attr("attr1").in(Collections.singletonList("1")),
            Filter.attr("attr2").in(Collections.singletonList(2))
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testSimpleNotInData() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.and(
            Filter.attr("attr1").notIn(Collections.singletonList("10")),
            Filter.attr("attr2").notIn(Collections.singletonList("10"))
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testLT() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.and(
            Filter.attr("attr1").lt(2)
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testLTE() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.attr("attr1").lte(1),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testGT() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.attr("attr1").gt(0),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testGTE() {
    Map<String, Object> data = new HashMap<>() {{
      put("attr1", 1);
      put("attr2", 2);
    }};
    Object out = jpDataCheckServiceTest.check(
        Filter.or(
            Filter.attr("attr1").gte(1),
            Filter.attr("attr2").gte(1)
        ),
        JPMutableData.of(data)
    );
    assertEquals(Boolean.TRUE, out);
  }
}
