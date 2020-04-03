package mp.jprime.query.json;

import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.filters.And;
import mp.jprime.json.beans.*;
import mp.jprime.json.services.QueryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class QueryTest {
  private static final JsonQuery query;
  /**
   * Заполнение Select на основе JSON-Query
   */
  @Autowired
  private QueryService queryService;

  @Configuration
  @ComponentScan("mp.jprime.json.services")
  public static class Config {
  }

  static {
    query = new JsonQuery();
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
            new JsonExpr().or(Arrays.asList(new JsonExpr(JsonCond.newAttrCond("attr5").between(new JsonBetweenCond("1", "10"))),
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

    JsonQuery newQuery = queryService.getQuery(json);

    Assert.assertNotNull(newQuery);
    Assert.assertNotNull(newQuery.getAttrs());
    Assert.assertNotNull(newQuery.getFilter());
    Assert.assertNotNull(newQuery.getFilter().getAnd());
    Assert.assertNotNull(newQuery.getOrders());
    Assert.assertEquals(4, newQuery.getAttrs().size());
    Assert.assertEquals(4, newQuery.getLinkAttrs().size());
    Assert.assertEquals(7, newQuery.getFilter().getAnd().size());
    Assert.assertEquals(2, newQuery.getOrders().size());
  }

  @Test
  public void testJpSelectCreator() {
    JPSelect jpSelect = queryService.getSelect("test", query, null).build();

    Assert.assertNotNull(jpSelect);

    Assert.assertNotNull(jpSelect);
    Assert.assertNotNull(jpSelect.getAttrs());
    Assert.assertNotNull(jpSelect.getWhere());
    Assert.assertNotNull(((And) jpSelect.getWhere()).getFilters());
    Assert.assertNotNull(jpSelect.getOrderBy());
    Assert.assertEquals(4, jpSelect.getAttrs().size());
    Assert.assertEquals(7, ((And) jpSelect.getWhere()).getFilters().size());
    Assert.assertEquals(2, jpSelect.getOrderBy().size());
  }
}
