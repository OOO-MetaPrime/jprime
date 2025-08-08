package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.utils.annotations.JPUtilResultType;
import mp.jprime.utils.json.beans.JsonCompConf;

import java.util.UUID;

/**
 * Тип результата - Отображение конфигуратора компонент
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpCompConf")
public final class JPUtilJPCompConfOutParams extends BaseJPUtilOutParams<JsonCompConf> {
  private final JsonCompConf result;

  private JPUtilJPCompConfOutParams(JPUtilInParams inParams,
                                    String description, String qName, boolean changeData, boolean deleteData,
                                    JPJsonNode value, UUID compConfCode, String compConfButtonText,
                                    String saveUtil, String saveMode, String field) {
    super(description, qName, changeData, deleteData);

    JsonCompConf compConf = new JsonCompConf();
    if (inParams != null) {
      compConf.setRootObjectClassCode(inParams.getRootObjectClassCode());
      compConf.setRootObjectId(inParams.getRootObjectId());
      compConf.setObjectClassCode(inParams.getObjectClassCode());
      compConf.setObjectIds(inParams.getObjectIds());
    }
    compConf.setValue(value);
    compConf.setCompConfCode(compConfCode);
    compConf.setCompConfButtonText(compConfButtonText);
    compConf.setSaveUtil(saveUtil);
    compConf.setSaveMode(saveMode);
    compConf.setField(field);
    this.result = compConf;
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public JsonCompConf getResult() {
    return result;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private JPUtilInParams inParams;
    private JPJsonNode value;
    private UUID compConfCode;
    private String compConfButtonText;
    private String saveUtil;
    private String saveMode;
    private String field;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPCompConfOutParams build() {
      return new JPUtilJPCompConfOutParams(
          inParams,
          description, qName, changeData, deleteData,
          value, compConfCode, compConfButtonText, saveUtil, saveMode, field
      );
    }

    public Builder create(JPUtilInParams inParams, JPJsonNode value, UUID compConfCode) {
      this.inParams = inParams;
      this.value = value;
      this.compConfCode = compConfCode;
      return this;
    }

    public Builder create(JPUtilInParams inParams, JPJsonNode value, UUID compConfCode,
                          String saveUtil, String saveMode, String field) {
      this.inParams = inParams;
      this.value = value;
      this.compConfCode = compConfCode;
      this.saveUtil = saveUtil;
      this.saveMode = saveMode;
      this.field = field;
      return this;
    }

    public Builder create(JPUtilInParams inParams, JPJsonNode value, UUID compConfCode, String compConfButtonText,
                          String saveUtil, String saveMode, String field) {
      this.inParams = inParams;
      this.value = value;
      this.compConfCode = compConfCode;
      this.compConfButtonText = compConfButtonText;
      this.saveUtil = saveUtil;
      this.saveMode = saveMode;
      this.field = field;
      return this;
    }
  }
}
