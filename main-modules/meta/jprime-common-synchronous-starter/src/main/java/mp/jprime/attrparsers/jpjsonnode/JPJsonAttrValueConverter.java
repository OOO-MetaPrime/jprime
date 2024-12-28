package mp.jprime.attrparsers.jpjsonnode;

import mp.jprime.lang.JPJsonNode;

/**
 * Конвертация json-значений атрибута
 */
public interface JPJsonAttrValueConverter {
  /**
   * Конвертирует данные из формата хранения в формат представления
   *
   * @param value Данные в формате хранения
   * @return Данные в формате представления
   */
  default JPJsonNode toJsonView(JPJsonNode value) {
    return value;
  }

  /**
   * Конвертирует данные из формата представления в формат хранения
   *
   * @param value Данные в формате представления
   * @return Данные в формате хранения
   */
  default JPJsonNode fromJsonView(JPJsonNode value) {
    return value;
  }
}
