package mp.jprime.formats;

/**
 * Форматы дат
 */
public final class DateFormat {
  /**
   * Год-месяц-день
   */
  public static final String YYYYY_MM_DD = "yyyy-MM-dd";
  /**
   * день-месяц-год
   */
  public static final String DD_MM_YYYY = "dd-MM-yyyy";
  /**
   * день.месяц.год
   */
  public static final String DDdMMdYYYY = "dd.MM.yyyy";
  /**
   * Формат ИСО 8601 (полное время)
   */
  public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  /**
   * Формат ИСО 8601 - (год-месяц-день-час-минута)
   */
  public static final String ISO8601_YYYY_MM_DD_HH_MM = "yyyy-MM-dd'T'HH:mmZ";
}
