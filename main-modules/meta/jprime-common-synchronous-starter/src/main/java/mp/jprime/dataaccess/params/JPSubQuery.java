package mp.jprime.dataaccess.params;

import mp.jprime.dataaccess.params.query.Filter;

/**
 * Возвращает описание подзапроса
 */
public interface JPSubQuery {
  /**
   * Атрибут
   *
   * @return Атрибут
   */
  String getAttr();

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String getClassCode();

  /**
   * Ограничение
   *
   * @return Ограничение
   */
  Filter getFilter();

  static JPSubQuery of(String attr, String classCode, Filter filter) {
    return new JPSubQuery() {
      @Override
      public String getAttr() {
        return attr;
      }

      @Override
      public String getClassCode() {
        return classCode;
      }

      @Override
      public Filter getFilter() {
        return filter;
      }
    };
  }
}
