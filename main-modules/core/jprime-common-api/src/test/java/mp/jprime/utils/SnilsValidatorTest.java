package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SnilsValidatorTest {

  @Test
  void validSnils() {
    Assertions.assertTrue(SnilsValidator.validate("18281866808"));
  }

  @Test
  void noValidSnils() {
    Assertions.assertFalse(SnilsValidator.validate("12345678901"));
  }

  @Test
  void snilsIsNull() {
    Assertions.assertFalse(SnilsValidator.validate(null));
  }

}
