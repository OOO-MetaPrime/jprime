package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonGetIn extends BaseJPUtilInParams {
  private Collection<String> oktmo;

  public Collection<String> getOktmo() {
    return oktmo;
  }

  public void setOktmo(Collection<String> oktmo) {
    this.oktmo = oktmo;
  }
}
