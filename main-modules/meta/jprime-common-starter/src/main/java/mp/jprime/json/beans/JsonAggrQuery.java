package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель запроса агрегации
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAggrQuery {
  @JsonProperty
  private List<JsonAggregate> aggrs = new ArrayList<>();
  @JsonProperty
  private JsonExpr filter;

  public JsonExpr getFilter() {
    return filter;
  }

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }

  public List<JsonAggregate> getAggrs() {
    return aggrs;
  }

  public void setAggrs(List<JsonAggregate> aggrs) {
    this.aggrs = aggrs;
  }
}
