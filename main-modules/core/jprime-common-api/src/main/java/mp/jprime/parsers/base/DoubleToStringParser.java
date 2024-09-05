package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Double -> String
 */
@Service
public final class DoubleToStringParser implements TypeParser<Double, String> {
  /**
   * Возвращает DecimalFormat
   * @return DecimalFormat
   */
  private static DecimalFormat getDecimalFormat() {
    DecimalFormat df = new DecimalFormat();
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setDecimalSeparator('.');
    df.setGroupingUsed(false);
    df.setMaximumFractionDigits(Integer.MAX_VALUE);
    df.setDecimalFormatSymbols(dfs);
    return df;
  }
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(Double value) {
    return value != null ? getDecimalFormat().format(value) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Double> getInputType() {
    return Double.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}