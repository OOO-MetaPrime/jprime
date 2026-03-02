package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import mp.jprime.xml.beans.XmlParams;

@JacksonXmlRootElement(localName = "jpMode")
public class XmlJpMode {
  private String code;
  private String title;
  private String qName;
  private String appendType;
  private XmlParams params;
  private XmlJpOperations operations;
  private XmlJpModeResult jpResult;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public String getAppendType() {
    return appendType;
  }

  public void setAppendType(String appendType) {
    this.appendType = appendType;
  }

  public XmlParams getParams() {
    return params;
  }

  public void setParams(XmlParams params) {
    this.params = params;
  }

  public XmlJpOperations getOperations() {
    return operations;
  }

  public void setOperations(XmlJpOperations operations) {
    this.operations = operations;
  }

  public XmlJpModeResult getJpResult() {
    return jpResult;
  }

  public void setJpResult(XmlJpModeResult jpResult) {
    this.jpResult = jpResult;
  }

  @Override
  public String toString() {
    return "XmlJpMode{" +
        "code=" + code +
        ",title=" + title +
        ",qName=" + qName +
        ",appendType=" + appendType +
        ",params=" + params +
        ",operations=" + operations +
        ",jpResult=" + jpResult +
        '}';
  }
}
