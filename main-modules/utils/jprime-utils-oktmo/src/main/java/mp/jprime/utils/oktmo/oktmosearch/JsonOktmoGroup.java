package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.utils.oktmo.JpOktmoUtilsService;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonOktmoGroup {
  private String code;
  private String name;
  private Collection<String> oktmo;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<String> getOktmo() {
    return oktmo;
  }

  public void setOktmo(Collection<String> oktmo) {
    this.oktmo = oktmo;
  }

  public static JsonOktmoGroup of(JpOktmoUtilsService.Group group) {
    JsonOktmoGroup result = new JsonOktmoGroup();
    result.setCode(group.getCode());
    result.setName(group.getName());
    result.setOktmo(group.getOktmo());
    return result;
  }
}
