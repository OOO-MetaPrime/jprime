package mp.jprime.system.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Кеш className - Class.
 * Экономия на обращении к ФС
 */
@Service
public class JavaClassCache {
  private ConcurrentMap<String, Class<?>> classForNames = new ConcurrentHashMap<>();

  /**
   * Возвращает класс по имени
   *
   * @param className Класс
   * @return Класс
   */
  public Class<?> getClass(String className) {
    Class<?> cls = classForNames.get(className);
    if (cls == null) {
      try {
        cls = Class.forName(className);
      } catch (Throwable e) {
        cls = Class.class;
      }
      classForNames.putIfAbsent(className, cls);
      cls = classForNames.get(className);
    }
    return !cls.equals(Class.class) ? cls : null;
  }
}
