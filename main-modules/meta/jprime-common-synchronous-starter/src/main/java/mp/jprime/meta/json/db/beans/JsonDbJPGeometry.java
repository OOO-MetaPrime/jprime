package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonDbJPGeometry {
  /**
   * ID of the Spatial Reference System
   */
  private int SRID;

  public int getSRID() {
    return SRID;
  }

  public void setSRID(int SRID) {
    this.SRID = SRID;
  }

  public JsonDbJPGeometry() {

  }

  private JsonDbJPGeometry(int SRID) {
    this.SRID = SRID;
  }

  /**
   * Построитель JsonJPGeometry
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPGeometry
   */
  public static final class Builder {
    private int SRID;

    private Builder() {

    }

    public Builder srid(int SRID) {
      this.SRID = SRID;
      return this;
    }

    public JsonDbJPGeometry build() {
      return new JsonDbJPGeometry(SRID);
    }
  }
}
