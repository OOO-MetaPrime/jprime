package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Параметры для чека утилит
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JPUtilCheckInParams extends BaseJPUtilInParams {

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String rootObjectClassCode;
    private String rootObjectId;
    private String objectClassCode;
    private Collection<String> objectIds = new ArrayList<>();


    private Builder() {
      super();
    }

    public Builder rootObjectClassCode(String rootObjectClassCode) {
      this.rootObjectClassCode = rootObjectClassCode;
      return this;
    }

    public Builder rootObjectId(String rootObjectId) {
      this.rootObjectId = rootObjectId;
      return this;
    }

    public Builder objectClassCode(String objectClassCode) {
      this.objectClassCode = objectClassCode;
      return this;
    }

    public Builder objectIds(Collection<String> objectIds) {
      if (objectIds != null) {
        this.objectIds.addAll(objectIds);
      }
      return this;
    }

    public Builder objectId(String objectId) {
      if (objectId != null) {
        this.objectIds.add(objectId);
      }
      return this;
    }

    public JPUtilCheckInParams build() {
      JPUtilCheckInParams params = new JPUtilCheckInParams();
      params.setRootObjectClassCode(rootObjectClassCode);
      params.setRootObjectId(rootObjectId);
      params.setObjectClassCode(objectClassCode);
      params.setObjectIds(objectIds);
      return params;
    }
  }
}
