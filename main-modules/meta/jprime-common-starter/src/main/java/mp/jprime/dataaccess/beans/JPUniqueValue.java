package mp.jprime.dataaccess.beans;

import java.util.*;
import java.util.stream.Stream;

public final class JPUniqueValue {
  private final String attr;
  private final Object value;

  private final Collection<JPUniqueValue> subValues;

  private JPUniqueValue(String attr, Object value, Collection<JPUniqueValue> subValues) {
    this.attr = attr;
    this.value = value;
    this.subValues = subValues == null ? Collections.emptyList() : Collections.unmodifiableCollection(subValues);
  }

  public static JPUniqueValue of(String attr, Object value) {
    return new JPUniqueValue(attr, value, null);
  }

  public static JPUniqueValue of(String attr, Object value, Collection<JPUniqueValue> subValue) {
    return new JPUniqueValue(attr, value, subValue);
  }


  public String getAttr() {
    return attr;
  }

  public Object getValue() {
    return value;
  }

  public Collection<JPUniqueValue> getSubValues() {
    return subValues;
  }

  /**
   * Объединяет два массива
   *
   * @param values1 Массив 1
   * @param values2 Массив2
   * @return Объединенный массив
   */
  public static Collection<JPUniqueValue> append(Collection<JPUniqueValue> values1, Collection<JPUniqueValue> values2) {
    if (values1 == null && values2 == null) {
      return Collections.emptyList();
    }
    if (values1 == null || values1.isEmpty()) {
      return new ArrayList<>(values2);
    }
    if (values2 == null || values2.isEmpty()) {
      return new ArrayList<>(values1);
    }
    Map<Object, JPUniqueValue> result = new HashMap<>();
    Stream.concat(values1.stream(), values2.stream())
        .forEach(newV -> {
          JPUniqueValue oldV = result.get(newV.getValue());
          if (oldV == null) {
            result.put(newV.getValue(), newV);
          } else {
            result.put(newV.getValue(),
                JPUniqueValue.of(oldV.attr, oldV.value, append(oldV.getSubValues(), newV.getSubValues()))
            );
          }
        });
    return result.values();
  }
}
