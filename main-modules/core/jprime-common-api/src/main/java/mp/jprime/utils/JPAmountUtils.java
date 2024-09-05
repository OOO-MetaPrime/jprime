package mp.jprime.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Логика работы с суммами
 */
public final class JPAmountUtils {
  private final static BigDecimal KOPECK_VALUE = new BigDecimal("0.01");
  private final static BigDecimal VALUE_12 = new BigDecimal(12);
  private final static Map<Integer, BigDecimal> MONTHS_COUNT = new HashMap<>() {
    {
      put(1, BigDecimal.valueOf(1));
      put(2, BigDecimal.valueOf(2));
      put(3, BigDecimal.valueOf(3));
      put(4, BigDecimal.valueOf(4));
      put(5, BigDecimal.valueOf(5));
      put(6, BigDecimal.valueOf(6));
      put(7, BigDecimal.valueOf(7));
      put(8, BigDecimal.valueOf(8));
      put(9, BigDecimal.valueOf(9));
      put(10, BigDecimal.valueOf(10));
      put(11, BigDecimal.valueOf(11));
      put(12, BigDecimal.valueOf(12));
    }
  };

  private JPAmountUtils() {

  }

  /**
   * Разделение годовой суммы по месяцам
   *
   * @param yearValue Сумма на год
   * @return Помесячные суммы
   */
  public static Map<Integer, BigDecimal> splitYearAmountToMonths(BigDecimal yearValue) {
    if (yearValue == null || yearValue.equals(BigDecimal.ZERO)) {
      return Collections.emptyMap();
    }
    Map<Integer, BigDecimal> result = new HashMap<>();

    BigDecimal monthRoundAmount = yearValue.divide(VALUE_12, 2, RoundingMode.DOWN);

    BigDecimal totalRoundAmount = BigDecimal.ZERO;
    for (int i = 1; i <= 12; i++) {
      BigDecimal count = MONTHS_COUNT.get(i);

      BigDecimal totalAmount = yearValue.multiply(count).divide(VALUE_12, 2, RoundingMode.DOWN);
      totalRoundAmount = totalRoundAmount.add(monthRoundAmount);

      BigDecimal monthValue;
      if (totalAmount.subtract(totalRoundAmount).equals(KOPECK_VALUE)) {
        // Если накопилась копейка после округления, добавляем ее в сумму
        monthValue = monthRoundAmount.add(KOPECK_VALUE);
        totalRoundAmount = totalRoundAmount.add(KOPECK_VALUE);
      } else {
        monthValue = monthRoundAmount;
      }
      result.put(i, monthValue);
    }
    return Collections.unmodifiableMap(result);
  }
}
