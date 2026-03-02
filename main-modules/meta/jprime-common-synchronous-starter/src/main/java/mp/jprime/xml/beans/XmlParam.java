package mp.jprime.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import mp.jprime.common.JPEnum;
import mp.jprime.common.JPParam;
import mp.jprime.common.beans.JPCommonParam;
import mp.jprime.formats.JPIntegerFormat;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.beans.JPType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParam {
  private Boolean external;
  private String code;
  private String description;
  private String value;
  private String type;
  private Integer length;
  private String stringFormat;
  private String integerFormat;
  private String qName;
  private boolean multiple;
  private boolean mandatory;
  private String refJpClass;
  private String refJpAttr;
  private String refFilter;
  @JacksonXmlProperty(localName = "paramEnums")
  private XmlParamEnums xmlParamEnums;
  private boolean clientSearch;

  public Boolean getExternal() {
    return external;
  }

  public void setExternal(Boolean external) {
    this.external = external;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public String getStringFormat() {
    return stringFormat;
  }

  public void setStringFormat(String stringFormat) {
    this.stringFormat = stringFormat;
  }

  public String getIntegerFormat() {
    return integerFormat;
  }

  public void setIntegerFormat(String integerFormat) {
    this.integerFormat = integerFormat;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public String getRefJpClass() {
    return refJpClass;
  }

  public void setRefJpClass(String refJpClass) {
    this.refJpClass = refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public void setRefJpAttr(String refJpAttr) {
    this.refJpAttr = refJpAttr;
  }

  public String getRefFilter() {
    return refFilter;
  }

  public void setRefFilter(String refFilter) {
    this.refFilter = refFilter;
  }

  public XmlParamEnums getXmlParamEnums() {
    return xmlParamEnums;
  }

  public void setXmlParamEnums(XmlParamEnums xmlParamEnums) {
    this.xmlParamEnums = xmlParamEnums;
  }

  public boolean isClientSearch() {
    return clientSearch;
  }

  public void setClientSearch(boolean clientSearch) {
    this.clientSearch = clientSearch;
  }

  @Override
  public String toString() {
    return "XmlParam{" +
        "code='" + code + '\'' +
        ", description='" + description + '\'' +
        ", external=" + external +
        ", value=" + value +
        ", type='" + type + '\'' +
        ", length='" + length + '\'' +
        ", stringFormat='" + stringFormat + '\'' +
        ", integerFormat='" + integerFormat + '\'' +
        ", qName='" + qName + '\'' +
        ", multiple='" + multiple + '\'' +
        ", mandatory=" + mandatory +
        ", refJpClass='" + refJpClass + '\'' +
        ", refJpAttr='" + refJpAttr + '\'' +
        ", refFilter='" + refFilter + '\'' +
        ", paramEnums=" + xmlParamEnums +
        ", clientSearch=" + clientSearch +
        '}';
  }

  public static JPParam toParam(XmlParam xmlParam,
                                BiFunction<XmlParam, JPType, Object> getDefValue) {
    return toParam(xmlParam, getDefValue, XmlParam::getRefFilter);
  }

  public static JPParam toParam(XmlParam xmlParam,
                                BiFunction<XmlParam, JPType, Object> getDefValue,
                                Function<XmlParam, String> getRefFilter) {
    JPType jpType = JPType.getType(xmlParam.getType());
    boolean mandatory = xmlParam.isMandatory();
    boolean multiple = xmlParam.isMultiple();
    boolean clientSearch = xmlParam.isClientSearch();
    String refJPClass = xmlParam.getRefJpClass();
    String refJPAttr = xmlParam.getRefJpAttr();
    XmlParamEnums xmlParamEnums = xmlParam.getXmlParamEnums();
    XmlParamEnum[] xmlParamEnumArr = xmlParamEnums != null ? xmlParamEnums.getXmlParamEnums() : null;
    // Перечислимые значения
    Collection<JPEnum> enums = null;
    if (xmlParamEnumArr != null) {
      enums = new ArrayList<>();
      for (XmlParamEnum xmlParamEnum : xmlParamEnumArr) {
        enums.add(JPEnum.of(xmlParamEnum.getValue(), xmlParamEnum.getDescription(), xmlParamEnum.getqName()));
      }
    }
    String defValue = xmlParam.getValue();
    return JPCommonParam.newBuilder()
        .external(xmlParam.getExternal() == null || xmlParam.getExternal())
        .code(xmlParam.getCode())
        .description(xmlParam.getDescription())
        .type(jpType)
        .length(xmlParam.getLength())
        .stringFormat(JPStringFormat.getType(xmlParam.getStringFormat()))
        .integerFormat(JPIntegerFormat.getType(xmlParam.getIntegerFormat()))
        .qName(xmlParam.getqName())
        .multiple(multiple)
        .mandatory(mandatory)
        .value(defValue == null || defValue.isEmpty() ? null :
            (multiple || jpType == null ? defValue : getDefValue.apply(xmlParam, jpType)))
        .refJpClass(refJPClass != null && !refJPClass.isEmpty() ? refJPClass : null)
        .refJpAttr(refJPAttr != null && !refJPAttr.isEmpty() ? refJPAttr : null)
        .refFilter(getRefFilter.apply(xmlParam))
        .enums(enums)
        .clientSearch(clientSearch)
        .build();
  }
}
