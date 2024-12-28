package mp.jprime.dataaccess.beans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
public class JPUniqueValueTest {

  @Test
  void testEmpty() {
    Collection<JPUniqueValue> values1 = null;
    Collection<JPUniqueValue> values2 = null;
    assertEquals(0, JPUniqueValue.append(values1, values2).size());
  }

  @Test
  void testFlat() {
    Collection<JPUniqueValue> values1 = List.of(
        JPUniqueValue.of("a1", 1, List.of(
            JPUniqueValue.of("a2", 1),
            JPUniqueValue.of("a2", 2)
        )),
        JPUniqueValue.of("a1", 2, List.of(
            JPUniqueValue.of("a2", 2),
            JPUniqueValue.of("a2", 3)
        ))
    );
    Collection<JPUniqueValue> values2 = List.of(
        JPUniqueValue.of("a1", 2, List.of(
            JPUniqueValue.of("a2", 3),
            JPUniqueValue.of("a2", 4)
        )),
        JPUniqueValue.of("a1", 3)
    );
    assertEquals(3, JPUniqueValue.append(values1, values2).size());
  }

  @Test
  void testTree() {
    Collection<JPUniqueValue> values1 = List.of(
        JPUniqueValue.of("a1", 1),
        JPUniqueValue.of("a1", 2)
    );
    Collection<JPUniqueValue> values2 = List.of(
        JPUniqueValue.of("a1", 2),
        JPUniqueValue.of("a1", 3)
    );
    assertEquals(3, JPUniqueValue.append(values1, values2).size());
  }
}
