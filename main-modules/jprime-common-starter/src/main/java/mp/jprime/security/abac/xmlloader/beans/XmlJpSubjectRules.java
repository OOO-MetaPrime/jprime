package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpSubjectRules {
  @JacksonXmlProperty(localName = "subjectRule")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpSubjectRule[] subjectRules;

  public XmlJpSubjectRule[] getSubjectRules() {
    return subjectRules;
  }

  public void setSubjectRules(XmlJpSubjectRule[] subjectRules) {
    this.subjectRules = subjectRules;
  }

  @Override
  public String toString() {
    return "XmlJpSubjectRules{" +
        "subjectRules=" + Arrays.toString(subjectRules) +
        '}';
  }
}
