package mp.jprime.common.beans;

import mp.jprime.common.JPEnum;

/**
 * Бин описания значения
 */
public final class JPEnumBase implements JPEnum {
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

  private JPEnumBase(Object value, String description, String qName) {
    this.value = value;
    this.description = description;
    this.qName = qName;
  }

  public static JPEnum of(Object value, String description, String qName) {
    return new JPEnumBase(value, description, qName);
  }

  public static JPEnum of(Object value, String description) {
    return of(value, description, null);
  }
}
