package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Доступ к объектам текущим пользователем
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObjectAccessList {
  /**
   * Список настроек доступа к объектам текущим пользователем
   */
  private Collection<JsonJPObjectAccess> accessList = new ArrayList<>();

  public Collection<JsonJPObjectAccess> getAccessList() {
    return accessList;
  }

  public void setAccessList(Collection<JsonJPObjectAccess> accessList) {
    this.accessList = accessList;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final Collection<JsonJPObjectAccess> accessList = new ArrayList<>();

    private Builder() {
    }

    public Builder withAccessList(Collection<JsonJPObjectAccess> accessList) {
      this.accessList.addAll(accessList);
      return this;
    }

    public JsonJPObjectAccessList build() {
      JsonJPObjectAccessList jsonJPObjectAccessList = new JsonJPObjectAccessList();
      jsonJPObjectAccessList.setAccessList(accessList);
      return jsonJPObjectAccessList;
    }
  }
}
