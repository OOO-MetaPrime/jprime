package mp.jprime.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Коды ОКТМО
 */
public final class Oktmo {
  private static final Map<String, Collection<String>> HIERARCHY = new ConcurrentHashMap<>();

  private Oktmo() {

  }

  /**
   * Муниципальные образования субъектов Российской Федерации
   */
  public static final String CODE_00000000 = "00000000";

  private static final Collection<String> ROOT_HIERARCHY = List.of(CODE_00000000);

  /**
   * Возвращает список иерархии кодов ОКТМО
   * 75 738 123 ->
   * - 75 738 123
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   *
   * @return Иерархия кодов ОКТМО
   */
  public static Collection<String> getHierarchy(String oktmo) {
    if (oktmo == null || oktmo.length() < 8) {
      return ROOT_HIERARCHY;
    }
    Collection<String> result = HIERARCHY.get(oktmo);
    if (result == null) {
      char i1 = oktmo.charAt(0);
      char i2 = oktmo.charAt(1);
      char i3 = oktmo.charAt(2);
      char i4 = oktmo.charAt(3);
      char i5 = oktmo.charAt(4);

      result = new LinkedHashSet<>(4);
      result.add(oktmo);
      result.add(String.valueOf(new char[]{i1, i2, i3, i4, i5, '0', '0', '0'}));
      result.add(String.valueOf(new char[]{i1, i2, '0', '0', '0', '0', '0', '0'}));
      result.add(CODE_00000000);

      HIERARCHY.put(oktmo, result);
    }
    return result;
  }

  /**
   * Возвращает список иерархии кодов ОКТМО
   * 75 738 123 ->
   * - 75 738 123
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   *
   * @return Иерархия кодов ОКТМО
   */
  public static Collection<String> getHierarchy(Collection<String> oktmoList) {
    if (oktmoList == null || oktmoList.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<String> result = new HashSet<>();
    for (String oktmo : oktmoList) {
      result.addAll(getHierarchy(oktmo));
    }
    return result;
  }

  /**
   * Возвращает префикс кода ОКТМО
   * 75 738 000 -> 75 738
   * 75 000 000 -> 75
   *
   * @return Префикс кода ОКТМО
   */
  public static String getPrefix(String oktmo) {
    if (oktmo == null) {
      return null;
    }
    if (oktmo.length() < 8) {
      return null;
    }
    char i1 = oktmo.charAt(0);
    char i2 = oktmo.charAt(1);
    char i3 = oktmo.charAt(2);
    char i4 = oktmo.charAt(3);
    char i5 = oktmo.charAt(4);
    char i6 = oktmo.charAt(5);
    char i7 = oktmo.charAt(6);
    char i8 = oktmo.charAt(7);

    if (i1 == '0' && i2 == '0') {
      return "";
    }
    if (i3 == '0' && i4 == '0' && i5 == '0') {
      return String.valueOf(new char[]{i1, i2});
    }
    if (i6 == '0' && i7 == '0' && i8 == '0') {
      return String.valueOf(new char[]{i1, i2, i3, i4, i5});
    }
    return oktmo;
  }

  /**
   * Возвращает список префиксов кодов ОКТМО
   * 75 738 000 -> 75 738
   * 75 000 000 -> 75
   *
   * @return Иерархия кодов ОКТМО
   */
  public static Collection<String> getPrefix(Collection<String> oktmoList) {
    if (oktmoList == null || oktmoList.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<String> result = new HashSet<>();
    for (String oktmo : oktmoList) {
      String prefix = getPrefix(oktmo);
      if (prefix != null) {
        result.add(prefix);
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  /**
   * Проверка соответствия ОКТМО переданному списку префиксов
   *
   * @param oktmo           ОКТМО
   * @param oktmoPrefixList Список префиксов
   * @return Да/Нет
   */
  public static boolean isCheck(String oktmo, Collection<String> oktmoPrefixList) {
    if (oktmoPrefixList == null || oktmoPrefixList.isEmpty()) {
      return false;
    }
    return oktmoPrefixList.stream().anyMatch(oktmo::startsWith);
  }
}
