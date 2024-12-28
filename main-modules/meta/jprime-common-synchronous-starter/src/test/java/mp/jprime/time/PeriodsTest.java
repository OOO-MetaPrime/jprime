package mp.jprime.time;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                LocalDate.of(2011, 1, 2),
                LocalDate.of(2011, 12, 31)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2011, 5, 1),
                LocalDate.of(2016, 12, 31)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1)
            )
        ).add(
            JPPeriod.get(
                LocalDate.of(2017, 1, 2),
                LocalDate.of(2017, 12, 31)
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
                LocalDate.of(2011, 1, 2),
                LocalDate.of(2011, 12, 31)
            ),
            JPPeriod.get(
                LocalDate.of(2011, 5, 1),
                LocalDate.of(2016, 12, 31)
            ),
            JPPeriod.get(
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1)
            ),
            JPPeriod.get(
                LocalDate.of(2017, 1, 2),
                LocalDate.of(2017, 12, 31)
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

  @Test
  void mustIntersectGroupsOfPeriods() {
    JPPeriods period1 = JPPeriods.get()
        .add(
            JPPeriod.get(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2010, 11, 30)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 11, 30)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2013, 5, 1),
                LocalDate.of(2013, 11, 30)
            )
        );

    JPPeriods period2 = JPPeriods.get()
        .add(
            JPPeriod.get(
                LocalDate.of(2011, 5, 1),
                LocalDate.of(2012, 11, 30)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2013, 10, 1),
                LocalDate.of(2013, 12, 30)
            )
        );

    JPPeriods period3 = JPPeriods.get()
        .add(
            JPPeriod.get(
                null,
                LocalDate.of(2011, 3, 30)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2011, 4, 1),
                LocalDate.of(2011, 5, 30)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2013, 7, 1),
                LocalDate.of(2013, 10, 14)
            )
        )
        .add(
            JPPeriod.get(
                LocalDate.of(2013, 10, 19),
                null
            )
        );
    JPPeriods infinitePeriod = JPPeriods.get().add(JPPeriod.get(null, null));

    Collection<JPPeriod> expected = Arrays.asList(
        JPPeriod.get(
            LocalDate.of(2011, 5, 1),
            LocalDate.of(2011, 5, 30)
        ),
        JPPeriod.get(
            LocalDate.of(2013, 10, 1),
            LocalDate.of(2013, 10, 14)
        ),
        JPPeriod.get(
            LocalDate.of(2013, 10, 19),
            LocalDate.of(2013, 11, 30)
        )
    );

    Collection<JPPeriod> result = JPPeriodUtils.intersections(period1, period2, period3, infinitePeriod);
    assertTrue(CollectionUtils.isEqualCollection(expected, result));
  }

  @Test
  void mustIntersectInfinitePeriods() {
    JPPeriods infinitePeriod = JPPeriods.get().add(JPPeriod.get(null, null));

    Collection<JPPeriod> result = JPPeriodUtils.intersections(infinitePeriod, infinitePeriod);
    assertTrue(CollectionUtils.isEqualCollection(infinitePeriod.getPeriod(), result));
  }

  @Test
  void mustCollectInfinitePeriod() {
    JPPeriods jpPeriods = JPPeriods.get()
        .add(JPPeriod.get(null, LocalDate.of(2013, 10, 1)))
        .add(JPPeriod.get(LocalDate.of(2013, 9, 1), null));

    Collection<JPPeriod> periods = jpPeriods.getPeriod();

    assertEquals(1, periods.size());
    JPPeriod next = periods.iterator().next();
    assertNotNull(next);
    assertNull(next.getFrom());
    assertNull(next.getTo());

    jpPeriods = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2013, 1, 1), LocalDate.of(2013, 1, 31)))
        .add(JPPeriod.get(LocalDate.of(2013, 1, 31), LocalDate.of(2013, 2, 10)));

    periods = jpPeriods.getPeriod();
    assertEquals(1, periods.size());
  }

  @Test
  void shouldSplitOpenPeriods() {
    Collection<JPPeriod> result = JPPeriodUtils.split(Arrays.asList(
        JPPeriod.get(null, LocalDate.of(2020, 1, 1)),
        JPPeriod.get(LocalDate.of(2020, 1, 1), null)));
    assertEquals(3, result.size());
    Collection<JPPeriod> expectedPeriods = Arrays.asList(
        JPPeriod.get(null, LocalDate.of(2019, 12, 31)),
        JPPeriod.get(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1)),
        JPPeriod.get(LocalDate.of(2020, 1, 2), null)
    );
    assertTrue(CollectionUtils.isEqualCollection(expectedPeriods, result));
  }

  @Test
  void shouldSplitClosedPeriods() {
    Collection<JPPeriod> result = JPPeriodUtils.split(Arrays.asList(
        JPPeriod.get(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10)),
        JPPeriod.get(LocalDate.of(2020, 1, 3), LocalDate.of(2020, 1, 13)),
        JPPeriod.get(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1)),
        JPPeriod.get(LocalDate.of(2020, 1, 5), LocalDate.of(2020, 1, 15)))
    );
    assertEquals(6, result.size());
    Collection<JPPeriod> expectedPeriods = Arrays.asList(
        JPPeriod.get(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2)),
        JPPeriod.get(LocalDate.of(2020, 1, 3), LocalDate.of(2020, 1, 4)),
        JPPeriod.get(LocalDate.of(2020, 1, 5), LocalDate.of(2020, 1, 10)),
        JPPeriod.get(LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 13)),
        JPPeriod.get(LocalDate.of(2020, 1, 14), LocalDate.of(2020, 1, 15)),
        JPPeriod.get(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1))
    );
    assertTrue(CollectionUtils.isEqualCollection(expectedPeriods, result));
  }

  @Test
  void shouldReturnEmptyCollection_whenEmptyMinuendPassed() {
    assertTrue(JPPeriodUtils.subtract(Collections.emptyList(), null).isEmpty());
  }

  @Test
  void shouldReturnEmptyCollection_whenNullMinuendPassed() {
    assertTrue(JPPeriodUtils.subtract(Collections.emptyList(), null).isEmpty());
  }

  @Test
  void shouldReturnMinuend_whenEmptySubtractantPassed() {
    JPPeriods minuend = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)));
    Collection<JPPeriod> actual = JPPeriodUtils.subtract(minuend.getPeriod(), Collections.emptyList());
    assertFalse(actual.isEmpty());
    assertTrue(CollectionUtils.isEqualCollection(minuend.getPeriod(), actual));
  }

  @Test
  void shouldReturnSubtractant_whenNullSubtractantPassed() {
    JPPeriods minuend = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)));
    Collection<JPPeriod> actual = JPPeriodUtils.subtract(minuend, null);
    assertFalse(actual.isEmpty());
    assertTrue(CollectionUtils.isEqualCollection(minuend.getPeriod(), actual));
  }

  @Test
  void shouldSubtractCorrectly() {
    JPPeriods minuend = JPPeriods.get()
        .add(JPPeriod.get(null, LocalDate.of(1999, 10, 10)))
        .add(JPPeriod.get(LocalDate.of(1999, 11, 11), LocalDate.of(1999, 12, 12)))
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)))
        .add(JPPeriod.get(LocalDate.of(2001, 10, 10), null));

    JPPeriods sub = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(1999, 5, 5), LocalDate.of(1999, 6, 6)))
        .add(JPPeriod.get(LocalDate.of(1999, 7, 7), LocalDate.of(1999, 11, 11)))
        .add(JPPeriod.get(LocalDate.of(2000, 1, 2), LocalDate.of(2000, 1, 3)))
        .add(JPPeriod.get(LocalDate.of(2000, 1, 5), LocalDate.of(2000, 1, 6)));

    Collection<JPPeriod> actual = JPPeriodUtils.subtract(minuend, sub);
    assertFalse(CollectionUtils.isEmpty(actual));

    Collection<JPPeriod> expected = List.of(
        JPPeriod.get(null, LocalDate.of(1999, 5, 4)),
        JPPeriod.get(LocalDate.of(1999, 6, 7), LocalDate.of(1999, 7, 6)),
        JPPeriod.get(LocalDate.of(1999, 11, 12), LocalDate.of(1999, 12, 12)),
        JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 1)),
        JPPeriod.get(LocalDate.of(2000, 1, 4), LocalDate.of(2000, 1, 4)),
        JPPeriod.get(LocalDate.of(2000, 1, 7), LocalDate.of(2000, 1, 30)),
        JPPeriod.get(LocalDate.of(2001, 10, 10), null)
    );
    assertTrue(CollectionUtils.isEqualCollection(expected, actual));
  }

  @Test
  void shouldReturnEmptyCollection_whenOpenSubtractantPassed() {
    JPPeriods minuend = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)));

    JPPeriods sub = JPPeriods.get()
        .add(JPPeriod.get(null, null));

    assertTrue(JPPeriodUtils.subtract(minuend, sub).isEmpty());
  }

  @Test
  void shouldReturnEmptyCollection_whenOpenPeriodsPassed() {
    JPPeriods periods = JPPeriods.get()
        .add(JPPeriod.get(null, null));

    assertTrue(JPPeriodUtils.subtract(periods, periods).isEmpty());
  }

  @Test
  void shouldReturnEmptyCollection_whenSubtractantEqualsToMinuend() {
    JPPeriods periods = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)));

    assertTrue(JPPeriodUtils.subtract(periods, periods).isEmpty());
  }

  @Test
  void shouldReturnEmptyCollection_whenSubtractantCoversMinuend() {
    JPPeriods minuend = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 30)));

    JPPeriods sub = JPPeriods.get()
        .add(JPPeriod.get(LocalDate.of(1999, 1, 1), LocalDate.of(2001, 1, 30)));

    assertTrue(JPPeriodUtils.subtract(minuend, sub).isEmpty());
  }
}
