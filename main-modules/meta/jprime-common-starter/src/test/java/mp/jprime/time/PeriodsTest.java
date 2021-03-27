package mp.jprime.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration()
class PeriodsTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(value = {"mp.jprime.time"})
  public static class Config {
  }

  @Test
  public void testPeriod() {
    int y = 1;
    int m = 1;
    int d = 100;
    m += (d / 30); // Увеличиваем месяца
    d = (d % 30); // Уменьшаем дни
    Period period = Period.of(Math.max(y, 0), Math.max(m, 0), Math.max(d, 0)).normalized();

    assertEquals(1, period.getYears());
    assertEquals(4, period.getMonths());
    assertEquals(10, period.getDays());
  }

  @Test
  public void testDiffPeriods() {
    Period period = Period.between(
        LocalDate.of(2020,1,21),
        LocalDate.of(2020,1,22)
    );

    assertEquals(2, period.getDays() + 1);
  }

  @Test
  public void testCrossPeriods() {
    Collection<JPPeriod> periods = JPPeriods.get()
        .add(
            JPPeriod.get(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2010, 12, 31)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2010, 2, 1),
                LocalDate.of(2010, 8, 31)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 12, 31)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2011, 5, 1),
                LocalDate.of(2016, 12, 31)
            )
        ).getPeriod();
    assertEquals(2, periods.size());
  }

}
