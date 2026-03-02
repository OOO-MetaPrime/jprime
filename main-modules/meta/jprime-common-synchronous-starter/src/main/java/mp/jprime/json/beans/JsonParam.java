package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPParam;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.JsonJPMoney;
import mp.jprime.files.FileType;

import java.util.Collection;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonParam {
  /**
   * Код
   */
  private String code;
  /**
   * Тип
   */
  private String type;
  /**
   * Тип строкового поля
   */
  private String stringFormat;
  /**
   * Маска строкового поля
   */
  private String stringMask;
  /**
   * Тип целочисленного поля
   */
  private String integerFormat;
  /**
   * Признак многострочного строкового поля
   */
  private boolean multiline;
  /**
   * Расширения файлов для выбора
   */
  private Collection<String> fileTypes;
  /**
   * Длина для строковых полей
   */
  private Integer length;
  /**
   * Название
   */
  private String description;
  /**
   * QName
   */
  private String qName;
  /**
   * Признак обязательности
   */
  private boolean mandatory;
  /**
   * Значение по умолчанию
   */
  private Object value;
  /**
   * Разрешен множественный выбор
   */
  private boolean multiple;
  /**
   * Возможность внешнего использования параметра. Например, для ввода пользователем
   */
  private boolean external;
  /**
   * Класс из меты
   */
  private String refJpClass;
  /**
   * Атрибут класса
   */
  private String refJpAttr;
  /**
   * json для фильтрации объектов
   */
  private String refFilter;
  /**
   * Описание денежного типа
   */
  private JsonJPMoney money;
  /**
   * Перечислимые значения
   */
  private Collection<JsonJPEnum> enums;
  /**
   * Клиентский поиск
   */
  private boolean clientSearch;

  /**
   * Признак только для чтения
   */
  private boolean readOnly;

  public JsonParam() {

  }

  private JsonParam(String code, String type,
                    String stringFormat, String stringMask, String integerFormat, boolean multiline,
                    Collection<String> fileTypes, Integer length, String description, String qName,
                    boolean mandatory, Object value, boolean multiple, boolean external, String refJpClass,
                    String refJpAttr, String refFilter, JsonJPMoney money,
                    Collection<JsonJPEnum> enums, boolean clientSearch,
                    boolean readOnly) {
    this.code = code;
    this.type = type;
    this.stringFormat = stringFormat;
    this.stringMask = stringMask;
    this.integerFormat = integerFormat;
    this.multiline = multiline;
    this.fileTypes = fileTypes == null || fileTypes.isEmpty() ? null : fileTypes;
    this.length = length;
    this.description = description;
    this.qName = qName;
    this.mandatory = mandatory;
    this.value = value;
    this.multiple = multiple;
    this.external = external;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
    this.refFilter = refFilter == null || refFilter.isEmpty() ? null : refFilter;
    this.clientSearch = clientSearch;
    this.enums = enums == null || enums.isEmpty() ? null : enums;
    this.money = money;
    this.readOnly = readOnly;
  }

  public static JsonParam external(JPParam param) {
    if (param == null) {
      return null;
    }
    return from(param, true);
  }

  public static JsonParam from(JPParam param) {
    if (param == null) {
      return null;
    }
    return from(param, param.isExternal());
  }

  private static JsonParam from(JPParam param, boolean external) {
    JPType type = param.getType();
    Collection<JPEnum> enums = param.getEnums();

    return JsonParam.newBuilder()
        .code(param.getCode())
        .type(JPType.getCode(type))
        .stringFormat(param.getStringFormat() != null ? param.getStringFormat().getCode() : null)
        .stringMask(param.getStringMask())
        .integerFormat(param.getIntegerFormat() != null ? param.getIntegerFormat().getCode() : null)
        .multiline(param.isMultiline())
        .fileTypes(param.getFileTypes())
        .length(param.getLength())
        .description(param.getDescription())
        .qName(param.getQName())
        .mandatory(param.isMandatory())
        .multiple(param.isMultiple())
        .external(external)
        .value(param.getValue())
        .refJpClass(param.getRefJpClassCode())
        .refJpAttr(param.getRefJpAttrCode())
        .refFilter(param.getRefFilter())
        .clientSearch(param.isClientSearch())
        .money(type == JPType.MONEY ? JsonJPMoney.toJson(param.getMoney()) : null)
        .enums(enums == null ? null : enums.stream()
            .map(y -> JsonJPEnum.of(y.getValue(), y.getDescription(), y.getQName()))
            .collect(Collectors.toList())
        )
        .readOnly(param.isReadOnly())
        .build();
  }

  public String getCode() {
    return code;
  }

  public String getType() {
    return type;
  }

  public String getStringFormat() {
    return stringFormat;
  }

  public String getStringMask() {
    return stringMask;
  }

  public String getIntegerFormat() {
    return integerFormat;
  }

  public boolean isMultiline() {
    return multiline;
  }

  public Collection<String> getFileTypes() {
    return fileTypes;
  }

  public Integer getLength() {
    return length;
  }

  public String getDescription() {
    return description;
  }

  public String getqName() {
    return qName;
  }

  /**
   * Должен ли параметр быть передан в POST-запросе
   */
  public boolean isMandatory() {
    return mandatory;
  }

  public Object getValue() {
    return value;
  }

  /**
   * Разрешен множественный выбор
   */
  public boolean isMultiple() {
    return multiple;
  }

  /**
   * Возможность внешнего использования параметра. Например, для ввода пользователем
   *
   * @return Да/Нет
   */
  public boolean isExternal() {
    return external;
  }

  public String getRefJpClass() {
    return refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public String getRefFilter() {
    return refFilter;
  }

  public JsonJPMoney getMoney() {
    return money;
  }

  public Collection<JsonJPEnum> getEnums() {
    return enums;
  }

  public boolean isClientSearch() {
    return clientSearch;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String type;
    private String stringFormat;
    private String stringMask;
    private String integerFormat;
    private boolean multiline;
    private Collection<String> fileTypes;
    private Integer length;
    private String description;
    private String qName;
    private boolean mandatory;
    private Object value;
    private boolean multiple;
    private boolean external = true;
    private String refJpClass;
    private String refJpAttr;
    private String refFilter;
    private JsonJPMoney money;
    private Collection<JsonJPEnum> enums;
    private boolean clientSearch;
    private boolean readOnly;

    private Builder() {
    }

    public JsonParam build() {
      return new JsonParam(code, type, stringFormat, stringMask, integerFormat, multiline,
          fileTypes, length, description, qName, mandatory, value, multiple, external,
          refJpClass, refJpAttr, refFilter, money,
          enums, clientSearch, readOnly);
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder type(JPType type) {
      this.type = type != null ? type.getCode() : null;
      return this;
    }

    public Builder stringFormat(String stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
      return this;
    }

    public Builder integerFormat(String integerFormat) {
      this.integerFormat = integerFormat;
      return this;
    }

    public Builder multiline(boolean multiline) {
      this.multiline = multiline;
      return this;
    }

    public Builder fileTypes(Collection<FileType> fileTypes) {
      this.fileTypes = fileTypes.stream()
          .map(FileType::getExt)
          .collect(Collectors.toList());
      return this;
    }

    public Builder length(Integer length) {
      this.length = length;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    public Builder value(Object value) {
      this.value = value;
      return this;
    }

    public Builder multiple(boolean multiple) {
      this.multiple = multiple;
      return this;
    }

    public Builder external(boolean external) {
      this.external = external;
      return this;
    }

    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    public Builder refFilter(String refFilter) {
      this.refFilter = refFilter;
      return this;
    }

    public Builder money(JsonJPMoney money) {
      this.money = money;
      return this;
    }

    public Builder enums(Collection<JsonJPEnum> enums) {
      this.enums = enums;
      return this;
    }

    public Builder clientSearch(boolean clientSearch) {
      this.clientSearch = clientSearch;
      return this;
    }

    public Builder readOnly(boolean readOnly) {
      this.readOnly = readOnly;
      return this;
    }
  }
}
