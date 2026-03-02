package mp.jprime.utils.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.json.beans.JsonParam;

import java.util.Collection;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWizard {
  private Mode prev;
  private Mode next;

  public Mode getPrev() {
    return prev;
  }

  public void setPrev(Mode prev) {
    this.prev = prev;
  }

  public Mode getNext() {
    return next;
  }

  public void setNext(Mode next) {
    this.next = next;
  }

  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Mode {
    private String utilCode;
    private String modeCode;
    private Collection<JsonParam> inParams;
    private Collection<String> hiddenParams;
    private Map<String, Object> defValues;

    public String getUtilCode() {
      return utilCode;
    }

    public void setUtilCode(String utilCode) {
      this.utilCode = utilCode;
    }

    public String getModeCode() {
      return modeCode;
    }

    public void setModeCode(String modeCode) {
      this.modeCode = modeCode;
    }

    public Collection<JsonParam> getInParams() {
      return inParams;
    }

    public void setInParams(Collection<JsonParam> inParams) {
      this.inParams = inParams;
    }

    public Collection<String> getHiddenParams() {
      return hiddenParams;
    }

    public void setHiddenParams(Collection<String> hiddenParams) {
      this.hiddenParams = hiddenParams;
    }

    public Map<String, Object> getDefValues() {
      return defValues;
    }

    public void setDefValues(Map<String, Object> defValues) {
      this.defValues = defValues;
    }
  }
}
