package mp.jprime.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeriodsTest {

  @Test
  void testPeriod() {
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
  void testDiffPeriods() {
    Period period = Period.between(
        LocalDate.of(2020, 1, 21),
        LocalDate.of(2020, 1, 22)
    );

    assertEquals(2, period.getDays() + 1);
  }

  @Test
  void testCrossPeriods() {
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

  @Test
  void testCrossPeriodsFromJPPeriodUtils() {
    Collection<JPPeriod> periods = JPPeriodUtils
        .add(
            JPPeriod.get(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2010, 12, 31)
            ),
            JPPeriod.get(
                LocalDate.of(2010, 2, 1),
                LocalDate.of(2010, 8, 31)
            ),
            JPPeriod.get(
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 12, 31)
            ),
            JPPeriod.get(
                LocalDate.of(2011, 5, 1),
                LocalDate.of(2016, 12, 31)
            )
        );
    assertEquals(2, periods.size());
  }

  @Test
  void mustReturnIntersectionPeriods() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 6, 1),
        LocalDate.of(2011, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriod1() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        LocalDate.of(2011, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period1.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnNull() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2011, 1, 1),
        LocalDate.of(2011, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturnNullBecauseIncorrectPeriod() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2009, 1, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturnFullPeriod() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period1.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriodEquals1Day() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2009, 1, 1),
        LocalDate.of(2010, 1, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriod2() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        null
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period2.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnFromPeriod2ToInfinity() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        null
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        null
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertNull(resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnFromInfinityToPeriod1() {
    JPPeriod period1 = JPPeriod.get(
        null,
        LocalDate.of(2009, 6, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        null,
        LocalDate.of(2010, 1, 1)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertNull(resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnInfinityPeriod() {
    JPPeriod period1 = JPPeriod.get(
        null,
        null
    );
    JPPeriod period2 = JPPeriod.get(
        null,
        null
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertNull(resultIntersecPeriod.getFrom());
    Assertions.assertNull(resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriod2WithValuePeriod1IsNull() {
    JPPeriod period1 = JPPeriod.get(
        null,
        null
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period2.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriod2WithPeriod1IsNull() {
    JPPeriod period1 = null;
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertEquals(period2.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period2.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnNullWithPeriodsIsNull() {
    JPPeriod period1 = null;
    JPPeriod period2 = null;

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2);
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturnNullWithCollectionPeriodsIsEmpty() {
    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(Collections.emptyList());
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturnNullWithTreePeriods() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2009, 1, 1),
        LocalDate.of(2009, 10, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        LocalDate.of(2010, 10, 31)
    );
    JPPeriod period3 = JPPeriod.get(
        LocalDate.of(2010, 6, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2, period3);
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturn1DayWithTreePeriods() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2009, 1, 1),
        LocalDate.of(2010, 1, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        LocalDate.of(2010, 10, 31)
    );
    JPPeriod period3 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2, period3);
    Assertions.assertEquals(period3.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnPeriod1WithTreePeriods() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 2, 1)
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        null
    );
    JPPeriod period3 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        LocalDate.of(2010, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2, period3);
    Assertions.assertEquals(period1.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period1.getTo(), resultIntersecPeriod.getTo());
  }

  @Test
  void mustReturnNullWithTreePeriodsWith2Infinity() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        null
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        null
    );
    JPPeriod period3 = JPPeriod.get(
        LocalDate.of(2008, 1, 1),
        LocalDate.of(2008, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2, period3);
    Assertions.assertNull(resultIntersecPeriod);
  }

  @Test
  void mustReturnPeriod3lWithTreePeriodsWith2Infinity() {
    JPPeriod period1 = JPPeriod.get(
        LocalDate.of(2010, 1, 1),
        null
    );
    JPPeriod period2 = JPPeriod.get(
        LocalDate.of(2009, 6, 1),
        null
    );
    JPPeriod period3 = JPPeriod.get(
        LocalDate.of(2011, 1, 1),
        LocalDate.of(2011, 12, 31)
    );

    JPPeriod resultIntersecPeriod = JPPeriodUtils.intersectionPeriod(period1, period2, period3);
    Assertions.assertEquals(period3.getFrom(), resultIntersecPeriod.getFrom());
    Assertions.assertEquals(period3.getTo(), resultIntersecPeriod.getTo());
  }


}
