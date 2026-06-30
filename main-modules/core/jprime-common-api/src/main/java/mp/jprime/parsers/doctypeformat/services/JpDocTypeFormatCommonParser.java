package mp.jprime.parsers.doctypeformat.services;

import mp.jprime.parsers.doctypeformat.JpDocTypeFormatParser;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Приведение строки в соответствие с типом документа
 */
@Service
public final class JpDocTypeFormatCommonParser implements JpDocTypeFormatParser {

  private final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
  private final Pattern CYRILLIC_PATTERN = Pattern.compile("^([А-Яа-я]+)$");
  private final Pattern ROMAN_PATTERN = Pattern.compile("^([IVXLCDM]+)$");

  @Override
  public Result parseRussiaBirthCertificate(String series, String number) {
    if (series == null && number == null) {
      return ParseResult.ERROR;
    }
    boolean checkSeries = false;

    series = series != null ? series.toUpperCase() : null;
    if (series == null) {
      checkSeries = true;
    } else {
      String s = series.trim();
      boolean split = series.contains("-");
      int length = series.length();

      String s1;
      String s2;
      if (split || length > 2) {
        if (split) {
          String[] tokens = series.split("-");
          s1 = tokens[0].trim();
          s2 = tokens[1].trim();
        } else {
          s1 = s.substring(0, length - 2);
          s2= s.substring(length - 2, length);
        }
        checkSeries = s1.length() <= 5 &&
            s2.length() == 2 &&
            ROMAN_PATTERN.matcher(s1).find() &&
            CYRILLIC_PATTERN.matcher(s2).find();

        series = s1 + s2;
      } else {
        checkSeries = s.length() == 2 && CYRILLIC_PATTERN.matcher(s).find();
        series = s;
      }
    }

    number = clearDigit(number);
    boolean checkNumber = number != null && number.length() <= 6;

    return new ParseResult(checkSeries && checkNumber,
        checkSeries ? series : null, checkSeries,
        checkNumber ? number : null, checkNumber
    );
  }

  @Override
  public Result parseRussiaMilitatyId(String series, String number) {
    if (series == null && number == null) {
      return ParseResult.ERROR;
    }

    if (series != null) {
      Matcher matcher = CYRILLIC_PATTERN.matcher(series);
      series = matcher.find() ? series : null;
    }
    number = clearDigit(number);

    boolean checkSeries = series != null && series.length() == 2;
    boolean checkNumber = number != null && number.length() >= 5 && number.length() <= 7;
    return new ParseResult(checkSeries && checkNumber,
        checkSeries ? series : null, checkSeries,
        checkNumber ? number : null, checkNumber
    );
  }

  @Override
  public Result parseRussiaPassport(String series, String number) {
    if (series == null && number == null) {
      return ParseResult.ERROR;
    }
    series = clearDigit(series);
    number = clearDigit(number);

    boolean checkSeries = series != null && series.length() == 4;
    boolean checkNumber = number != null && number.length() == 6;
    return new ParseResult(checkSeries && checkNumber,
        checkSeries ? series : null, checkSeries,
        checkNumber ? number : null, checkNumber
    );
  }

  private String clearDigit(String s) {
    if (s == null) {
      return null;
    }
    String value = s.replaceAll("\\D", "");
    Matcher matcher = DIGIT_PATTERN.matcher(value);
    return matcher.find() ? value : null;
  }

  private record ParseResult(boolean isCheck,
                             String getParseSeries,
                             boolean isSeriesCheck,
                             String getParseNumber,
                             boolean isNumberCheck) implements JpDocTypeFormatParser.Result {
    private static final ParseResult ERROR = new ParseResult(false, null, false, null, false);

    public static ParseResult error() {
      return ERROR;
    }
  }

  /**
   * Переводит символы из одного набора в другой.
   *
   * @param series    Исходная строка с символами для перевода.
   * @param fromChars Символы из исходного набора.
   * @param toChars   Символы из целевого набора.
   * @return Строка с символами, переведенными из одного набора в другой.
   */
  private static String translateSeries(String series, String fromChars, String toChars) {
    char[] charArray = series.toCharArray();
    StringBuilder translated = new StringBuilder();

    for (char c : charArray) {
      int index = fromChars.indexOf(c);

      if (index != -1) {
        translated.append(toChars.charAt(index));
      } else {
        translated.append(c);
      }
    }
    return translated.toString();
  }
}
