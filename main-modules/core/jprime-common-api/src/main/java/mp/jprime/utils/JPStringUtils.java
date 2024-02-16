package mp.jprime.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Набор функций обработки строк
 */
public abstract class JPStringUtils {
  private static final String CODE_REGEXP = "^[a-zA-Z0-9\\.\\-\\_]+$";
  private static final Pattern PATTERN_CODE_REGEXP = Pattern.compile(CODE_REGEXP);

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
}
