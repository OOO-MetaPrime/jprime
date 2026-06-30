package mp.jprime.parsers.stringformat.services;

import mp.jprime.formats.JPStringFormat;
import mp.jprime.parsers.stringformat.JpStringFormatParser;
import mp.jprime.utils.JPStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Приведение строки в соответствие с JPStringFormat
 */
@Service
public final class JpStringFormatCommonParser implements JpStringFormatParser {
  private static final int[] UL_WEIGHTS = {2, 4, 10, 3, 5, 9, 4, 6, 8};
  private static final int[] FL1_WEIGHTS = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
  private static final int[] FL2_WEIGHTS = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

  private static final int SNILS_LENGTH = 9;
  private static final int SNILS_MODULUS_DIVISOR = 101;
  private static final int SNILS_MODULUS_REMAINDER = 100;

  private static final int FULL_AGS_LENGTH = 21;

  private final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
  private final Pattern SNILS_PATTERN = Pattern.compile("(\\d{3})(?:\\s|-)?(\\d{3})(?:\\s|-)?(\\d{3})(?:\\s|-)?(\\d{2})(?:\\s|$|[.,)\\]|\\|])+");
  private final Pattern PHONE_PATTERN = Pattern.compile("(?<phone>(?<country>\\+\\d|8)?[\\s\\-]*\\(?" +
      "\\s*(?<code>\\d{3})\\s*\\)?[\\s\\-]*(?<code1>\\d{3})" +
      "[\\s\\-]*(?<code2>\\d{2})[\\s\\-]*(?<code3>\\d{2}))" +
      "(?:\\s|$|[,\\.)\\]\\|])");

  private static final Pattern ZAGS_SERIES_CLEAN_PATTERN = Pattern.compile("[^IVXА-ЯЁ]");
  private static final Pattern ZAGS_SERIES_PATTERN = Pattern.compile("^[IVX]+[А-ЯЁ]{2}$");
  private static final Pattern ZAGS_DEPARTMENT_CODE_PATTERN = Pattern.compile("^(R\\d{7}|\\d{8})$");

  private final Pattern ZERO_REMOVAL_PATTERN = Pattern.compile("^0+");

  private static final ResultRecord ERROR = ResultRecord.error();
  private static final ResultRecord EMPTY = ResultRecord.ok(null);

  private final Map<JPStringFormat, Function<String, Result>> byFormat = new HashMap<>();

  private JpStringFormatCommonParser() {
    byFormat.put(JPStringFormat.BANK_ACCOUNT_NUMBER, this::parseBankAccountNumber);
    byFormat.put(JPStringFormat.BANK_CARD_NUMBER, this::parseBankCardNumber);
    byFormat.put(JPStringFormat.BIK, this::parseBik);
    byFormat.put(JPStringFormat.EMAIL, this::parseEmail);
    byFormat.put(JPStringFormat.ERN, this::parseErn);
    byFormat.put(JPStringFormat.INN, this::parseInn);
    byFormat.put(JPStringFormat.INN_10, this::parseInn10);
    byFormat.put(JPStringFormat.INN_ANY, this::parseInnAny);
    byFormat.put(JPStringFormat.KBK, this::parseKbk);
    byFormat.put(JPStringFormat.KPP, this::parseKpp);
    byFormat.put(JPStringFormat.OGRN, this::parseOgrn);
    byFormat.put(JPStringFormat.OKTMO, this::parseOktmo);
    byFormat.put(JPStringFormat.OKTMO_11, this::parseOktmo11);
    byFormat.put(JPStringFormat.OKTMO_ANY, this::parseAnyOktmo);
    byFormat.put(JPStringFormat.PHONE, this::parsePhone);
    byFormat.put(JPStringFormat.SNILS, this::parseSnils);
    byFormat.put(JPStringFormat.ZAGS_AGS, this::parseZagsAgs);
    byFormat.put(JPStringFormat.ZAGS_DEPARTAMENT_CODE, this::parseZagsDepartmentCode);
    byFormat.put(JPStringFormat.ZAGS_NUMBER, this::parseZagsNumber);
    byFormat.put(JPStringFormat.ZAGS_SERIES, this::parseZagsSeries);
  }

