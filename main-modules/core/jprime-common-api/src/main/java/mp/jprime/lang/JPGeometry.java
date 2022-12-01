package mp.jprime.lang;

/**
 * Интерфейс типа "Геометрия". Реализация в отдельных модулях
 */
public interface JPGeometry extends Comparable {
  /**
   * Возвращает ID of the Spatial Reference System
   */
  int getSRID();
}
