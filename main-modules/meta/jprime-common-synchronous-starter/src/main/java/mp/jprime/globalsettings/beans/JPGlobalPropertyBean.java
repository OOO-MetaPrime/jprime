package mp.jprime.globalsettings.beans;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.globalsettings.JPGlobalProperty;

import java.util.Collection;
import java.util.Collections;

/**
 * Описание глобальной настройки
 */
public final class JPGlobalPropertyBean implements JPGlobalProperty {
  private final String code;
  private final String name;
  private final String description;
  private final JPPropertyType type;
  private final boolean mandatory;
  private final boolean readonly;
  private final Integer min;
  private final Integer max;
  private final JPStringFormat stringFormat;
  private final String stringMask;
  private final Collection<Enum> enums;

  private JPGlobalPropertyBean(String code, String name, String description, JPPropertyType type,
                               boolean mandatory, boolean readonly, Integer min, Integer max,
                               JPStringFormat stringFormat, String stringMask,
                               Collection<Enum> enums) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.type = type;
    this.mandatory = mandatory;
    this.readonly = readonly;
    this.min = min;
    this.max = max;
    this.stringFormat = stringFormat;
    this.stringMask = stringMask;
    this.enums = enums != null ? enums : Collections.emptyList();
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public JPPropertyType getType() {
    return type;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public boolean isReadonly() {
    return readonly;
  }

  @Override
  public Integer getMin() {
    return min;
  }

  @Override
  public Integer getMax() {
    return max;
  }

  @Override
  public JPStringFormat getStringFormat() {
    return stringFormat;
  }

  @Override
  public String getStringMask() {
    return stringMask;
  }

  @Override
  public Collection<Enum> getEnums() {
    return enums;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String name;
    private String description;
    private JPPropertyType type;
    private boolean mandatory;
    private boolean readonly;
    private Integer min;
    private Integer max;
    private JPStringFormat stringFormat;
    private String stringMask;
    private Collection<Enum> enums;

    private Builder() {
    }

    /**
     * Кодовое имя свойства
     *
     * @return Кодовое имя свойства
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Название свойства
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Описание свойства
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Тип свойства
     */
    public Builder type(JPPropertyType type) {
      this.type = type;
      return this;
    }

    /**
     * Признак обязательности
     */
    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    /**
     * Признак только на чтение
     */
    public Builder readonly(boolean readonly) {
      this.readonly = readonly;
      return this;
    }

    /**
     * Минимальное значение
     */
    public Builder min(Integer min) {
      this.min = min;
      return this;
    }

    /**
     * Максимальное значение
     */
    public Builder max(Integer max) {
      this.max = max;
      return this;
    }

    /**
     * Тип строкового поля
     *
     * @param stringFormat Тип строкового поля
     * @return Builder
     */
    public Builder stringFormat(JPStringFormat stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    /**
     * Маска строкового поля
     *
     * @param stringMask Маска строкового поля
     * @return Builder
     */
    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
      return this;
    }

    /**
     * Перечислимые значения
     */
    public Builder enums(Collection<Enum> enums) {
      this.enums = enums;
      return this;
    }

    public JPGlobalPropertyBean build() {
      return new JPGlobalPropertyBean(
          code, name, description, type,
          mandatory, readonly, min, max, stringFormat, stringMask, enums
      );
    }
  }

  /**
   * Перечислимое значение
   */
  public static class EnumBean implements Enum {
    private final Object value;
    private final String name;

    private EnumBean(Object value, String name) {
      this.value = value;
      this.name = name;
    }

    public static Enum of(Object value, String name) {
      return new EnumBean(value, name);
    }

    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public String getName() {
      return name;
    }
  }
}
