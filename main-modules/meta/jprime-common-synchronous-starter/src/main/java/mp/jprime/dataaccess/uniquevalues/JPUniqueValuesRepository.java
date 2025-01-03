package mp.jprime.dataaccess.uniquevalues;

import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс получения уникальных данных объекта
 */
public interface JPUniqueValuesRepository {
  /**
   * Получение уникальных значений атрибутов объектов
   */
  Collection<JPUniqueValue> getUniqueValues(JPSelect select, List<String> hierarchy);
}
