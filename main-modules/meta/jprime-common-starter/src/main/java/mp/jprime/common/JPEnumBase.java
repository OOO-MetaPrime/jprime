package mp.jprime.common;

/**
 * Бин описания значения
 */
public class JPEnumBase implements JPEnum {
  private final Object value;
  private final String description;
  private final String qName;

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getQName() {
    return qName;
  }

  protected JPEnumBase(Object value, String description, String qName) {
    this.value = value;
    this.description = description;
    this.qName = qName;
  }

  public static Builder newBuilder() {
    return new Builder<>();
  }

  protected static class Builder<T extends JPEnumBase.Builder> {
    protected Object value;
    protected String description;
    protected String qName;

    protected Builder() {
    }

    public JPEnumBase build() {
      return new JPEnumBase(value, description, qName);
    }

    public T value(Object value) {
      this.value = value;
      return (T) this;
    }

    public T description(String description) {
      this.description = description;
      return (T) this;
    }

    public T qName(String qName) {
      this.qName = qName;
      return (T) this;
    }
  }
}
