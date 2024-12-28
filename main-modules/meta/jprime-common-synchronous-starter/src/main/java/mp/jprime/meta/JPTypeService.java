package mp.jprime.meta;

import mp.jprime.meta.beans.JPType;

import java.util.Collection;

/**
 * Логика по работе с типами данных
 */
public interface JPTypeService {
  /**
   * Возвращает список типов, на которые можно менять
   *
   * @param type Тип атрибута
   * @return Список типов, на которые можно изменить указанный
   */
  Collection<JPType> getAvailableChanges(JPType type);
}
