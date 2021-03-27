package mp.jprime.meta;

/**
 * Фильтр меты
 */
public interface JPMetaFilter {
  /**
   * Признак фильтрации
   *
   * @param jpClass Метаописание
   * @return Да/Нет
   */
  boolean filter(JPClass jpClass);
}
