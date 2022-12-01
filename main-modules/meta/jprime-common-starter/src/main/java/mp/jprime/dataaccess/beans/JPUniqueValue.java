package mp.jprime.dataaccess.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class JPUniqueValue {
  private final String attr;
  private final Object value;

  private final Collection<JPUniqueValue> subValues;

  private JPUniqueValue(String attr, Object value, Collection<JPUniqueValue> subValues) {
    this.attr = attr;
    this.value = value;
    this.subValues = Collections.unmodifiableCollection(subValues);
  }

  public static Builder newBuilder() {
    return new Builder();
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

  public static class Builder {
    private String attr;
    private Object value;

    private Collection<JPUniqueValue> subValues = new ArrayList<>();

    public Builder attr(String attr) {
      this.attr = attr;
      return this;
    }

    public Builder value(Object value) {
      this.value = value;
      return this;
    }

    public Builder subValues(Collection<JPUniqueValue> subValues) {
      this.subValues = subValues;
      return this;
    }

    public JPUniqueValue build() {
      return new JPUniqueValue(attr, value, subValues);
    }
  }

}
