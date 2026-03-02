package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Double -> String
 */
@Service
public final class DoubleToStringParser extends BaseTypeParser<Double, String> {
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
  @Override
  public String parse(Double value) {
    return value != null ? getDecimalFormat().format(value) : null;
  }

  @Override
  public Class<Double> getInputType() {
    return Double.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}