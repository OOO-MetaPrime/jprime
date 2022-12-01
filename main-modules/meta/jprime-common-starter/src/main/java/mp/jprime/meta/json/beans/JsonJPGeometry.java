package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPGeometry {
  /**
   * ID of the Spatial Reference System
   */
  private int SRID;

  public int getSRID() {
    return SRID;
  }

  public JsonJPGeometry() {

  }

  private JsonJPGeometry(int SRID) {
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

    public JsonJPGeometry build() {
      return new JsonJPGeometry(SRID);
    }
  }
}
