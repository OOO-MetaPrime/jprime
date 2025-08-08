package mp.jprime.utils;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Набор функций обработки строк
 */
public abstract class JPStringUtils {
  private static final String CODE_REGEXP = "^[a-zA-Z0-9\\.\\-\\_]+$";
  private static final Pattern PATTERN_CODE_REGEXP = Pattern.compile(CODE_REGEXP);
  private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d+$");
  private static final int MIN_LENGTH_CARD_NUMBER_COMMON = 8;
  private static final int MAX_LENGTH_CARD_NUMBER_COMMON = 19;

  /**
   * Проверка на корректность строкового кода
   * Разрешены:
   * - латинские буквы
   * - цифры
   * - символы "-", "_", "."
   *
   * @param code Строковый код
   * @return Да/нет
   */
  public static boolean isCurrentCode(String code) {
    return code != null && PATTERN_CODE_REGEXP.matcher(code).matches();
  }

  /**
   * Применяет шаблон форматирования строки.
   * Заменяет следующие шаблоны данными из текущей даты и времени
   * {@link #applyDataTimeTemplate(String, LocalDateTime)}
   *
   * @param templateString шаблон
   * @return обработанная строка
   */
  public static String applyDataTimeTemplate(String templateString) {
    return applyDataTimeTemplate(templateString, LocalDateTime.now());
  }

  /**
   * Применяет шаблон форматирования строки.
   * Заменяет следующие шаблоны данными из даты и времени:
   * #year# - год
   * #month# - месяц
   * #day# - день
   * #week# - номер недели в году
   * #weekOfMonth# - номер недели в месяце
   * #hour# - час
   * #minute# - минута
   * Дополняет числа менее 10 нулем слева
   *
   * @param templateString шаблон
   * @param dt             дата и время
   * @return обработанная строка
   */
  public static String applyDataTimeTemplate(String templateString, LocalDateTime dt) {
    if (templateString == null) {
      return null;
    }
    String delim = "#";
    String[] tokens = StringUtils.substringsBetween(templateString, delim, delim);
    if (tokens == null || tokens.length == 0) {
      return templateString;
    }
    String str = templateString;
    for (String token : tokens) {
      switch (token) {
        case "year":
          str = str.replaceAll("#year#", String.valueOf(dt.getYear()));
          break;
        case "month":
          str = str.replaceAll("#month#", StringUtils.leftPad(String.valueOf(dt.getMonth().getValue()), 2, '0'));
          break;
        case "day":
          str = str.replaceAll("#day#", StringUtils.leftPad(String.valueOf(dt.getDayOfMonth()), 2, '0'));
          break;
        case "week":
          str = str.replaceAll("#week#", StringUtils.leftPad(String.valueOf(dt.get(WeekFields.of(Locale.getDefault()).weekOfYear())), 2, '0'));
          break;
        case "weekOfMonth":
          str = str.replaceAll("#weekOfMonth#", StringUtils.leftPad(String.valueOf(dt.get(WeekFields.of(Locale.getDefault()).weekOfMonth())), 2, '0'));
          break;
        case "hour":
          str = str.replaceAll("#hour#", StringUtils.leftPad(String.valueOf(dt.getHour()), 2, '0'));
          break;
        case "minute":
          str = str.replaceAll("#minute#", StringUtils.leftPad(String.valueOf(dt.getMinute()), 2, '0'));
          break;
      }
    }
    return str;
  }

  public static boolean isValidBankCardNumber(String cardNumber, @Nonnull PaymentSystem ps) {
    return isValidBankCardNumber(cardNumber, ps.getMinLength(), ps.getMaxLength());
  }

  public static boolean isValidBankCardNumber(String cardNumber) {
    return isValidBankCardNumber(cardNumber, MIN_LENGTH_CARD_NUMBER_COMMON, MAX_LENGTH_CARD_NUMBER_COMMON);
  }

  private static boolean isValidBankCardNumber(String cardNumber, int minLength, int maxLength) {
    if (StringUtils.isBlank(cardNumber)) {
      return false;
    }
    String value = cardNumber.replaceAll("\\s", "");
    Matcher matcher = DIGIT_PATTERN.matcher(value);
    boolean isFind = matcher.find();
    int length = value.length();
    if (isFind && length >= minLength && length <= maxLength) {
      // Алгоритм Luna для проверки номера банковских карт
      int result = 0;
      boolean nextSecond = false;
      for (int i = value.length() - 1; i >= 0 ; i--) {
        int d = Character.getNumericValue(value.charAt(i));
        if (nextSecond) {
          d *= 2;
          if (d >= 10) {
            d -= 9;
          }
        }
        nextSecond = !nextSecond;
        result += d;
      }
      return result % 10 == 0;
    }
    return false;
  }

  public static Collection<String> collect(Collection<String>... values) {
    if (values == null) {
      return null;
    }
    Collection<String> result = new LinkedHashSet<>();
    for (Collection<String> value : values) {
      if (value == null) {
        continue;
      }
      result.addAll(value);
    }
    return result.isEmpty() ? null : result;
  }
}
