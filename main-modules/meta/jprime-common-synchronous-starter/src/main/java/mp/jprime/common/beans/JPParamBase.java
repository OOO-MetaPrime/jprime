package mp.jprime.common.beans;

import mp.jprime.common.JPEnum;
import mp.jprime.common.JPParam;
import mp.jprime.files.FileType;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.JPMoney;
import mp.jprime.meta.beans.JPType;

import java.util.Collection;
import java.util.Collections;

/**
 * Базовый бин параметра
 */
public class JPParamBase implements JPParam {
  /**
   * Код
   */
  private final String code;
  /**
   * Тип
   */
  private final JPType type;
  /**
   * Тип строкового поля
   */
  private final JPStringFormat stringFormat;
  /**
   * Маска строкового поля
   */
  private final String stringMask;
  /**
   * Расширения файлов для выбора
   */
  private final Collection<FileType> fileTypes;
  /**
   * Длина (для строковых полей)
   */
  private final Integer length;
  /**
   * Название
   */
  private final String description;
  /**
   * QName
   */
  private final String qName;
  /**
   * Признак обязательности
   */
  private final boolean mandatory;
  /**
   * Разрешен ли множественный выбор
   */
  private final boolean multiple;
  /**
   * Класс из меты
   */
  private final String refJpClass;
  /**
   * Атрибут класса
   */
  private final String refJpAttr;
  /**
   * json для фильтрации объектов
   */
  private final String refFilter;
  /**
   * Описание денежного типа
   */
  private final JPMoney money;
  /**
   * Возможность внешнего переопределения параметра. Например, для ввода пользователем
   */
  private final boolean external;
  /**
   * Значение параметра
   */
  private final Object value;
  /**
   * Перечислимые значения
   */
  private final Collection<JPEnum> enums;
  /**
   * Клиентский поиск
   */
  private final boolean clientSearch;
  /**
   * Признак логирования значения
   */
  private final boolean actionLog;
  /**
   * Признак только для чтения
   */
  private final boolean readOnly;

