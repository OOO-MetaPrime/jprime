package mp.jprime.schedule.beans;

import mp.jprime.schedule.JpScheduleCron;

import java.util.*;

/**
 * Настройки запуска
 * ┌───────────── second (0-59)
 * │ ┌───────────── minute (0 - 59)
 * │ │ ┌───────────── hour (0 - 23)
 * │ │ │ ┌───────────── day of the month (1 - 31)
 * │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
 * │ │ │ │ │ ┌───────────── day of the week (0 - 7)
 * │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
 * │ │ │ │ │ │
 * * * * * * *
 */
public class JpScheduleCronBean implements JpScheduleCron {
  private final static String DEFAULT_DESCRIPTION = "Описание не сформировано";

  private interface Symbol {
    String COMMA = ",";
    String DASH = "-";
    String SLASH = "/";
    String STAR = "*";
  }

  private static final Map<String, String> FROM_MONTH = new HashMap<>();
  private static final Map<String, String> TO_MONTH = new HashMap<>();
  private static final Map<String, String> IN_MONTH = new HashMap<>();

  private static final Map<String, String> DAYS_OF_WEEK = new HashMap<>();
  private static final Map<String, String> FROM_DAYS_OF_WEEK = new HashMap<>();
  private static final Map<String, String> TO_DAYS_OF_WEEK = new HashMap<>();
  private static final Map<String, String> IN_DAYS_OF_WEEK = new HashMap<>();

