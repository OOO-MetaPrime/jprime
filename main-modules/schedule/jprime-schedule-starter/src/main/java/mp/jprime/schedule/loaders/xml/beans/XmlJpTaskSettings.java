package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpTaskSettings")
public class XmlJpTaskSettings {
  private XmlJpTasks jpTasks;

  public XmlJpTasks getJpTasks() {
    return jpTasks;
  }

  public void setJpTasks(XmlJpTasks jpTasks) {
    this.jpTasks = jpTasks;
  }

  @Override
  public String toString() {
    return "XmlJpTaskSettings{" +
        "jpTasks=" + jpTasks +
        '}';
  }
}
