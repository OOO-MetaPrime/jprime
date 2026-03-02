package mp.jprime.dataaccess.functions.common;

import mp.jprime.dataaccess.functions.JPDataBaseFunction;
import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionResultBean;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

/**
 * Функция преобразование даты в текстовое описание
 */
@Service
public class JPDateToWordsFunction extends JPDataBaseFunction<String> {
  /**
   * Кодовое имя функции
   */
  private static final String CODE = "dateToWords";

  /**
   * Шаблоны
   */
  public interface Template {
    // первое января две тысячи двадцатого года
    String WORDS_1 = "words_1";
    // 01 января 2020 г.
    String FORMAT_1 = "format_1";
    // 01 января 2020 года
    String FORMAT_2 = "format_2";
    // 1 января 2020 г.
    String FORMAT_3 = "format_3";
    // 1 января 2020 года
    String FORMAT_4 = "format_4";
    // "01" января 2020 г.
    String FORMAT_5 = "format_5";
    // "01" января 2020 года
    String FORMAT_6 = "format_6";
    // "1" января 2020 г.
    String FORMAT_7 = "format_7";
    // "1" января 2020 года
    String FORMAT_8 = "format_8";
  }

  /**
   * Аргументы
   */
  private interface Arg {
    // поле с датой
    String DATE = "date";
  }

  /**
   * Шаблоны для вызова функции
   */
  private static final Collection<String> TEMPLATES = Set.of(
      Template.WORDS_1,
      Template.FORMAT_1,
      Template.FORMAT_2,
      Template.FORMAT_3,
      Template.FORMAT_4,
      Template.FORMAT_5,
      Template.FORMAT_6,
      Template.FORMAT_7,
      Template.FORMAT_8
  );

  /**
   * Кодовые имена аргументов функции
   */
  private static final List<String> ARG_CODES = Collections.singletonList(Arg.DATE);

  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Collection<String> getTemplates() {
    return TEMPLATES;
  }

  @Override
  protected List<String> getArgCodes() {
    return ARG_CODES;
  }

  @Override
  protected Map<JPDataFunctionParams, JPDataFunctionResult<String>> compute(Collection<JPDataFunctionParams> args, AuthInfo auth) {
    Map<JPDataFunctionParams, JPDataFunctionResult<String>> result = new HashMap<>(args.size());
    args.forEach(x -> {
      String value = null;
      LocalDate date = parserService.parseTo(LocalDate.class, x.getArgs().get(0));

      Function<LocalDate, String> func = date != null ? TEMPLATE_FUNC_LIST.get(x.getTemplate()) : null;
      if (func != null) {
        value = func.apply(date);
      }
      if (value == null) {
        value = "";
      }
      result.put(x, JPDataFunctionResultBean.of(value));
    });
    return result;
  }

  // Названия дня/месяца в родительном падеже
  private static final Map<Integer, String> DAY_NAME_LIST = new HashMap<>() {{
    put(1, "первое");
    put(2, "второе");
    put(3, "третье");
    put(4, "четвёртое");
    put(5, "пятое");
    put(6, "шестое");
    put(7, "седьмое");
    put(8, "восьмое");
    put(9, "девятое");
    put(10, "десятое");
    put(11, "одиннадцатое");
    put(12, "двенадцатое");
    put(13, "тринадцатое");
    put(14, "четырнадцатое");
    put(15, "пятнадцатое");
    put(16, "шестнадцатое");
    put(17, "семнадцатое");
    put(18, "восемнадцатое");
    put(19, "девятнадцатое");
    put(20, "двадцатое");
    put(21, "двадцать первое");
    put(22, "двадцать второе");
    put(23, "двадцать третье");
    put(24, "двадцать четвёртое");
    put(25, "двадцать пятое");
    put(26, "двадцать шестое");
    put(27, "двадцать седьмое");
    put(28, "двадцать восьмое");
    put(29, "двадцать девятое");
    put(30, "тридцатое");
    put(31, "тридцать первое");
  }};

  private static final Map<Integer, String> MONTH_NAME_LIST = new HashMap<>() {{
    put(1, "января");
    put(2, "февраля");
    put(3, "марта");
    put(4, "апреля");
    put(5, "мая");
    put(6, "июня");
    put(7, "июля");
    put(8, "августа");
    put(9, "сентября");
    put(10, "октября");
    put(11, "ноября");
    put(12, "декабря");
  }};

