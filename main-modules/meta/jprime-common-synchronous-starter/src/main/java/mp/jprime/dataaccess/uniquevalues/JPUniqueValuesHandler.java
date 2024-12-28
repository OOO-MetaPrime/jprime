package mp.jprime.dataaccess.uniquevalues;

import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;

import java.util.Collection;
import java.util.List;

/**
 * Логика получения значений по умолчанию
 */
public interface JPUniqueValuesHandler {
  /**
   * Получение уникальных значений атрибутов объектов
   */
  Collection<JPUniqueValue> getUniqueValues(JPSelect select, List<String> hierarchy);
}
