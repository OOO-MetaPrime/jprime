package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.JPUtilCustomOutParams;
import mp.jprime.utils.oktmo.JpOktmoUtilsService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonSearchOut extends JPUtilCustomOutParams {
  private Map<String, String> oktmo;

  private JsonSearchOut() {
    super(null, null, false);
  }

  public static JsonSearchOut of(Collection<JpOktmoUtilsService.Oktmo> list) {
    JsonSearchOut result = new JsonSearchOut();

    if (list != null) {
      Map<String, String> oktmo = new LinkedHashMap<>(list.size());
      for (JpOktmoUtilsService.Oktmo obj : list) {
        oktmo.put(obj.getCode(), obj.getName());
      }
      result.setOktmo(oktmo);
    }
    return result;
  }

  public Map<String, String> getOktmo() {
    return oktmo;
  }

  public void setOktmo(Map<String, String> oktmo) {
    this.oktmo = oktmo;
  }
}
