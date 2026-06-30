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
   * ОКТМО
   */
  public interface Code {
    /**
     * Муниципальные образования субъектов Российской Федерации
     */
    String CODE_00000000 = "00000000";
    /**
     * Муниципальные районы Республики Крым
     */
    String CODE_35000000 = "35000000";
    /**
     * Алушта
     */
    String CODE_35503000 = "35503000";
    /**
     * Ялта
     */
    String CODE_35529000 = "35529000";
    /**
     * Белогорский муниципальный район
     */
    String CODE_35607000 = "35607000";
    /**
     * Джанкойский муниципальный район
     */
    String CODE_35611000 = "35611000";
    /**
     * Симферополь
     */
    String CODE_35701000 = "35701000";
    /**
     * Джанкой
     */
    String CODE_35709000 = "35709000";
    /**
     * Евпатория
     */
    String CODE_35712000 = "35712000";
    /**
     * Муниципальные образования города федерального значения Санкт-Петербурга
     */
    String CODE_40000000 = "40000000";
  }

  public static final Collection<String> ROOT_HIERARCHY = List.of(Code.CODE_00000000);

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
      char i6 = oktmo.charAt(5);

      result = new LinkedHashSet<>(4);
      result.add(oktmo);
      result.add(String.valueOf(new char[]{i1, i2, i3, i4, i5, i6, '0', '0'}));
      result.add(String.valueOf(new char[]{i1, i2, i3, i4, i5, '0', '0', '0'}));
      result.add(String.valueOf(new char[]{i1, i2, i3, '0', '0', '0', '0', '0'}));
      result.add(String.valueOf(new char[]{i1, i2, '0', '0', '0', '0', '0', '0'}));
      result.add(Code.CODE_00000000);

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
   * 75 700 000 -> 75 7
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
    if (i7 == '0' && i8 == '0') {
      if (i6 == '0') {
        if (i5 == '0') {
          if (i4 == '0') {
            return String.valueOf(new char[]{i1, i2, i3});
          } else {
            return String.valueOf(new char[]{i1, i2, i3, i4});
          }
        }
        return String.valueOf(new char[]{i1, i2, i3, i4, i5});
      } else {
        return String.valueOf(new char[]{i1, i2, i3, i4, i5, i6});
      }
    }
    return oktmo;
  }

  /**
   * Возвращает список префиксов кодов ОКТМО
   * 75 738 000 -> 75 738
   * 75 700 000 -> 75 7
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

  /**
   * Возвращает префиксы для проверки ОКТМО
   * 75 738 123 ->
   * - 75 738 123
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   * 75 738 000 ->
   * - 75 738
   * - 75 738 000
   * - 75 000 000
   * - 00 000 000
   *
   * @param oktmo список исходных ОКТМО
   * @return Список всех доступных ОКТМО и префиксов
   */
  public static Collection<String> getOktmoTreeList(Collection<String> oktmo) {
    Collection<String> result = new HashSet<>();
    result.addAll(Oktmo.getPrefix(oktmo));
    result.addAll(Oktmo.getHierarchy(oktmo));
    return result;
  }
}