  static {
    // Месяцы
    FROM_MONTH.put("1", "с января");
    FROM_MONTH.put("2", "с февраля");
    FROM_MONTH.put("3", "с марта");
    FROM_MONTH.put("4", "с апреля");
    FROM_MONTH.put("5", "с мая");
    FROM_MONTH.put("6", "с июня");
    FROM_MONTH.put("7", "с июля");
    FROM_MONTH.put("8", "с августа");
    FROM_MONTH.put("9", "с сентября");
    FROM_MONTH.put("10", "с октября");
    FROM_MONTH.put("11", "с ноября");
    FROM_MONTH.put("12", "с декабря");
    FROM_MONTH.put("JAN", "с января");
    FROM_MONTH.put("FEB", "с февраля");
    FROM_MONTH.put("MAR", "с марта");
    FROM_MONTH.put("APR", "с апреля");
    FROM_MONTH.put("MAY", "с мая");
    FROM_MONTH.put("JUN", "с июня");
    FROM_MONTH.put("JUL", "с июля");
    FROM_MONTH.put("AUG", "с августа");
    FROM_MONTH.put("SEP", "с сентября");
    FROM_MONTH.put("OCT", "с октября");
    FROM_MONTH.put("NOV", "с ноября");
    FROM_MONTH.put("DEC", "с декабря");

    TO_MONTH.put("1", "по январь");
    TO_MONTH.put("2", "по февраль");
    TO_MONTH.put("3", "по март");
    TO_MONTH.put("4", "по апрель");
    TO_MONTH.put("5", "по май");
    TO_MONTH.put("6", "по июнь");
    TO_MONTH.put("7", "по июль");
    TO_MONTH.put("8", "по август");
    TO_MONTH.put("9", "по сентябрь");
    TO_MONTH.put("10", "по октябрь");
    TO_MONTH.put("11", "по ноябрь");
    TO_MONTH.put("12", "по декабрь");
    TO_MONTH.put("JAN", "по январь");
    TO_MONTH.put("FEB", "по февраль");
    TO_MONTH.put("MAR", "по март");
    TO_MONTH.put("APR", "по апрель");
    TO_MONTH.put("MAY", "по май");
    TO_MONTH.put("JUN", "по июнь");
    TO_MONTH.put("JUL", "по июль");
    TO_MONTH.put("AUG", "по август");
    TO_MONTH.put("SEP", "по сентябрь");
    TO_MONTH.put("OCT", "по октябрь");
    TO_MONTH.put("NOV", "по ноябрь");
    TO_MONTH.put("DEC", "по декабрь");

    IN_MONTH.put("1", "январе");
    IN_MONTH.put("2", "феврале");
    IN_MONTH.put("3", "марте");
    IN_MONTH.put("4", "апреле");
    IN_MONTH.put("5", "мае");
    IN_MONTH.put("6", "июне");
    IN_MONTH.put("7", "июле");
    IN_MONTH.put("8", "августе");
    IN_MONTH.put("9", "сентябре");
    IN_MONTH.put("10", "октябре");
    IN_MONTH.put("11", "ноябре");
    IN_MONTH.put("12", "декабре");
    IN_MONTH.put("JAN", "январе");
    IN_MONTH.put("FEB", "феврале");
    IN_MONTH.put("MAR", "марте");
    IN_MONTH.put("APR", "апреле");
    IN_MONTH.put("MAY", "мае");
    IN_MONTH.put("JUN", "июне");
    IN_MONTH.put("JUL", "июле");
    IN_MONTH.put("AUG", "августе");
    IN_MONTH.put("SEP", "сентябре");
    IN_MONTH.put("OCT", "октябре");
    IN_MONTH.put("NOV", "ноябре");
    IN_MONTH.put("DEC", "декабре");

    // Дни недели
    DAYS_OF_WEEK.put("0", "воскресенье");
    DAYS_OF_WEEK.put("1", "понедельник");
    DAYS_OF_WEEK.put("2", "вторник");
    DAYS_OF_WEEK.put("3", "среду");
    DAYS_OF_WEEK.put("4", "четверг");
    DAYS_OF_WEEK.put("5", "пятницу");
    DAYS_OF_WEEK.put("6", "субботу");
    DAYS_OF_WEEK.put("7", "воскресенье");
    DAYS_OF_WEEK.put("SUN", "воскресенье");
    DAYS_OF_WEEK.put("MON", "понедельник");
    DAYS_OF_WEEK.put("TUE", "вторник");
    DAYS_OF_WEEK.put("WED", "среду");
    DAYS_OF_WEEK.put("THU", "четверг");
    DAYS_OF_WEEK.put("FRI", "пятницу");
    DAYS_OF_WEEK.put("SAT", "субботу");

    FROM_DAYS_OF_WEEK.put("0", "с воскресенья");
    FROM_DAYS_OF_WEEK.put("1", "с понедельника");
    FROM_DAYS_OF_WEEK.put("2", "со вторника");
    FROM_DAYS_OF_WEEK.put("3", "со среды");
    FROM_DAYS_OF_WEEK.put("4", "с четверга");
    FROM_DAYS_OF_WEEK.put("5", "с пятницы");
    FROM_DAYS_OF_WEEK.put("6", "с субботы");
    FROM_DAYS_OF_WEEK.put("7", "с воскресенья");
    FROM_DAYS_OF_WEEK.put("SUN", "с воскресенья");
    FROM_DAYS_OF_WEEK.put("MON", "с понедельника");
    FROM_DAYS_OF_WEEK.put("TUE", "со вторника");
    FROM_DAYS_OF_WEEK.put("WED", "со среды");
    FROM_DAYS_OF_WEEK.put("THU", "с четверга");
    FROM_DAYS_OF_WEEK.put("FRI", "с пятницы");
    FROM_DAYS_OF_WEEK.put("SAT", "с субботы");

    TO_DAYS_OF_WEEK.put("0", "по воскресенье");
    TO_DAYS_OF_WEEK.put("1", "по понедельник");
    TO_DAYS_OF_WEEK.put("2", "по вторник");
    TO_DAYS_OF_WEEK.put("3", "по среду");
    TO_DAYS_OF_WEEK.put("4", "по четвергу");
    TO_DAYS_OF_WEEK.put("5", "по пятницу");
    TO_DAYS_OF_WEEK.put("6", "по субботу");
    TO_DAYS_OF_WEEK.put("7", "по воскресенье");
    TO_DAYS_OF_WEEK.put("SUN", "по воскресенье");
    TO_DAYS_OF_WEEK.put("MON", "по понедельник");
    TO_DAYS_OF_WEEK.put("TUE", "по вторник");
    TO_DAYS_OF_WEEK.put("WED", "по среду");
    TO_DAYS_OF_WEEK.put("THU", "по четверг");
    TO_DAYS_OF_WEEK.put("FRI", "по пятницу");
    TO_DAYS_OF_WEEK.put("SAT", "по субботу");

    IN_DAYS_OF_WEEK.put("0", "каждое воскресенье");
    IN_DAYS_OF_WEEK.put("1", "каждый понедельник");
    IN_DAYS_OF_WEEK.put("2", "каждый вторник");
    IN_DAYS_OF_WEEK.put("3", "каждую среду");
    IN_DAYS_OF_WEEK.put("4", "каждый четверг");
    IN_DAYS_OF_WEEK.put("5", "каждую пятницу");
    IN_DAYS_OF_WEEK.put("6", "каждую субботу");
    IN_DAYS_OF_WEEK.put("7", "каждое воскресенье");
    IN_DAYS_OF_WEEK.put("SUN", "каждое воскресенье");
    IN_DAYS_OF_WEEK.put("MON", "каждый понедельник");
    IN_DAYS_OF_WEEK.put("TUE", "каждое вторник");
    IN_DAYS_OF_WEEK.put("WED", "каждую среду");
    IN_DAYS_OF_WEEK.put("THU", "каждый четверг");
    IN_DAYS_OF_WEEK.put("FRI", "каждую пятницу");
    IN_DAYS_OF_WEEK.put("SAT", "каждую субботу");
  }

