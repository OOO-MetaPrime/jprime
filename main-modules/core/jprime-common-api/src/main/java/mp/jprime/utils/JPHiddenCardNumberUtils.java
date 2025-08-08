package mp.jprime.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Утилитный класс для базовой работы с номерами банковских карт
 */
public final class JPHiddenCardNumberUtils {
  private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
  private static final Pattern HIDDEN_PATTERN = Pattern.compile("[*]+\\d{4}");

  private JPHiddenCardNumberUtils() {

  }

  /**
   * Очищаем номер карты от символов
   *
   * @param cardNumber Номер карты
   * @return Очищенный номер карты
   */
  public static String clearCardNumber(String cardNumber) {
    if (cardNumber == null) {
      return null;
    }
    return cardNumber.replaceAll("\\s", "");
  }

  /**
   * Признак скрытого номера карты
   *
   * @param cardNumber Номер карты
   * @return Да/Нет
   */
  public static boolean isHiddenCardNumber(String cardNumber) {
    if (StringUtils.isEmpty(cardNumber)) {
      return false;
    }
    String str = clearCardNumber(cardNumber);
    if (str.length() > 4) {
      return HIDDEN_PATTERN.matcher(str).matches();
    }
    return false;
  }

  public static String getHiddenCardNumber(String cardNumber) {
    if (StringUtils.isEmpty(cardNumber) || isHiddenCardNumber(cardNumber)) {
      return cardNumber;
    }

    String str = clearCardNumber(cardNumber);
    int length = str.length();
    int length4 = length - 4;
    if (length4 <= 0) {
      return str;
    }
    return StringUtils.rightPad("", length - 4, "*") + str.substring(length4, length);
  }
}
