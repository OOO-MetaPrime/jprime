package mp.jprime.utils;

import jakarta.annotation.Nonnull;
import mp.jprime.parsers.ValueParser;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
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

  private static final Map<Character, String> TRANSLIT_MAP;

  static {
    // ГОСТ 7.79-2000
    Map<Character, String> tm = new HashMap<>();
    tm.put('а', "a");
    tm.put('б', "b");
    tm.put('в', "v");
    tm.put('г', "g");
    tm.put('д', "d");
    tm.put('е', "e");
    tm.put('ё', "yo");
    tm.put('ж', "zh");
    tm.put('з', "z");
    tm.put('и', "i");
    tm.put('й', "y");
    tm.put('к', "k");
    tm.put('л', "l");
    tm.put('м', "m");
    tm.put('н', "n");
    tm.put('о', "o");
    tm.put('п', "p");
    tm.put('р', "r");
    tm.put('с', "s");
    tm.put('т', "t");
    tm.put('у', "u");
    tm.put('ф', "f");
    tm.put('х', "kh");
    tm.put('ц', "ts");
    tm.put('ч', "ch");
    tm.put('ш', "sh");
    tm.put('щ', "shch");
    tm.put('ъ', "");
    tm.put('ы', "y");
    tm.put('ь', "");
    tm.put('э', "e");
    tm.put('ю', "yu");
    tm.put('я', "ya");

    tm.put('А', "A");
    tm.put('Б', "B");
    tm.put('В', "V");
    tm.put('Г', "G");
    tm.put('Д', "D");
    tm.put('Е', "E");
    tm.put('Ё', "Yo");
    tm.put('Ж', "Zh");
    tm.put('З', "Z");
    tm.put('И', "I");
    tm.put('Й', "Y");
    tm.put('К', "K");
    tm.put('Л', "L");
    tm.put('М', "M");
    tm.put('Н', "N");
    tm.put('О', "O");
    tm.put('П', "P");
    tm.put('Р', "R");
    tm.put('С', "S");
    tm.put('Т', "T");
    tm.put('У', "U");
    tm.put('Ф', "F");
    tm.put('Х', "Kh");
    tm.put('Ц', "Ts");
    tm.put('Ч', "Ch");
    tm.put('Ш', "Sh");
    tm.put('Щ', "Shch");
    tm.put('Ъ', "");
    tm.put('Ы', "Y");
    tm.put('Ь', "");
    tm.put('Э', "E");
    tm.put('Ю', "Yu");
    tm.put('Я', "Ya");

    TRANSLIT_MAP = Collections.unmodifiableMap(tm);
  }

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

  /**
   * Проверяет контрольную сумму номера банковской карты
   *
   * @param cardNumber номер банковской карты
   * @param ps         платежная система
   * @return признак валидности
   */
  public static boolean isValidBankCardNumber(String cardNumber, @Nonnull PaymentSystem ps) {
    return isValidBankCardNumber(cardNumber, ps.getMinLength(), ps.getMaxLength());
  }

  /**
   * Проверяет контрольную сумму номера банковской карты
   *
   * @param cardNumber номер банковской карты
   * @return признак валидности
   */
  public static boolean isValidBankCardNumber(String cardNumber) {
    return isValidBankCardNumber(cardNumber, MIN_LENGTH_CARD_NUMBER_COMMON, MAX_LENGTH_CARD_NUMBER_COMMON);
  }

  /**
   * Транслитерирует русский текст в английский алфавит по стандарту ГОСТ 7.79-2000
   *
   * @param text русский текст
   * @return текст в английском алфавите
   */
  public static String transliterate(String text) {
    if (text == null || text.isEmpty()) {
      return text;
    }
    StringBuilder result = new StringBuilder();
    for (char c : text.toCharArray()) {
      result.append(TRANSLIT_MAP.getOrDefault(c, String.valueOf(c)));
    }
    return result.toString();
  }

  /**
   * Заменяет в строке с фильтром значения параметров вида {PARAMS.param1}
   *
   * @param sFilter   Строка фильтра
   * @param params    Список параметров для замены
   * @param valueFunc Функция для параметров
   * @return Строка с добавленными значениями
   */
  public static String replaceParamValues(String sFilter, Collection<String> params, Function<String, Object> valueFunc) {
    if (params == null || params.isEmpty()) {
      return sFilter;
    }
    for (String param : params) {
      String template = "{PARAMS." + param + "}";
      if (sFilter.contains(template)) {
        String sValue = ValueParser.parseTo(String.class, valueFunc.apply(param));
        sFilter = StringUtils.replace(sFilter, template, sValue != null ? sValue : "");
      }
    }
    return sFilter;
  }

  /**
   * Заменяет в строке с фильтром значения параметров вида {ATTR_VALUE.attr1}
   *
   * @param sFilter   Строка фильтра
   * @param attrs     Список атрибутов для замены
   * @param valueFunc Функция для параметров
   * @return Строка с добавленными значениями
   */
  public static String replaceAttrValues(String sFilter, Collection<String> attrs, Function<String, Object> valueFunc) {
    if (attrs == null || attrs.isEmpty()) {
      return sFilter;
    }
    for (String attr : attrs) {
      String template = "{ATTR_VALUE." + attr + "}";
      if (sFilter.contains(template)) {
        String sValue = ValueParser.parseTo(String.class, valueFunc.apply(attr));
        sFilter = StringUtils.replace(sFilter, template, sValue != null ? sValue : "");
      }
    }
    return sFilter;
  }

  /**
   * Объединяет коллекции строк
   *
   * @param values коллекции строк
   * @return коллекция строк
   */
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
      for (int i = value.length() - 1; i >= 0; i--) {
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

  /**
   * Экранировать спец символы для XML строк
   *
   * @param value XML строка
   * @return XML строка с экранированными спец символами
   */
  public static String escapeXml(String value) {
    if (value == null) {
      return null;
    }
    int len = value.length();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      char c = value.charAt(i);
      switch (c) {
        case '&' -> sb.append("&amp;");
        case '<' -> sb.append("&lt;");
        case '>' -> sb.append("&gt;");
        case '"' -> sb.append("&quot;");
        case '\'' -> sb.append("&apos;");
        default -> sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Экранировать спец символы для SQL строк
   *
   * @param value SQL строка
   * @return SQL строка с экранированными спец символами
   */
  public static String escapeSql(String value) {
    if (value == null) {
      return null;
    }
    return value.replace("'", "''");
  }
}
