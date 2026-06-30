package mp.jprime.utils;

import java.util.Collection;
import java.util.Collections;

/**
 * Утилитный класс для работы с коллекциями.
 */
public final class JPCollectionUtils {
  private static final JPCollectionUtils INSTANCE = new JPCollectionUtils();

  private JPCollectionUtils() {
  }

  public static JPCollectionUtils getInstance() {
    return INSTANCE;
  }

  /**
   * Проверяет, является ли переданный объект коллекцией.
   *
   * @param object объект для проверки
   * @return {@code true}, если объект является {@link Collection}, иначе {@code false}
   */
  public static boolean isCollection(Object object) {
    return object instanceof Collection;
  }

  /**
   * Преобразует объект в коллекцию.
   * <ul>
   *   <li>Если объект уже является коллекцией — возвращается он же.</li>
   *   <li>Если нет — создаётся коллекция из одного элемента.</li>
   * </ul>
   *
   * @param object объект для преобразования
   * @return коллекция, содержащая объект
   */
  public static Collection<?> asCollection(Object object) {
    return isCollection(object) ? (Collection<?>) object : Collections.singleton(object);
  }

  /**
   * Проверяет, является ли коллекция пустой.
   *
   * @param collection коллекция для проверки
   * @return {@code true}, если коллекция {@code null} или пустая
   */
  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Проверяет, содержит ли коллекция элементы.
   *
   * @param collection коллекция для проверки
   * @return {@code true}, если коллекция не {@code null} и не пустая
   */
  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmpty(collection);
  }

  /**
   * Возвращает размер коллекции.
   *
   * @param collection коллекция
   * @return количество элементов или {@code 0}, если коллекция {@code null}
   */
  public static long size(Collection<?> collection) {
    return collection == null ? 0 : collection.size();
  }

  /**
   * Возвращает первый элемент коллекции.
   *
   * @param collection коллекция
   * @param <T>        тип элементов
   * @return первый элемент или {@code null}, если коллекция {@code null} или пустая
   */
  public static <T> T first(Collection<T> collection) {
    return collection != null && !collection.isEmpty() ? collection.iterator().next() : null;
  }
}
