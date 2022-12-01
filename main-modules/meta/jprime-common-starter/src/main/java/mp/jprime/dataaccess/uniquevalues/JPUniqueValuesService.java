package mp.jprime.dataaccess.uniquevalues;

import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * Получение уникальных значиний атрибутов объектов
 */
public interface JPUniqueValuesService {
  /**
   * Получение уникальных значиний атрибутов объектов
   */
  Flux<JPUniqueValue> getUniqueValues(JPSelect select, Collection<String> hierarchy);
}
