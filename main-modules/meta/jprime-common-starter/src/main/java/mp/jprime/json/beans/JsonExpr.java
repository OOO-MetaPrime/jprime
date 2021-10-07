package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonExpr {
  private JsonCond cond;
  private Collection<JsonExpr> or;
  private Collection<JsonExpr> and;

  public JsonExpr() {

  }

  public JsonExpr(JsonCond cond) {
    this.cond = cond;
  }

  public JsonCond getCond() {
    return cond;
  }

  public Collection<JsonExpr> getOr() {
    return or;
  }

  public Collection<JsonExpr> getAnd() {
    return and;
  }

  public JsonExpr or(Collection<JsonExpr> or) {
    this.or = or;
    return this;
  }

  public JsonExpr and(Collection<JsonExpr> and) {
    this.and = and;
    return this;
  }
}
