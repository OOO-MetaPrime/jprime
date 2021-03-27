package mp.jprime.utils.testutils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UniIn extends BaseJPUtilInParams {
  private String request;
  private String objectClassCode;

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public String getObjectClassCode() {
    return objectClassCode;
  }

  public void setObjectClassCode(String objectClassCode) {
    this.objectClassCode = objectClassCode;
  }
}
