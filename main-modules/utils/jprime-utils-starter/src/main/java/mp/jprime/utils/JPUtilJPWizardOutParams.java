package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.json.beans.JsonParam;
import mp.jprime.utils.annotations.JPUtilResultType;
import mp.jprime.utils.json.beans.JsonWizard;

import java.util.Collection;
import java.util.Map;

/**
 * Тип результата - Wizard
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpWizard")
public final class JPUtilJPWizardOutParams extends BaseJPUtilOutParams<JsonWizard> {
  private final JsonWizard result;

  private JPUtilJPWizardOutParams(String description, String qName, boolean changeData, boolean deleteData,
                                  String prevUtil, String prevMode, Collection<JsonParam> prevParams,
                                  Collection<String> prevHiddenParams, Map<String, Object> prevValues,
                                  String nextUtil, String nextMode, Collection<JsonParam> nextParams,
                                  Collection<String> nextHiddenParams, Map<String, Object> nextValues) {
    super(description, qName, changeData, deleteData);

    JsonWizard.Mode prev = new JsonWizard.Mode();
    prev.setUtilCode(prevUtil);
    prev.setModeCode(prevMode);
    prev.setInParams(prevParams);
    prev.setHiddenParams(prevHiddenParams);
    prev.setDefValues(prevValues);

    JsonWizard.Mode next = new JsonWizard.Mode();
    next.setUtilCode(nextUtil);
    next.setModeCode(nextMode);
    next.setInParams(nextParams);
    next.setHiddenParams(nextHiddenParams);
    next.setDefValues(nextValues);

    JsonWizard wizard = new JsonWizard();
    wizard.setPrev(prev);
    wizard.setNext(next);
    this.result = wizard;
  }

  @Override
  public JsonWizard getResult() {
    return result;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private String prevUtil;
    private String prevMode;
    private Collection<JsonParam> prevParams;
    private Collection<String> prevHiddenParams;
    private Map<String, Object> prevValues;

    private String nextUtil;
    private String nextMode;
    private Collection<JsonParam> nextParams;
    private Collection<String> nextHiddenParams;
    private Map<String, Object> nextValues;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPWizardOutParams build() {
      return new JPUtilJPWizardOutParams(
          description, qName, changeData, deleteData,
          prevUtil, prevMode, prevParams, prevHiddenParams, prevValues,
          nextUtil, nextMode, nextParams, nextHiddenParams, nextValues
      );
    }

    public Builder params(String prevUtil, String prevMode, Collection<JsonParam> prevParams,
                          Collection<String> prevHiddenParams, Map<String, Object> prevValues,
                          String nextUtil, String nextMode, Collection<JsonParam> nextParams,
                          Collection<String> nextHiddenParams, Map<String, Object> nextValues) {
      this.prevUtil = prevUtil;
      this.prevMode = prevMode;
      this.prevParams = prevParams;
      this.prevHiddenParams = prevHiddenParams;
      this.prevValues = prevValues;

      this.nextUtil = nextUtil;
      this.nextMode = nextMode;
      this.nextParams = nextParams;
      this.nextHiddenParams = nextHiddenParams;
      this.nextValues = nextValues;
      return this;
    }
  }
}
