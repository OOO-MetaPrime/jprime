package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonGroupGetIn extends BaseJPUtilInParams {
  private Collection<String> group;
  private boolean prefixMode;

  public Collection<String> getGroup() {
    return group;
  }

  public void setGroup(Collection<String> group) {
    this.group = group;
  }

  public boolean isPrefixMode() {
    return prefixMode;
  }

  public void setPrefixMode(boolean prefixMode) {
    this.prefixMode = prefixMode;
  }
}
