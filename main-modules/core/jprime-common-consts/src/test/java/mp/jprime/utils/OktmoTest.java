package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class OktmoTest {
  @Test
  void test() {
    Collection<String> oktmo1 = List.of("00000000");
    Assertions.assertIterableEquals(Oktmo.getPrefix(oktmo1), List.of(""));
    Assertions.assertIterableEquals(Oktmo.getHierarchy(oktmo1), List.of("00000000"));

    Collection<String> oktmo2 = List.of("75000000");
    Assertions.assertIterableEquals(Oktmo.getPrefix(oktmo2), List.of("75"));
    Assertions.assertIterableEquals(Oktmo.getHierarchy(oktmo2), List.of("75000000", "00000000"));

    Collection<String> oktmo3 = List.of("75111000");
    Assertions.assertIterableEquals(Oktmo.getPrefix(oktmo3), List.of("75111"));
    Assertions.assertIterableEquals(Oktmo.getHierarchy(oktmo3), List.of("75000000", "00000000", "75111000"));
  }
}
