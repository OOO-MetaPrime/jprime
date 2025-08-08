package mp.jprime.utils;

/**
 * Валидация СНИЛС
 */
public class SnilsValidator {

  /**
   * Метод проверяет контрольную сумму СНИЛС
   *
   * @param snils СНИЛС
   */
  public static boolean validate(String snils) {
    if (snils == null || snils.isEmpty()) {
      return false;
    }
    snils = clearSnils(snils);
    if (snils.trim().isEmpty()) {
      return false;
    }
    if (checkNumber(snils)) {
      int contrSumma;
      int summa = 0;
      try {
        contrSumma = Integer.parseInt(snils.substring(snils.length() - 2));
      } catch (Exception e) {
        return false;
      }
      int m = 9;
      int chast;
      for (int i = 0; i < (snils.length() - 2); i++) {
        try {
          chast = Integer.parseInt("" + snils.charAt(i));
        } catch (Exception e) {
          return false;
        }
        if (chast == 0) {
          m--;
          continue;
        }
        summa += m * chast;
        m--;
      }
      summa = summa % 101;
        /*остаток от деления на 101 находится в диапазоне от 0 до 100, в правиле расчета снилса
          необходимо брать последние две цифры остатака, поэтому берем дополнительный остаток от деления на 100*/
      return summa % 100 == contrSumma && m == 0;
    } else {
      return false;
    }
  }

  /**
   * Выодит только цифры снилса без пробелов и -
   *
   * @param snils снилс
   * @return снилс без пробелов и тире
   */
  public static String clearSnils(String snils) {
    return snils == null ? null : snils.replaceAll("[^0-9]", "");
  }

  /**
   * Номер снилса больше 001-001-998?
   *
   * @param snils исходный снилс
   * @return true - если номер снилса больше 001-001-998, false - в противном случае
   */
  private static boolean checkNumber(final String snils) {
    String clear = parseSnilsNumber(snils);
    return clear != null && Integer.parseInt(clear) > 1001998;
  }

  /**
   * Возвращает номер СНИЛСа (без контрольной суммы)
   * Например, "068-619-801-04" -> "068619801".
   *
   * @param snils исходный снилс
   * @return снилс без контрольной суммы
   */
  private static String parseSnilsNumber(final String snils) {
    String clear = clearSnils(snils);
    return clear != null && clear.length() > 9 ? clear.substring(0, 9) : null;
  }

  /**
   * Форматирует
   *
   * @param snils СНИЛС
   * @return СНИЛС в формате XXXXXXXXX XX (так хранится в БД ЕАИС)
   */
  public String convert(String snils) {
    String clear = clearSnils(snils);
    return clear.substring(0, 9) + " " + clear.substring(9, 11);
  }
}
