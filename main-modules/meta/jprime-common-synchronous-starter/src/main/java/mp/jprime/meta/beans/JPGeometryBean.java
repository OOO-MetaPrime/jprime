package mp.jprime.meta.beans;

import mp.jprime.meta.JPGeometry;

/**
 * Бин JPGeometry
 */
public class JPGeometryBean implements JPGeometry {
  /**
   * ID of the Spatial Reference System
   */
  private final int SRID;

  private JPGeometryBean(int SRID) {
    this.SRID = SRID;
  }

  /**
   * Возвращает ID of the Spatial Reference System
   *
   * @return ID of the Spatial Reference System
   */
  @Override
  public int getSRID() {
    return SRID;
  }

  /**
   * Построитель JPGeometryBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPGeometryBean
   */
  public static final class Builder {
    /**
     * ID of the Spatial Reference System
     */
    private int SRID;

    private Builder() {
    }

    /**
     * Создаем JPGeometryBean
     *
     * @return JPGeometryBean
     */
    public JPGeometryBean build() {
      return new JPGeometryBean(SRID);
    }

    /**
     * ID of the Spatial Reference System
     *
     * @param SRID ID of the Spatial Reference System
     * @return Builder
     */
    public Builder srid(int SRID) {
      this.SRID = SRID;
      return this;
    }
  }
}
