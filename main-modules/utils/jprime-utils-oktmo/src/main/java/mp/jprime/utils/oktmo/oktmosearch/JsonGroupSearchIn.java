package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonGroupSearchIn extends BaseJPUtilInParams {
  private String query;
  private Integer limit;
  private boolean prefixMode;
  private Collection<String> oktmoSearch;
  private boolean authSearch;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public boolean isPrefixMode() {
    return prefixMode;
  }

  public void setPrefixMode(boolean prefixMode) {
    this.prefixMode = prefixMode;
  }

  public Collection<String> getOktmoSearch() {
    return oktmoSearch;
  }

  public void setOktmoSearch(Collection<String> oktmoSearch) {
    this.oktmoSearch = oktmoSearch;
  }

  public boolean isAuthSearch() {
    return authSearch;
  }

  public void setAuthSearch(boolean authSearch) {
    this.authSearch = authSearch;
  }
}
