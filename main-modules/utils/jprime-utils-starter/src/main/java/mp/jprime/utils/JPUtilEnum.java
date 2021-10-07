package mp.jprime.utils;

import mp.jprime.common.JPEnumBase;

/**
 * Бин перечислимого для параметра утилиты
 */
public class JPUtilEnum extends JPEnumBase {
  private JPUtilEnum(Object value, String description, String qName) {
    super(value, description, qName);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends mp.jprime.common.JPEnumBase.Builder<Builder> {
    private Builder() {
      super();
    }

    public JPUtilEnum build() {
      return new JPUtilEnum(value, description, qName);
    }
  }
}
