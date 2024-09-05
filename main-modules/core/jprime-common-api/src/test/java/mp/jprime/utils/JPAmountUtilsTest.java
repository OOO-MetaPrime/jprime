package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

public class JPAmountUtilsTest {
  @Test
  void validSplitYearAmountToMonths() {
    Map<Integer, BigDecimal>  amounts = JPAmountUtils.splitYearAmountToMonths(new BigDecimal("49000"));
    Assertions.assertNotNull(amounts);
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(1));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(2));
    Assertions.assertEquals(new BigDecimal("4083.34"), amounts.get(3));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(4));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(5));
    Assertions.assertEquals(new BigDecimal("4083.34"), amounts.get(6));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(7));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(8));
    Assertions.assertEquals(new BigDecimal("4083.34"), amounts.get(9));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(10));
    Assertions.assertEquals(new BigDecimal("4083.33"), amounts.get(11));
    Assertions.assertEquals(new BigDecimal("4083.34"), amounts.get(12));
  }
}
