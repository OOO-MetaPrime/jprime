package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class JPDateUtilsTest {
  @Test
  void dateToFormatTest() {
    LocalDate ld = LocalDate.of(2025, 2, 25);
    String dateStr = JPDateUtils.dateToFormat(ld, "dd.MM.yyyy");
    Assertions.assertEquals("25.02.2025", dateStr);
  }
}