  // Функции трансформации согласно шаблона
  private static final Map<String, Function<LocalDate, String>> TEMPLATE_FUNC_LIST = Map.of(
      Template.WORDS_1, date -> {
        // первое января две тысячи двадцатого года
        return date == null ? "" : DAY_NAME_LIST.get(date.getDayOfMonth()) + " " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + toWords(date.getYear()) + " года";
      },
      Template.FORMAT_1, date -> {
        // 01 января 2020 г.
        Integer day = date != null ? date.getDayOfMonth() : null;
        return date == null ? "" : (day < 10 ? "0" : "") + day + " " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " г.";
      },
      Template.FORMAT_2, date -> {
        // 01 января 2020 года
        Integer day = date != null ? date.getDayOfMonth() : null;
        return date == null ? "" : (day < 10 ? "0" : "") + day + " " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " года";
      },
      Template.FORMAT_3, date -> {
        // 1 января 2020 г.
        return date == null ? "" : date.getDayOfMonth() + " " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " г.";
      },
      Template.FORMAT_4, date -> {
        // 1 января 2020 года
        return date == null ? "" : date.getDayOfMonth() + " " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " года";
      },
      Template.FORMAT_5, date -> {
        // "01" января 2020 г.
        Integer day = date != null ? date.getDayOfMonth() : null;
        return date == null ? "" : "\"" + (day < 10 ? "0" : "") + day + "\" " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " г.";
      },
      Template.FORMAT_6, date -> {
        // "01" января 2020 года
        Integer day = date != null ? date.getDayOfMonth() : null;
        return date == null ? "" : "\"" + (day < 10 ? "0" : "") + day + "\" " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " года";
      },
      Template.FORMAT_7, date -> {
        // "1" января 2020 г.
        return date == null ? "" : "\"" + date.getDayOfMonth() + "\" " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " г.";
      },
      Template.FORMAT_8, date -> {
        // "1" января 2020 года
        return date == null ? "" : "\"" + date.getDayOfMonth() + "\" " + MONTH_NAME_LIST.get(date.getMonthValue()) + " " + date.getYear() + " года";
      }
  );

  private static String toWords(Integer value) {
    int thousands = value / 1_000;
    int hundreds = (value - thousands * 1_000) / 100;
    int tens = (value - thousands * 1_000 - hundreds * 100) / 10;
    int ones = value - thousands * 1_000 - hundreds * 100 - tens * 10;

    String result = hundreds == 0 && tens == 0 && ones == 0 ?
        switch (thousands) {
          case 1 -> "тысячного";
          case 2 -> "двухтысячного";
          case 3 -> "трехтысячного";
          case 4 -> "четырехтысячного";
          default -> "";
        }
        :
        switch (thousands) {
          case 1 -> "одна тысяча";
          case 2 -> "две тысячи";
          case 3 -> "три тысячи";
          case 4 -> "четыре тысячи";
          default -> "";
        };

    if (hundreds > 0) {
      result = result + (tens == 0 && ones == 0 ?
          switch (hundreds) {
            case 1 -> " сотого";
            case 2 -> " двухсотого";
            case 3 -> " трехсотого";
            case 4 -> " четырехсотого";
            case 5 -> " пятисотого";
            case 6 -> " шестисотого";
            case 7 -> " семисотого";
            case 8 -> " восьмисотого";
            case 9 -> " девятисотого";
            default -> "";
          }
          :
          switch (hundreds) {
            case 1 -> "сто";
            case 2 -> "двести";
            case 3 -> "триста";
            case 4 -> "четыре тысячи";
            case 5 -> "пять тысяч";
            case 6 -> "шесть тысяч";
            case 7 -> "семь тысяч";
            case 8 -> "восемь тысяч";
            case 9 -> "девять тысяч";
            default -> "";
          });
    }

    if (tens > 0) {
      if (ones == 0) {
        result = result + " " + switch (tens) {
          case 1 -> "десятого";
          case 2 -> "двадцатого";
          case 3 -> "тридцатого";
          case 4 -> "сорокового";
          case 5 -> "пятидесятого";
          case 6 -> "шестидесятого";
          case 7 -> "семидесятого";
          case 8 -> "восьмидесятого";
          case 9 -> "девяностого";
          default -> "";
        };
      }
      if (tens == 1 && ones > 0) {
        result = result + " " + switch (ones) {
          case 1 -> "одиннадцатого";
          case 2 -> "двенадцатого";
          case 3 -> "тринадцатого";
          case 4 -> "четырнадцатого";
          case 5 -> "пятнадцатого";
          case 6 -> "шестнадцатого";
          case 7 -> "семнадцатого";
          case 8 -> "восемнадцатого";
          case 9 -> "девятнадцатого";
          default -> "";
        };
      }
      if (tens > 1 && ones > 0) {
        result = result + " " + switch (tens) {
          case 2 -> "двадцать";
          case 3 -> "тридцать";
          case 4 -> "сорок";
          case 5 -> "пятьдесят";
          case 6 -> "шестьдесят";
          case 7 -> "семьдесят";
          case 8 -> "восемьдесят";
          case 9 -> "девяносто";
          default -> "";
        };
      }
    }

    result = result + (tens == 1 || ones == 0 ? "" : " " + switch (ones) {
      case 1 -> "первого";
      case 2 -> "второго";
      case 3 -> "третьего";
      case 4 -> "четвертого";
      case 5 -> "пятого";
      case 6 -> "шестого";
      case 7 -> "седьмого";
      case 8 -> "восьмого";
      case 9 -> "девятого";
      default -> "";
    });

    return result;
  }

}
