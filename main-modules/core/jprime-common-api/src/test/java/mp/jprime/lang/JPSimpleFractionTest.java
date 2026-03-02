package mp.jprime.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JPSimpleFractionTest {
  @Test
  void test() {
    JPSimpleFraction sf1 = JPSimpleFraction.of(2,1,3);

    assertEquals(2, sf1.getInteger());
    assertEquals(1, sf1.getNumerator());
    assertEquals(3, sf1.getDenominator());

    JPSimpleFraction sf2 = JPSimpleFraction.of(0,1,3);
    assertEquals(0, sf2.getInteger());
    assertEquals(1, sf2.getNumerator());
    assertEquals(3, sf2.getDenominator());
  }
}
