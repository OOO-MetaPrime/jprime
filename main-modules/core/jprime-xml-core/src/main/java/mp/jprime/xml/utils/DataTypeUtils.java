package mp.jprime.xml.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Набор утилитарных методов преобразования типов данных
 */
public final class DataTypeUtils {
  private DataTypeUtils() { }

  private static final ThreadLocal<DatatypeFactory> datatypeFactory = new ThreadLocal<>();
  private static final ThreadLocal<DecimalFormat> decimalFormat = new ThreadLocal<>();

  /**
   * Преобразует дату к формату XML
   * @param date дата
   * @return дата в формате XML
   */
  public static XMLGregorianCalendar getXmlDate(LocalDate date) {
    if (date == null) {
      return null;
    }
    GregorianCalendar gc = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
    XMLGregorianCalendar xmlCal = getDatatypeFactory().newXMLGregorianCalendar(gc);
    xmlCal.setHour(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setMinute(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setSecond(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    return xmlCal;
  }

  /**
   * Преобразует год к формату даты XML
   * @param year год
   * @return дата в формате XML
   */
  public static XMLGregorianCalendar getXmlYear(Integer year) {
    if (year == null) {
      return null;
    }
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(Calendar.YEAR, year);
    XMLGregorianCalendar xmlCal = getDatatypeFactory().newXMLGregorianCalendar(gc);
    xmlCal.setDay(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setMonth(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setHour(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setMinute(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setSecond(DatatypeConstants.FIELD_UNDEFINED);
    return xmlCal;
  }

  /**
   * Преобразует дату к формату XML
   * @param date дата
   * @return дата в формате XML
   */
  public static XMLGregorianCalendar getXmlDate(Date date) {
    if (date == null) {
      return null;
    }

    final DatatypeFactory datatypeFactory;
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException(e);
    }

    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    XMLGregorianCalendar xmlCal = datatypeFactory.newXMLGregorianCalendar(gc);
    xmlCal.setHour(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setMinute(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setSecond(DatatypeConstants.FIELD_UNDEFINED);
    xmlCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    return xmlCal;
  }

  /**
   * Преобразует дату и время к формату XML
   * @param date дата и время
   * @return дата и время в формате XML
   */
  public static XMLGregorianCalendar getXmlDateTime(LocalDateTime date) {
    if (date == null) {
      return null;
    }
    GregorianCalendar gc = GregorianCalendar.from(date.atZone(ZoneId.systemDefault()));
    return getDatatypeFactory().newXMLGregorianCalendar(gc);
  }

  /**
   * Возвращает текущую дату в формате XML
   * @return текущая дату в формате XML
   */
  public static XMLGregorianCalendar getXmlNow() {
    return getXmlDateTime(LocalDateTime.now());
  }

  /**
   * Преобразует XML дату
   * @param xmlDate XML дата
   * @return дата
   */
  public static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlDate) {
    return getLocalDateTime(xmlDate);
  }

  /**
   * Преобразует XML дату
   * @param xmlDate XML дата
   * @return дата
   */
  public static LocalDateTime getLocalDateTime(XMLGregorianCalendar xmlDate) {
    if (xmlDate == null) {
      return null;
    }
    return xmlDate.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
  }

  /**
   * Преобразует XML дату
   * @param xmlDate XML дата
   * @return дата
   */
  public static LocalDate getLocalDate(XMLGregorianCalendar xmlDate) {
    if (xmlDate == null) {
      return null;
    }
    return xmlDate.toGregorianCalendar().toZonedDateTime().toLocalDate();
  }

  /**
   * Преобразует строку даты в формате XML
   * @param xmlDateStr строка даты в формате XML
   * @return дата
   */
  public static LocalDateTime getLocalDateTime(String xmlDateStr) {
    if (xmlDateStr == null) {
      return null;
    }
    return getLocalDateTime(getDatatypeFactory().newXMLGregorianCalendar(xmlDateStr));
  }

  /**
   * Преобразует класс даты
   * @param d дата
   * @return дата
   */
  public static LocalDate toLocalDate(Date d) {
    if (d == null) {
      return null;
    }
    return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * Преобразует класс даты и время
   * @param d дата и время
   * @return дата и время
   */
  public static LocalDateTime toLocalDateTime(Date d) {
    if (d == null) {
      return null;
    }
    return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * Преобразует XML дату
   * @param xmlDate XML дата
   * @return год ((10^9-1) to (10^9)-1 or {@link DatatypeConstants#FIELD_UNDEFINED} or null)
   */
  public static Integer getYear(XMLGregorianCalendar xmlDate) {
    if (xmlDate == null) {
      return null;
    }
    return xmlDate.getYear();
  }

  /**
   * Преобразует вещественное значение к XML формату
   * @param d вещественное значение
   * @return вещественное значение в XML формате
   */
  public static BigDecimal getXmlDecimal(Double d) {
    return d != null ? new BigDecimal(d) : null;
  }

  /**
   * Преобразует вещественное значение к XML формату
   * @param d вещественное значение
   * @param precision количество знаков после запятой
   * @return вещественное значение в XML формате
   */
  public static BigDecimal getXmlDecimal(Double d, int precision) {
    return d != null ? new BigDecimal(d).setScale(precision, RoundingMode.HALF_UP) : null;
  }

  /**
   * Преобразует XML формат вещественного значения
   * @param bd XML формат вещественного значения
   * @return вещественное значение
   */
  public static Double getDouble(BigDecimal bd) {
    return bd != null ? bd.doubleValue() : null;
  }

  /**
   * Преобразует XML формат целого значения
   * @param bi XML формат целого значения
   * @return целое значение
   */
  public static Integer getInteger(BigInteger bi) {
    return bi != null ? bi.intValue() : null;
  }

  /**
   * Преобразует целое значение
   * @param i целое значение
   * @return XML формат целого значения
   */
  public static BigInteger getXmlInteger(Integer i) {
    return i != null ? BigInteger.valueOf(i) : null;
  }

  /**
   * Преобразует целое значение
   * @param l целое значение
   * @return XML формат целого значения
   */
  public static BigInteger getXmlInteger(Long l) {
    return l != null ? BigInteger.valueOf(l) : null;
  }

  /**
   * Преобразует целое значение
   * @param l целое значение
   * @return XML формат целого значения
   */
  public static BigInteger getXmlLong(Long l) {
    return l != null ? BigInteger.valueOf(l) : null;
  }

  /**
   * Возвращает признак того, что value соответствует "Да"
   * @param value Значение
   * @return Да/Нет
   */
  public static boolean isTrue(Object value) {
    if (value == null) {
      return false;
    }
    String val = value.toString();
    return val.equalsIgnoreCase("true") || val.equals("1") || val.equalsIgnoreCase("t") ||
        val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("on") || val.equalsIgnoreCase("Д") ||
        val.equalsIgnoreCase("Да") || val.equalsIgnoreCase("Есть");
  }

  private static DatatypeFactory getDatatypeFactory() {
    if (datatypeFactory.get() == null) {
      try {
        datatypeFactory.set(DatatypeFactory.newInstance());
      } catch (DatatypeConfigurationException var1) {
        throw new RuntimeException(var1);
      }
    }
    return datatypeFactory.get();
  }

  private static DecimalFormat getDecimalFormat() {
    if (decimalFormat.get() == null) {
      decimalFormat.set(new DecimalFormat("0.00"));
    }
    return decimalFormat.get();
  }

  /**
   * Накладывает на СНИЛС маску ххх-ххх-ххх-хх
   * @param s СНИЛС
   * @return СНИЛС
   */
  public static String addSnilsMask(String s) {
    if (s == null || s.length() < 11) {
      return s;
    }
    return s.substring(0,3) + '-' + s.substring(3,6) + '-' + s.substring(6,9) + '-' + s.substring(9,11);
  }

  /**
   * Преобразует вещественное значение к денежному типу
   * @param d вещественное значение
   * @return денежный тип
   */
  public static String formatMoney(Double d) {
    if (d == null) {
      return null;
    }
    return getDecimalFormat().format(d);
  }
}
