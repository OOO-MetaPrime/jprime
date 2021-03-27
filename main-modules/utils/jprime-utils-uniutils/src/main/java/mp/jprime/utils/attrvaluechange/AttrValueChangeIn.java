package mp.jprime.utils.attrvaluechange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttrValueChangeIn extends BaseJPUtilInParams {

  private Collection<JsonAttrCodeValue> attrs;

  public Collection<JsonAttrCodeValue> getAttrs() {
    return attrs;
  }

  public void setAttrs(Collection<JsonAttrCodeValue> attrs) {
    this.attrs = attrs;
  }
}