  private final String description;
  private final String expression;

  protected JpScheduleCronBean(String expression) {
    this.description = toDescription(expression);
    this.expression = expression;
  }

  public static JpScheduleCron of(String expression) {
    return new JpScheduleCronBean(expression);
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  private String toDescription(String expression) {
    try {
      String[] fields = expression != null ? expression.trim().split("\\s+") : null;
      if (fields == null || fields.length != 6) {
        return DEFAULT_DESCRIPTION;
      }
      String seconds = fields[0];
      String minutes = fields[1];
      String hours = fields[2];
      String dayOfMonth = fields[3];
      String month = fields[4];
      String dayOfWeek = fields[5];

      if (Symbol.STAR.equals(seconds) &&
          Symbol.STAR.equals(minutes) &&
          Symbol.STAR.equals(hours) &&
          Symbol.STAR.equals(dayOfMonth) &&
          Symbol.STAR.equals(month) &&
          Symbol.STAR.equals(dayOfWeek)) {
        return "каждую секунду минуты";
      }

      LinkedList<String> tokens = new LinkedList<>();

      TokenDesc tokenDesc = appendTimeDesc(false, true, seconds,
          "каждую", "секунду", "секунды", "секунд", "минуты"
      );
      boolean prevEveryCheck = append(tokens, tokenDesc, false);

      tokenDesc = appendTimeDesc(prevEveryCheck, tokens.isEmpty(), minutes,
          "каждую", "минуту", "минуты", "минут", "часа"
      );
      prevEveryCheck = append(tokens, tokenDesc, prevEveryCheck);

      tokenDesc = appendTimeDesc(prevEveryCheck, tokens.isEmpty(), hours,
          "каждый", "час", "часа", "часов", "суток"
      );
      prevEveryCheck = append(tokens, tokenDesc, prevEveryCheck);

      tokens.addFirst(getDateDescription(prevEveryCheck, dayOfMonth, month, dayOfWeek));

      return String.join(" ", tokens).trim();
    } catch (Exception e) {
      return DEFAULT_DESCRIPTION;
    }
  }

  private boolean append(LinkedList<String> tokens, TokenDesc tokenDesc, boolean prevEveryCheck) {
    if (tokenDesc.text() != null) {
      tokens.addFirst(tokenDesc.text());

      return tokenDesc.prevEveryCheck;
    } else {
      return prevEveryCheck || tokenDesc.prevEveryCheck;
    }
  }

  private String getDateDescription(boolean prevEveryCheck, String dayOfMonth, String month, String dayOfWeek) {
    LinkedList<String> tokens = new LinkedList<>();

    boolean hasDayOfMonth = !Symbol.STAR.equals(dayOfMonth) && !"?".equals(dayOfMonth);
    boolean hasDayOfWeek = !Symbol.STAR.equals(dayOfWeek) && !"?".equals(dayOfWeek);
    boolean hasMonth = !Symbol.STAR.equals(month);

    // Обработка дней недели
    if (hasDayOfWeek) {
      if (dayOfWeek.contains("#")) {
        String[] parts = dayOfWeek.split("#");
        String day = DAYS_OF_WEEK.getOrDefault(parts[0], parts[0]);
        tokens.addFirst("в " + getOrdinal(parts[1]) + " " + day + " каждого месяца");
      } else if ("L".equals(dayOfWeek)) {
        tokens.addFirst("в последний день недели месяца");
      } else if (dayOfWeek.endsWith("L")) {
        String dayNum = dayOfWeek.substring(0, dayOfWeek.length() - 1);
        String day = DAYS_OF_WEEK.getOrDefault(dayNum.toUpperCase(), dayNum);
        tokens.addFirst("в последний " + day + " месяца");
      } else if (dayOfWeek.contains(Symbol.SLASH)) {
        String[] parts = dayOfWeek.split(Symbol.SLASH);
        int interval = Integer.parseInt(parts[1]);
        if (interval == 1) {
          tokens.addFirst("каждый день недели");
        } else {
          tokens.addFirst("каждый " + interval + " день недели");
        }
      } else if (dayOfWeek.contains(Symbol.COMMA)) {
        String[] days = dayOfWeek.split(Symbol.COMMA);
        List<String> dayNames = new ArrayList<>();
        for (String d : days) {
          dayNames.add(DAYS_OF_WEEK.getOrDefault(d.toUpperCase(), d));
        }
        tokens.addFirst("в " + String.join(", ", dayNames));
      } else if (dayOfWeek.contains(Symbol.DASH)) {
        String[] range = dayOfWeek.split(Symbol.DASH);
        String from = FROM_DAYS_OF_WEEK.getOrDefault(range[0].toUpperCase(), range[0]);
        String to = TO_DAYS_OF_WEEK.getOrDefault(range[1].toUpperCase(), range[1]);
        tokens.addFirst(from + " " + to);
      } else {
        tokens.addFirst(IN_DAYS_OF_WEEK.getOrDefault(dayOfWeek.toUpperCase(), dayOfWeek));
      }
    }

    // Обработка дней месяца
    if (hasDayOfMonth) {
      if ("L".equals(dayOfMonth)) {
        tokens.addFirst("в последний день месяца");
      } else if (dayOfMonth.endsWith("W")) {
        String day = dayOfMonth.substring(0, dayOfMonth.length() - 1);
        tokens.addFirst("в ближайший рабочий день к " + day + " числу");
      } else if (dayOfMonth.contains(Symbol.SLASH)) {
        String[] parts = dayOfMonth.split(Symbol.SLASH);
        int interval = Integer.parseInt(parts[1]);
        if (interval == 1) {
          tokens.addFirst("каждый день месяца");
        } else {
          tokens.addFirst("каждый " + interval + " день месяца");
        }
      } else if (dayOfMonth.contains(Symbol.COMMA)) {
        String[] days = dayOfMonth.split(Symbol.COMMA);
        tokens.addFirst(String.join(", ", days) + " числа");
      } else if (dayOfMonth.contains(Symbol.DASH)) {
        String[] range = dayOfMonth.split(Symbol.DASH);
        tokens.addFirst("с " + range[0] + " по " + range[1] + " число");
      } else {
        tokens.addFirst(dayOfMonth + " числа");
      }
    } else if (!hasDayOfWeek && !prevEveryCheck) {
      tokens.addFirst("каждый день месяца");
      prevEveryCheck = true;
    }

    // Обработка месяцев
    if (hasMonth) {
      if (month.contains(Symbol.SLASH)) {
        String[] parts = month.split(Symbol.SLASH);
        int interval = Integer.parseInt(parts[1]);
        if (interval == 1) {
          tokens.addFirst("каждый месяц года");
        } else {
          tokens.addFirst("каждый " + interval + " месяц года");
        }
      } else if (month.contains(Symbol.COMMA)) {
        String[] monthsList = month.split(Symbol.COMMA);
        List<String> monthNames = new ArrayList<>();
        for (String m : monthsList) {
          monthNames.add(IN_MONTH.getOrDefault(m.toUpperCase(), m));
        }
        tokens.addFirst("в " + String.join(", ", monthNames));
      } else if (month.contains(Symbol.DASH)) {
        String[] range = month.split(Symbol.DASH);
        String from = FROM_MONTH.getOrDefault(range[0].toUpperCase(), range[0]);
        String to = TO_MONTH.getOrDefault(range[1].toUpperCase(), range[1]);
        tokens.addFirst(from + " " + to);
      } else {
        tokens.addFirst("в " + IN_MONTH.getOrDefault(month.toUpperCase(), month));
      }
    } else if (!prevEveryCheck) {
      tokens.addFirst("каждый месяц года");
    }

    // Собираем полное описание
    return String.join(" ", tokens).trim();
  }

  private record TokenDesc(boolean prevEveryCheck, String text) {

  }

  private TokenDesc appendTimeDesc(boolean prevEveryCheck, boolean emptyPrevToken,
                                   String value, String everyOneText, String oneText,
                                   String twoText, String manyText,
                                   String upperText) {
    if (Symbol.STAR.equals(value)) {
      if (prevEveryCheck) {
        return new TokenDesc(false, null);
      }
      return new TokenDesc(true, everyOneText + " " + oneText + " " + upperText);
    } else if (value.contains(Symbol.SLASH)) {
      String[] parts = value.split(Symbol.SLASH);
      int interval = Integer.parseInt(parts[1]);
      if (interval == 1) {
        return new TokenDesc(true, everyOneText + " " + oneText + " " + upperText);
      } else {
        return new TokenDesc(true, getNumeric(interval, everyOneText, oneText, everyOneText, oneText, oneText) + " " + upperText);
      }
    } else if (value.contains(Symbol.COMMA)) {
      String[] hourList = value.split(Symbol.COMMA);
      List<String> hourNames = new ArrayList<>(Arrays.asList(hourList));
      return new TokenDesc(false, "в " + String.join(", ", hourNames) + " " + ((hourNames.size() == 1) ? oneText : manyText));
    } else if (value.contains(Symbol.DASH)) {
      String[] range = value.split(Symbol.DASH);
      return new TokenDesc(false, "c " + range[0] + " до " + range[1] + " " + manyText);
    } else {
      int val = Integer.parseInt(value);
      return new TokenDesc(false, getNumeric(val, "в", oneText, "в", twoText, manyText));
    }
  }

  private String getNumeric(int interval, String everyOneText, String oneText,
                            String everyManyText, String twoText, String manyText) {
    int mod = interval % 10;
    if (interval != 11 && mod == 1) {
      return everyOneText + " " + interval + " " + oneText;
    } else if (interval == 12 || interval == 13 || interval == 14) {
      return everyManyText + " " + interval + " " + manyText;
    } else if (mod == 2 || mod == 3 || mod == 4) {
      return everyManyText + " " + interval + " " + twoText;
    } else {
      return everyManyText + " " + interval + " " + manyText;
    }
  }

  private String getOrdinal(String num) {
    int n = Integer.parseInt(num);
    return switch (n) {
      case 1 -> "первый";
      case 2 -> "второй";
      case 3 -> "третий";
      case 4 -> "четвертый";
      case 5 -> "пятый";
      default -> n + "-й";
    };
  }
}
