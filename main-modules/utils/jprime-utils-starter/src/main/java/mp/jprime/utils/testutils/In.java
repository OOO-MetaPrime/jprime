package mp.jprime.utils.testutils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class In extends BaseJPUtilInParams {
  private String request;

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }
}
