package mp.jprime.utils.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.lang.JPJsonNode;

import java.util.Collection;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonCompConf {
  private String rootObjectClassCode;
  private String rootObjectId;
  private String objectClassCode;
  private Collection<String> objectIds;

  private JPJsonNode value;
  private UUID compConfCode;
  private String compConfButtonText;
  private String saveUtil;
  private String saveMode;
  private String field;

  public String getRootObjectClassCode() {
    return rootObjectClassCode;
  }

  public void setRootObjectClassCode(String rootObjectClassCode) {
    this.rootObjectClassCode = rootObjectClassCode;
  }

  public String getRootObjectId() {
    return rootObjectId;
  }

  public void setRootObjectId(String rootObjectId) {
    this.rootObjectId = rootObjectId;
  }

  public String getObjectClassCode() {
    return objectClassCode;
  }

  public void setObjectClassCode(String objectClassCode) {
    this.objectClassCode = objectClassCode;
  }

  public Collection<String> getObjectIds() {
    return objectIds;
  }

  public void setObjectIds(Collection<String> objectIds) {
    this.objectIds = objectIds;
  }

  public JPJsonNode getValue() {
    return value;
  }

  public void setValue(JPJsonNode value) {
    this.value = value;
  }

  public UUID getCompConfCode() {
    return compConfCode;
  }

  public void setCompConfCode(UUID compConfCode) {
    this.compConfCode = compConfCode;
  }

  public String getCompConfButtonText() {
    return compConfButtonText;
  }

  public void setCompConfButtonText(String compConfButtonText) {
    this.compConfButtonText = compConfButtonText;
  }

  public String getSaveUtil() {
    return saveUtil;
  }

  public void setSaveUtil(String saveUtil) {
    this.saveUtil = saveUtil;
  }

  public String getSaveMode() {
    return saveMode;
  }

  public void setSaveMode(String saveMode) {
    this.saveMode = saveMode;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }
}
