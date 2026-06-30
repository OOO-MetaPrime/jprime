package mp.jprime.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OktmoTest {
  @Test
  void test() {
    Collection<String> oktmo1 = List.of("00000000");
    assertIterableEquals(Oktmo.getPrefix(oktmo1), List.of(""));
    assertIterableEquals(Oktmo.getHierarchy(oktmo1), List.of("00000000"));

    Collection<String> oktmo2 = List.of("75000000");
    assertIterableEquals(Oktmo.getPrefix(oktmo2), List.of("75"));
    assertIterableEquals(Oktmo.getHierarchy(oktmo2), List.of("75000000", "00000000"));

    Collection<String> oktmo3 = List.of("75111000");
    assertIterableEquals(Oktmo.getPrefix(oktmo3), List.of("75111"));
    assertIterableEquals(Oktmo.getHierarchy(oktmo3), List.of("75000000", "00000000", "75111000", "75100000"));

    Collection<String> oktmo4 = List.of("05600000");
    assertIterableEquals(Oktmo.getPrefix(oktmo4), List.of("056"));
    assertIterableEquals(Oktmo.getHierarchy(oktmo4), List.of("05000000", "00000000", "05600000"));

    Collection<String> oktmoTree = Oktmo.getOktmoTreeList(List.of("05600000"));
    assertIterableEquals(oktmoTree, List.of("056", "05000000", "00000000", "05600000"));
    assertTrue(Oktmo.isCheck("05607000", oktmoTree));

    oktmoTree = Oktmo.getOktmoTreeList(List.of("36701000"));
    assertIterableEquals(oktmoTree, List.of("36000000", "00000000", "36701000", "36701", "36700000"));
    assertTrue(Oktmo.isCheck("36000000", oktmoTree));

    oktmoTree = Oktmo.getOktmoTreeList(List.of("03601400"));
    assertIterableEquals(oktmoTree, List.of("036014", "03601400", "00000000", "03000000", "03601000", "03600000"));

    oktmoTree = Oktmo.getOktmoTreeList(List.of("03601403"));
    assertIterableEquals(oktmoTree, List.of("03601403", "03601400", "00000000", "03000000", "03601000", "03600000"));
  }
}
