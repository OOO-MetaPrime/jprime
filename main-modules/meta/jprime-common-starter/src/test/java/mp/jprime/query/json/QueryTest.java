package mp.jprime.query.json;

import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.filters.And;
import mp.jprime.json.beans.*;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = QueryTest.Config.class)
public class QueryTest {
  private static final JsonSelect query;
  /**
   * Заполнение Select на основе JSON-Query
   */
  @Autowired
  private QueryService queryService;

  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.json.modules", "mp.jprime.json.services"},
      excludeFilters = {
          @ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE,
              value = {
                  JsonJPObjectService.class
              }
          )
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  static {
    query = new JsonSelect();
    query.setLimit(10);
    query.setOffset(0);
    query.setAttrs(Stream.of("attr1", "attr2", "attr3", "attr4").collect(Collectors.toList()));
    query.setLinkAttrs(Stream.of("attr1", "attr2", "attr3", "attr4")
        .collect(HashMap::new, (m, v) -> m.put(v, ""), HashMap::putAll));
    query.setFilter(new JsonExpr()
        .and(Arrays.asList(
            new JsonExpr(JsonCond.newFeatureCond("filter1").checkDay(LocalDate.of(2019, 1, 1))),
            new JsonExpr(JsonCond.newAttrCond("attr1").eq("1")),
            new JsonExpr(JsonCond.newAttrCond("attr2").isNull(Boolean.TRUE)),
            new JsonExpr().or(Arrays.asList(new JsonExpr(JsonCond.newAttrCond("attr3").like("xxx")),
                new JsonExpr(JsonCond.newAttrCond("attr5").gt("2")))
            ),
            new JsonExpr().or(Arrays.asList(new JsonExpr(JsonCond.newAttrCond("attr5").between(new JsonBetween("1", "10"))),
                new JsonExpr(JsonCond.newAttrCond("attr6").in(Arrays.asList("1", "2", "3"))))
            ),
            new JsonExpr(JsonCond.newAttrCond("attr7").eqDay((LocalDate.of(2019, 1, 1)))),
            new JsonExpr(JsonCond.newAttrCond("attr8").gtYear(2008))
        ))
    );
    query.setOrders(Arrays.asList(new JsonOrder().asc("attr1"), new JsonOrder().desc("attr2")));

  }

  @Test
  public void testJsonConvertQuery() {
    String json = queryService.toString(query);

    JsonSelect newQuery = queryService.getQuery(json);

    assertNotNull(newQuery);
    assertNotNull(newQuery.getAttrs());
    assertNotNull(newQuery.getFilter());
    assertNotNull(newQuery.getFilter().getAnd());
    assertNotNull(newQuery.getOrders());
    assertEquals(4, newQuery.getAttrs().size());
    assertEquals(4, newQuery.getLinkAttrs().size());
    assertEquals(7, newQuery.getFilter().getAnd().size());
    assertEquals(2, newQuery.getOrders().size());
  }

  @Test
  public void testJpSelectCreator() {
    JPSelect jpSelect = queryService.getSelect("test", query, null).build();

    assertNotNull(jpSelect);

    assertNotNull(jpSelect);
    assertNotNull(jpSelect.getAttrs());
    assertNotNull(jpSelect.getWhere());
    assertNotNull(((And) jpSelect.getWhere()).getFilters());
    assertNotNull(jpSelect.getOrderBy());
    assertEquals(4, jpSelect.getAttrs().size());
    assertEquals(7, ((And) jpSelect.getWhere()).getFilters().size());
    assertEquals(2, jpSelect.getOrderBy().size());
  }
}
