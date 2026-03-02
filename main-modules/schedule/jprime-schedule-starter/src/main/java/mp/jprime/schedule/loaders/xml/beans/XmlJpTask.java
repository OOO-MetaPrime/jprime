package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.UUID;

@JacksonXmlRootElement(localName = "jpTask")
public class XmlJpTask {
  private UUID code;
  private boolean disable;
  private String name;
  private String description;
  private String catalogCode;
  private String executorCode;
  private XmlParamValues paramValues;
  private XmlJpCron jpCron;

  public UUID getCode() {
    return code;
  }

  public void setCode(UUID code) {
    this.code = code;
  }

  public boolean isDisable() {
    return disable;
  }

  public void setDisable(boolean disable) {
    this.disable = disable;
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

  public String getCatalogCode() {
    return catalogCode;
  }

  public void setCatalogCode(String catalogCode) {
    this.catalogCode = catalogCode;
  }

  public String getExecutorCode() {
    return executorCode;
  }

  public void setExecutorCode(String executorCode) {
    this.executorCode = executorCode;
  }

  public XmlParamValues getParamValues() {
    return paramValues;
  }

  public void setParamValues(XmlParamValues paramValues) {
    this.paramValues = paramValues;
  }

  public XmlJpCron getJpCron() {
    return jpCron;
  }

  public void setJpCron(XmlJpCron jpCron) {
    this.jpCron = jpCron;
  }

  @Override
  public String toString() {
    return "XmlJpTask{" +
        "code=" + code +
        ",disable=" + disable +
        ",name=" + name +
        ",description=" + description +
        ",catalogCode=" + catalogCode +
        ",executorCode=" + executorCode +
        ",paramValues=" + paramValues +
        ",jpCron=" + jpCron +
        '}';
  }
}
