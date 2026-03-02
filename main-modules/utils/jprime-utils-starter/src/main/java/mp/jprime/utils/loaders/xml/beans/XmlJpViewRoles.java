package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "roles")
public class XmlJpViewRoles {
  @JacksonXmlProperty(localName = "role")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] roles;

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "XmlJpViewRoles{" +
        "roles=" + Arrays.toString(roles) +
        '}';
  }
}