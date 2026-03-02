package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpCron")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpCron {
  private String expression;

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  @Override
  public String toString() {
    return "XmlJpCron{" +
        "expression=" + expression +
        '}';
  }
}