  /**
   * @param code         Название (Код) параметра
   * @param type         Тип параметра
   * @param stringFormat Тип строкового поля
   * @param stringMask   Маска строкового поля
   * @param fileTypes    Расширения файлов для выбора
   * @param length       Длина (для строковых полей)
   * @param description  Наименование
   * @param qName        QName
   * @param mandatory    Признак обязательности
   * @param multiple     Признак множественного выбора
   * @param refJpClass   Класс из меты
   * @param refJpAttr    Атрибут класса
   * @param refFilter    JSON фильтрации объектов
   * @param money        Описание денежного типа
   * @param external     Возможность внешнего переопределения параметра
   * @param value        Значение параметра
   * @param enums        Перечислимые значения
   * @param clientSearch Клиентский поиск
   * @param actionLog    Признак логирования значения
   * @param readOnly     Признак только для чтения
   */
  protected JPParamBase(String code, JPType type, JPStringFormat stringFormat, String stringMask,
                        Collection<FileType> fileTypes, Integer length, String description, String qName,
                        boolean mandatory, boolean multiple, String refJpClass, String refJpAttr, String refFilter,
                        JPMoney money,
                        boolean external, Object value, Collection<JPEnum> enums, boolean clientSearch, boolean actionLog,
                        boolean readOnly) {
    this.code = code;
    this.type = type;
    this.stringFormat = stringFormat;
    this.stringMask = stringMask;
    this.fileTypes = fileTypes != null && !fileTypes.isEmpty() ? Collections.unmodifiableCollection(fileTypes) : Collections.emptyList();
    this.length = length != null && length > 0 ? length : null;
    this.description = description;
    this.qName = qName;
    this.mandatory = mandatory;
    this.multiple = multiple;
    this.refJpClass = refJpClass != null && !refJpClass.isEmpty() ? refJpClass : null;
    this.refJpAttr = refJpAttr != null && !refJpAttr.isEmpty() ? refJpAttr : null;
    this.refFilter = refFilter != null && !refFilter.isEmpty() ? refFilter : null;
    this.money = money;
    this.external = external;
    this.value = value;
    this.enums = enums != null && !enums.isEmpty() ? Collections.unmodifiableCollection(enums) : Collections.emptyList();
    this.clientSearch = clientSearch;
    this.actionLog = actionLog;
    this.readOnly = readOnly;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public JPType getType() {
    return type;
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
  public Collection<FileType> getFileTypes() {
    return fileTypes;
  }

  @Override
  public Integer getLength() {
    return length;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getQName() {
    return qName;
  }

  @Override
  public String getRefJpClassCode() {
    return refJpClass;
  }

  @Override
  public String getRefJpAttrCode() {
    return refJpAttr;
  }

  @Override
  public String getRefFilter() {
    return refFilter;
  }

  @Override
  public JPMoney getMoney() {
    return money;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public boolean isMultiple() {
    return multiple;
  }

  @Override
  public boolean isExternal() {
    return external;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Collection<JPEnum> getEnums() {
    return enums;
  }

  @Override
  public boolean isClientSearch() {
    return clientSearch;
  }

  @Override
  public boolean isActionLog() {
    return actionLog;
  }

  @Override
  public boolean isReadOnly() {
    return readOnly;
  }

  public static Builder newBuilder() {
    return new Builder<>();
  }

  public static class Builder<T extends Builder> {
    protected String code;
    protected JPType type;
    protected JPStringFormat stringFormat;
    protected String stringMask;
    protected Collection<FileType> fileTypes;
    protected Integer length;
    protected String description;
    protected String qName;
    protected boolean mandatory;
    protected boolean multiple;
    protected String refJpClass;
    protected String refJpAttr;
    protected String refFilter;
    protected JPMoney money;
    protected boolean external;
    protected Object value;
    protected Collection<JPEnum> enums;
    protected boolean clientSearch;
    protected boolean actionLog;
    protected boolean readOnly;

    protected Builder() {
    }

    public JPParamBase build() {
      return new JPParamBase(code, type, stringFormat, stringMask,
          fileTypes, length, description, qName, mandatory, multiple,
          refJpClass, refJpAttr, refFilter, money, external, value, enums, clientSearch, actionLog, readOnly);
    }

    public T enums(Collection<JPEnum> enums) {
      this.enums = enums;
      return (T) this;
    }

    public T code(String code) {
      this.code = code;
      return (T) this;
    }

    public T type(JPType type) {
      this.type = type;
      return (T) this;
    }

    public T stringFormat(JPStringFormat stringFormat) {
      this.stringFormat = stringFormat;
      return (T) this;
    }

    public T stringMask(String stringMask) {
      this.stringMask = stringMask;
      return (T) this;
    }

    public T fileTypes(Collection<FileType> fileTypes) {
      this.fileTypes = fileTypes;
      return (T) this;
    }

    public T length(Integer length) {
      this.length = length;
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

    public T mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return (T) this;
    }

    public T multiple(boolean multiple) {
      this.multiple = multiple;
      return (T) this;
    }

    public T refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return (T) this;
    }

    public T refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return (T) this;
    }

    public T refFilter(String refFilter) {
      this.refFilter = refFilter;
      return (T) this;
    }

    public T money(JPMoney money) {
      this.money = money;
      return (T) this;
    }

    public T external(boolean external) {
      this.external = external;
      return (T) this;
    }

    public T value(Object value) {
      this.value = value;
      return (T) this;
    }

    public T clientSearch(boolean clientSearch) {
      this.clientSearch = clientSearch;
      return (T) this;
    }

    public T actionLog(boolean actionLog) {
      this.actionLog = actionLog;
      return (T) this;
    }

    public T readOnly(boolean readOnly) {
      this.readOnly = readOnly;
      return (T) this;
    }
  }
}
