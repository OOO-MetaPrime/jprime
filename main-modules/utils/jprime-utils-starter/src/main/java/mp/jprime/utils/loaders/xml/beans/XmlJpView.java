package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "view")
public class XmlJpView {
  private String title;
  @JacksonXmlProperty(localName = "roles")
  private XmlJpViewRoles roles;
  @JacksonXmlProperty(localName = "jpClasses")
  private XmlJpViewClasses jpClasses;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public XmlJpViewRoles getRoles() {
    return roles;
  }

  public void setRoles(XmlJpViewRoles roles) {
    this.roles = roles;
  }

  public XmlJpViewClasses getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(XmlJpViewClasses jpClasses) {
    this.jpClasses = jpClasses;
  }

  @Override
  public String toString() {
    return "XmlJpView{" +
        ",title=" + title +
        ",roles=" + roles +
        ",jpClasses=" + jpClasses +
        '}';
  }
}