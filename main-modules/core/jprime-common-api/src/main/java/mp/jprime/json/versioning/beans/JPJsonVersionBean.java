package mp.jprime.json.versioning.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.json.versioning.JPJsonVersion;
import mp.jprime.lang.JPJsonNode;

/**
 * Данные в формате указанной версии
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class JPJsonVersionBean implements JPJsonVersion {
  /**
   * Номер версии
   */
  private Integer version;
  /**
   * Данные в формате версии
   */
  private JPJsonNode data;

  public JPJsonVersionBean() {
  }

  private JPJsonVersionBean(Integer version, JPJsonNode data) {
    this.version = version;
    this.data = data;
  }


  public static JPJsonVersionBean of(Integer version, JPJsonNode data) {
    return new JPJsonVersionBean(version, data);
  }

  @Override
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public JPJsonNode getData() {
    return data;
  }

  public void setData(JPJsonNode data) {
    this.data = data;
  }
}
