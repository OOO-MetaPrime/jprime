package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Модель запроса
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonSelect {
  @JsonProperty
  private Integer limit;
  @JsonProperty
  private Integer offset;
  @JsonProperty
  private boolean totalCount;
  @JsonProperty
  private Collection<String> attrs;
  @JsonProperty
  private Map<String, String> linkAttrs = new HashMap<>();
  @JsonProperty
  private JsonExpr filter;
  @JsonProperty
  private List<JsonOrder> orders = new ArrayList<>();

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getOffset() {
    return offset;
  }

  public boolean isTotalCount() {
    return totalCount;
  }

  public void setTotalCount(boolean totalCount) {
    this.totalCount = totalCount;
  }

  public Collection<String> getAttrs() {
    return attrs;
  }

  public void setAttrs(Collection<String> attrs) {
    this.attrs = attrs;
  }

  public Map<String, String> getLinkAttrs() {
    return linkAttrs;
  }

  public void setLinkAttrs(Map<String, String> linkAttrs) {
    this.linkAttrs = linkAttrs;
  }

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }

  public JsonExpr getFilter() {
    return filter;
  }

  public void setOrders(List<JsonOrder> orders) {
    this.orders = orders;
  }

  public List<JsonOrder> getOrders() {
    return orders;
  }
}
