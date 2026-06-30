package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JPDateUtilsTest {
  @Test
  void dateToFormatTest() {
    LocalDate ld = LocalDate.of(2025, 2, 25);
    String dateStr = JPDateUtils.dateToFormat(ld, "dd.MM.yyyy");
    Assertions.assertEquals("25.02.2025", dateStr);
  }

  @Test
  void dateTimeToFormatTest() {
    LocalDateTime ld = LocalDateTime.of(2025, 2, 25, 20, 34);
    String dateStr = JPDateUtils.dateTimeToFormat(ld, "dd.MM.yyyy'T'HH:mm:ss");
    Assertions.assertEquals("25.02.2025T20:34:00", dateStr);
  }

  @Test
  void weekOfMonthNumberTest() {
    Assertions.assertEquals(1, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2025, 12, 1)));
    Assertions.assertEquals(5, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2025, 12, 31)));

    Assertions.assertEquals(1, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2026, 3, 1)));
    Assertions.assertEquals(6, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2026, 3, 31)));

    Assertions.assertEquals(1, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2026, 4, 3)));
    Assertions.assertEquals(3, JPDateUtils.getWeekOfMonthNumber(LocalDate.of(2026, 4, 15)));
  }
}
