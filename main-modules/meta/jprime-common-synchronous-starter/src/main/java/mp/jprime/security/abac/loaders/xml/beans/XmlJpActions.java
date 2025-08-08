package mp.jprime.security.abac.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpActions {
  @JacksonXmlProperty(localName = "action")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] jpAction;

  public String[] getJpAction() {
    return jpAction;
  }

  public void setJpAction(String[] jpAction) {
    this.jpAction = jpAction;
  }

  @Override
  public String toString() {
    return "XmlJpActions{" +
        "jpAction='" + Arrays.toString(jpAction) + '\'' +
        '}';
  }
}
