package mp.jprime.utils.oktmo.oktmosearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.utils.JPUtilCustomOutParams;
import mp.jprime.utils.oktmo.JpOktmoUtilsService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonGroupOut extends JPUtilCustomOutParams {
  private Map<String, JsonOktmoGroup> groups;

  private JsonGroupOut() {
    super(null, null, false);
  }

  public static JsonGroupOut of(Collection<JpOktmoUtilsService.Group> list) {
    JsonGroupOut result = new JsonGroupOut();

    Map<String, JsonOktmoGroup> groups = new HashMap<>();
    if (list != null) {
      for (JpOktmoUtilsService.Group group : list) {
        groups.put(group.getCode(), JsonOktmoGroup.of(group));
      }
    }
    result.setGroups(groups);
    return result;
  }

  public Map<String, JsonOktmoGroup> getGroups() {
    return groups;
  }

  public void setGroups(Map<String, JsonOktmoGroup> groups) {
    this.groups = groups;
  }
}
