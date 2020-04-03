package mp.jprime.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Фильтр классов, отмеченных аннотацией JPClassesLink
 */
public interface JPClassesLinkFilter<T> {
  /**
   * Убирает лишние хендлера
   * Если на метаописание настроены два разных хендлера, один из которых является наследником другого, то для обработки будет использоваться наследник
   *
   * @param handlers Список хендлеров
   * @return Очищенный список
   */
  default Collection<T> filter(Collection<T> handlers) {
    Collection<T> result = new ArrayList<>();
    for (T handler : handlers) {
      Collection<T> temp = new ArrayList<>();
      boolean isNew = true;
      for (T r : result) {
        if (handler.getClass().isAssignableFrom(r.getClass())) {  // Присутствует наследник
          isNew = false;
          temp.add(r);
        } else if (!r.getClass().isAssignableFrom(handler.getClass())) { // Классы не связаны
          temp.add(r);
        }
      }
      if (isNew) {
        temp.add(handler);
      }
      result = temp;
    }
    return result;
  }
}
