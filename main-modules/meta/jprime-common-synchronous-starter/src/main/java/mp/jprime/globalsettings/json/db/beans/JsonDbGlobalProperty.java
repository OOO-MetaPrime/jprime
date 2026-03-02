package mp.jprime.globalsettings.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.beans.JPPropertyType;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.globalsettings.JPGlobalProperty;
import mp.jprime.globalsettings.beans.JPGlobalPropertyBean;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDbGlobalProperty {
  private String code;
  private String name;
  private String description;
  private boolean mandatory;
  private boolean readonly;
  private String type;
  private Integer min;
  private Integer max;
  private String stringFormat;
  private String stringMask;
  private Collection<Enum> enums;

  public JsonDbGlobalProperty() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public boolean isReadonly() {
    return readonly;
  }

  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer min) {
    this.min = min;
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }

  public String getStringFormat() {
    return stringFormat;
  }

  public void setStringFormat(String stringFormat) {
    this.stringFormat = stringFormat;
  }

  public String getStringMask() {
    return stringMask;
  }

  public void setStringMask(String stringMask) {
    this.stringMask = stringMask;
  }

  public Collection<Enum> getEnums() {
    return enums;
  }

  public void setEnums(Collection<Enum> enums) {
    this.enums = enums;
  }

  public static JPGlobalProperty toJPGlobalProperty(JsonDbGlobalProperty json) {
    if (json == null) {
      return null;
    }

    Collection<JsonDbGlobalProperty.Enum> enums = json.getEnums();

    return JPGlobalPropertyBean.builder()
        .code(json.getCode())
        .name(json.getName())
        .description(json.getDescription())
        .type(JPPropertyType.getType(json.getType()))
        .mandatory(json.isMandatory())
        .readonly(json.isReadonly())
        .min(json.getMin())
        .max(json.getMax())
        .stringFormat(JPStringFormat.getType(json.getStringFormat()))
        .stringMask(json.getStringMask())
        .enums(
            enums == null ? null : enums.stream()
                .map(x -> JPGlobalPropertyBean.EnumBean.of(x.getValue(), x.getName()))
                .toList()
        )
        .build();
  }

  /**
   * Перечислимое значение
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Enum {
    private Object value;
    private String name;

    public Enum() {

    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
