package mp.jprime.meta;

/**
 * Метаописание хранения пространственных данных
 */
public interface JPGeometry {
  /**
   * Возвращает ID of the Spatial Reference System
   */
  default int getSRID() {
    return 0;
  }
}
