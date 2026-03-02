package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpCheck")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpCheck {
  @JacksonXmlProperty(localName = "operation")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpOperation[] operation;

  public XmlJpOperation[] getOperation() {
    return operation;
  }

  public void setOperation(XmlJpOperation[] operation) {
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "XmlJpCheck{" +
        "operation=" + Arrays.toString(operation) +
        '}';
  }
}
