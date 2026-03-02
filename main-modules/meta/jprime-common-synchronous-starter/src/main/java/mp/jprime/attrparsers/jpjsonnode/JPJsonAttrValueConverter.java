package mp.jprime.attrparsers.jpjsonnode;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.lang.JPJsonNode;

/**
 * Конвертация json-значений атрибута
 */
public interface JPJsonAttrValueConverter {
  /**
   * Конвертирует данные из формата хранения в формат представления
   *
   * @param jpo   Объект в отношении которого происходит трансформация
   * @param value Данные в формате хранения
   * @return Данные в формате представления
   */
  default JPJsonNode toJsonView(JPObject jpo, JPJsonNode value) {
    return value;
  }

  /**
   * Конвертирует данные из формата представления в формат хранения
   *
   * @param jpo   Объект в отношении которого происходит трансформация
   * @param value Данные в формате представления
   * @return Данные в формате хранения
   */
  default JPJsonNode fromJsonView(JPObject jpo, JPJsonNode value) {
    return value;
  }
}