  private record ResultRecord(boolean isCheck, String getParseValue) implements JpStringFormatParser.Result {
    private static final ResultRecord ERROR = new ResultRecord(false, null);

    public static ResultRecord error() {
      return ERROR;
    }

    public static ResultRecord ok(String parseValue) {
      return new ResultRecord(true, parseValue);
    }
  }

  @Override
  public Result parse(JPStringFormat format, String value) {
    Function<String, Result> func = byFormat.get(format);
    return func != null ? func.apply(value) : ResultRecord.ok(value);
  }

  @Override
  public JpStringFormatParser.Result parseBik(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 9) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseBankAccountNumber(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() >= 20 && value.length() <= 34) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseBankCardNumber(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 16) {
      // Алгоритм Luna для проверки номера банковских карт
      int result = 0;
      for (int i = 0; i < value.length(); i++) {
        int d = Integer.parseInt(String.valueOf(value.charAt(i)));
        if (i % 2 == 0) {
          d *= 2;
          if (d >= 10) {
            d -= 9;
          }
        }
        result += d;
      }
      if (result % 10 == 0) {
        return ResultRecord.ok(value);
      }
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseInn(String inn) {
    if (inn == null) {
      return EMPTY;
    }
    inn = clearDigit(inn);
    if (inn != null && isInn12(inn)) {
      return ResultRecord.ok(inn);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseInn10(String inn) {
    if (inn == null) {
      return EMPTY;
    }
    inn = clearDigit(inn);
    if (inn != null && isInn10(inn)) {
      return ResultRecord.ok(inn);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseInnAny(String inn) {
    if (inn == null) {
      return EMPTY;
    }
    inn = clearDigit(inn);
    if (inn != null && (isInn12(inn) || isInn10(inn))) {
      return ResultRecord.ok(inn);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseKbk(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 20) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseKpp(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 9) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseOgrn(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 13) {
      // Берем первые 12 цифр
      String first12 = value.substring(0, 12);
      // Вычисляем остаток от деления на 11
      long remainder = Long.parseLong(first12) % 11;
      // Младший разряд остатка
      char lastDigit = String.valueOf(remainder).charAt(0);
      // Сравниваем с 13-й цифрой
      char controlDigit = value.charAt(12);

      return lastDigit == controlDigit ? ResultRecord.ok(value) : ERROR;
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseOktmo(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 8) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseOktmo11(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 11) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseAnyOktmo(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && (value.length() == 8 || value.length() == 11)) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parsePhone(String value) {
    if (value == null) {
      return EMPTY;
    }
    Matcher matcher = PHONE_PATTERN.matcher(value);
    if (matcher.find()) {
      String country = matcher.group("country");
      String code = matcher.group("code");
      String code1 = matcher.group("code1");
      String code2 = matcher.group("code2");
      String code3 = matcher.group("code3");

      String phone = (country == null || country.equals("8") ? "7" : country.substring(1)) +
          code + code1 + code2 + code3;

      return ResultRecord.ok(phone);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseSnils(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 11) {
      Matcher matcher = SNILS_PATTERN.matcher(value);
      if (matcher.find()) {
        String val1 = matcher.group(1);
        String val2 = matcher.group(2);
        String val3 = matcher.group(3);
        String crc = matcher.group(4);

        String snils = val1 + val2 + val3;

        int c = 0;

        for (int i = 0; i < SNILS_LENGTH; i++) {
          c += Integer.parseInt(String.valueOf(snils.charAt(i))) * (SNILS_LENGTH - i);
        }

        if (c % SNILS_MODULUS_DIVISOR % SNILS_MODULUS_REMAINDER == Integer.parseInt(crc)) {
          return ResultRecord.ok(snils + crc);
        }
      }
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseErn(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 12) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseEmail(String value) {
    if (value == null) {
      return EMPTY;
    }
    String[] parts = value.split("@");
    String leftPart = parts.length == 2 ? parts[0] : null;
    String rightPart = parts.length == 2 ? parts[1] : null;
    int dotIndex = rightPart != null ? rightPart.lastIndexOf(".") : 0;
    if (leftPart != null && !leftPart.isEmpty() &&
        rightPart != null && !rightPart.isEmpty() &&
        dotIndex > 0 && dotIndex != rightPart.length() - 1) {
      return ResultRecord.ok(value);
    }

    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseFio(String s) {
    if (s == null || s.isBlank()) {
      return EMPTY;
    }
    return ResultRecord.ok(JPStringUtils.parseFio(s));
  }

  @Override
  public JpStringFormatParser.Result parseZagsSeries(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = ZAGS_SERIES_CLEAN_PATTERN.matcher(value).replaceAll("");
    if (ZAGS_SERIES_PATTERN.matcher(value).matches()) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseZagsNumber(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null && value.length() == 6) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseZagsAgs(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = clearDigit(value);
    if (value != null) {
      int length = value.length();
      if (length == FULL_AGS_LENGTH && isFullAgs(value) || length >= 1 && length <= 5) {
        return ResultRecord.ok(value);
      }
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseZagsDepartmentCode(String value) {
    if (value == null) {
      return EMPTY;
    }
    value = value.trim();
    if (ZAGS_DEPARTMENT_CODE_PATTERN.matcher(value).matches()) {
      return ResultRecord.ok(value);
    }
    return ERROR;
  }

  @Override
  public JpStringFormatParser.Result parseAgsShortNumber(String value) {
    if (value == null || value.isBlank()) {
      return EMPTY;
    }
    if (value.length() == FULL_AGS_LENGTH) {
      String result = ZERO_REMOVAL_PATTERN.matcher(value.substring(13, 18)).replaceFirst("");
      return result.isEmpty() ? EMPTY : ResultRecord.ok(result);
    } else {
      String result = ZERO_REMOVAL_PATTERN.matcher(value).replaceFirst("");
      return result.isEmpty() ? EMPTY : ResultRecord.ok(result);
    }
  }

  private String clearDigit(String s) {
    String value = s.replaceAll("\\D", "");
    Matcher matcher = DIGIT_PATTERN.matcher(value);
    return matcher.find() ? value : null;
  }

  private boolean isInn12(String inn) {
    if (inn.length() != 12) {
      return false;
    }

    int c1 = 0, c2 = 0;
    for (int i = 0; i < FL2_WEIGHTS.length; i++) {
      if (i < FL1_WEIGHTS.length) {
        c1 += Character.getNumericValue(inn.charAt(i)) * FL1_WEIGHTS[i];
      }
      c2 += Character.getNumericValue(inn.charAt(i)) * FL2_WEIGHTS[i];
    }
    if (c1 % 11 % 10 == Character.getNumericValue(inn.charAt(10)) && c2 % 11 % 10 == Character.getNumericValue(inn.charAt(11))) {
      return true;
    }
    return false;
  }

  private boolean isInn10(String inn) {
    if (inn.length() != 10) {
      return false;
    }

    int c = 0;
    for (int i = 0; i < UL_WEIGHTS.length; i++) {
      c += Character.getNumericValue(inn.charAt(i)) * UL_WEIGHTS[i];
    }
    if (c % 11 % 10 == Character.getNumericValue(inn.charAt(9))) {
      return true;
    }
    return false;
  }

  private boolean isFullAgs(String fullAgs) {
    if (fullAgs.length() != FULL_AGS_LENGTH) {
      return false;
    }

    int sum = 0;
    int[] ratios = {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2};

    for (int i = 0; i < 20; i++) {
      char ch = fullAgs.charAt(i);
      if (!Character.isDigit(ch)) {
        return false;
      }

      int value = Character.getNumericValue(ch);
      int result = value * ratios[i];
      if (result > 9) {
        result -= 9;
      }
      sum += result;
    }

    char controlCh = fullAgs.charAt(20);
    if (!Character.isDigit(controlCh)) {
      return false;
    }
    sum += Character.getNumericValue(controlCh);

    return sum % 10 == 0;
  }
}
