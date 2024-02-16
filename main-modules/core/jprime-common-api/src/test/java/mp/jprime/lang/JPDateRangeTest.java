package mp.jprime.lang;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JPDateRangeTest {

  @Test
  void shouldBeClosed() {
    LocalDate lower = LocalDate.now();
    LocalDate upper = lower.plusDays(1);
    Stream.of(
            JPDateRange.create(lower, upper),
            JPDateRange.create(lower, upper, true, true),
            JPDateRange.closed(lower, upper)
        )
        .forEach(range -> {
          assertTrue(range.isLowerBoundClosed());
          assertTrue(range.isUpperBoundClosed());
        });
  }

  @Test
  void shouldBeOpen() {
    LocalDate lower = LocalDate.now();
    LocalDate upper = lower.plusDays(1);
    Stream.of(
            JPDateRange.create(null, null),
            JPDateRange.create(lower, upper, false, false),
            JPDateRange.infinite()
        )
        .forEach(range -> {
          assertFalse(range.isLowerBoundClosed());
          assertFalse(range.isUpperBoundClosed());
        });
  }

  @Test
  void shouldBeClosedOpen() {
    LocalDate lower = LocalDate.now();
    Stream.of(
            JPDateRange.create(lower, null),
            JPDateRange.create(lower, lower.plusMonths(1), true, false),
            JPDateRange.closedInfinite(lower)
        )
        .forEach(range -> {
          assertTrue(range.isLowerBoundClosed());
          assertFalse(range.isUpperBoundClosed());
        });
  }

  @Test
  void shouldBeOpenClosed() {
    LocalDate upper = LocalDate.now();
    Stream.of(
            JPDateRange.create(null, upper),
            JPDateRange.create(upper.minusMonths(1), upper, false, true),
            JPDateRange.infiniteClosed(upper)
        )
        .forEach(range -> {
          assertFalse(range.isLowerBoundClosed());
          assertTrue(range.isUpperBoundClosed());
        });
  }

  @ParameterizedTest
  @MethodSource("getContained")
  void contains_shouldReturnTrue(Pair<JPDateRange, JPDateRange> pair) {
    assertTrue(pair.getLeft().contains(pair.getRight()));
  }

  @ParameterizedTest
  @MethodSource("getNotContained")
  void contains_shouldReturnFalse(Pair<JPDateRange, JPDateRange> pair) {
    assertFalse(pair.getLeft().contains(pair.getRight()));
  }

  static Stream<Pair<JPDateRange, JPDateRange>> getContained() {
    LocalDate now = LocalDate.now();
    return Stream.of(
        Pair.of(JPDateRange.create(now, now.plusMonths(10)), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(2), now.minusMonths(1))),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(null, now.minusMonths(1))),
        Pair.of(JPDateRange.create(now, null), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),
        Pair.of(JPDateRange.create(now, null), JPDateRange.create(now.plusMonths(1), null)),
        Pair.of(JPDateRange.create(now, now.plusMonths(10)), JPDateRange.create(now, now.plusMonths(10))),
        Pair.of(JPDateRange.create(now, now.plusMonths(10), false, false), JPDateRange.create(now, now.plusMonths(10), false, false)),
        Pair.of(JPDateRange.infinite(), JPDateRange.create(now, now.plusMonths(10), false, false)),
        Pair.of(JPDateRange.infinite(), JPDateRange.create(now, now.plusMonths(10)))
    );
  }

  static Stream<Pair<JPDateRange, JPDateRange>> getNotContained() {
    LocalDate now = LocalDate.now();
    return Stream.of(
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(1), null)),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(1), now.plusMonths(1))),
        Pair.of(JPDateRange.create(now.minusMonths(2), now), JPDateRange.create(now.minusMonths(1), null)),
        Pair.of(JPDateRange.create(now.minusMonths(2), now), JPDateRange.create(now.minusMonths(1), now.plusMonths(1))),

        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.plusMonths(1), null)),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),
        Pair.of(JPDateRange.create(now.minusMonths(2), now), JPDateRange.create(now.plusMonths(1), null)),
        Pair.of(JPDateRange.create(now.minusMonths(2), now), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),

        Pair.of(JPDateRange.create(now, now.plusMonths(10), false, false), JPDateRange.create(now, now.plusMonths(10), true, true)),
        Pair.of(JPDateRange.create(now, now.plusMonths(10), false, false), JPDateRange.create(now, now.plusMonths(10), false, true)),
        Pair.of(JPDateRange.create(now, now.plusMonths(10), false, false), JPDateRange.create(now, now.plusMonths(10), true, false)),

        //Обратные getContained()
        Pair.of(JPDateRange.create(now.plusMonths(1), now.plusMonths(2)), JPDateRange.create(now, now.plusMonths(10))),
        Pair.of(JPDateRange.create(now.minusMonths(2), now.minusMonths(1)), JPDateRange.create(null, now)),
        Pair.of(JPDateRange.create(null, now.minusMonths(1)), JPDateRange.create(null, now)),
        Pair.of(JPDateRange.create(now.plusMonths(1), now.plusMonths(2)), JPDateRange.create(now, null)),
        Pair.of(JPDateRange.create(now.plusMonths(1), null), JPDateRange.create(now, null))
    );
  }

  @ParameterizedTest
  @MethodSource("getIntersected")
  void intersection_shouldReturnTrueSymmetrically_whenIntersected(Pair<JPDateRange, JPDateRange> pair) {
    assertTrue(pair.getLeft().intersection(pair.getRight()), "Некорректный результат прямой проверки пересечения " + pair.getLeft() + " и " + pair.getRight());
    assertTrue(pair.getRight().intersection(pair.getLeft()), "Несимметричный результат для " + pair.getRight() + " и " + pair.getLeft());
  }

  @ParameterizedTest
  @MethodSource("getNotIntersected")
  void intersection_shouldReturnFalseSymmetrically_whenNotIntersected(Pair<JPDateRange, JPDateRange> pair) {
    assertFalse(pair.getLeft().intersection(pair.getRight()), "Некорректный результат прямой проверки пересечения " + pair.getLeft() + " и " + pair.getRight());
    assertFalse(pair.getRight().intersection(pair.getLeft()), "Несимметричный результат для " + pair.getRight() + " и " + pair.getLeft());
  }

  static Stream<Pair<JPDateRange, JPDateRange>> getNotIntersected() {
    LocalDate now = LocalDate.now();
    return Stream.of(
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.plusMonths(1), null)),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),
        Pair.of(JPDateRange.create(now.minusMonths(1), now), JPDateRange.create(now.plusMonths(1), now.plusMonths(2)))
    );
  }

  static Stream<Pair<JPDateRange, JPDateRange>> getIntersected() {
    LocalDate now = LocalDate.now();
    return Stream.of(
        Pair.of(JPDateRange.create(null, null), JPDateRange.create(null, null)),

        Pair.of(JPDateRange.create(now, null), JPDateRange.create(null, null)),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(null, null)),
        Pair.of(JPDateRange.create(now, now.plusMonths(2)), JPDateRange.create(null, null)),

        Pair.of(JPDateRange.create(now, null), JPDateRange.create(now.plusMonths(1), null)),
        Pair.of(JPDateRange.create(now, null), JPDateRange.create(null, now.plusMonths(1))),
        Pair.of(JPDateRange.create(now, null), JPDateRange.create(now.plusMonths(1), now.plusMonths(2))),
        Pair.of(JPDateRange.create(now, null), JPDateRange.create(now.minusMonths(1), now.plusMonths(1))),

        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(1), null)),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(null, now.plusMonths(1))),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(1), now.plusMonths(1))),
        Pair.of(JPDateRange.create(null, now), JPDateRange.create(now.minusMonths(2), now.minusMonths(1))),

        Pair.of(JPDateRange.create(now, now), JPDateRange.create(now, now)),
        Pair.of(JPDateRange.create(now, now.plusMonths(3)), JPDateRange.create(now.plusMonths(1), now.plusMonths(4))),
        Pair.of(JPDateRange.create(now, now.plusMonths(3)), JPDateRange.create(now.plusMonths(1), now.plusMonths(2)))
    );
  }
}