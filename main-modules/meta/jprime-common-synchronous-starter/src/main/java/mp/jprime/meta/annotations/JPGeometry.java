package mp.jprime.meta.annotations;

/**
 * Описание пространственных данных
 */
public @interface JPGeometry {
  /**
   * Возвращает ID of the Spatial Reference System
   */
  int SRID() default 0;
}
