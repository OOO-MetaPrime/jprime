package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpTask")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpTasks {
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpTask[] jpTask;

  public XmlJpTask[] getJpTask() {
    return jpTask;
  }

  public void setJpTask(XmlJpTask[] jpTask) {
    this.jpTask = jpTask;
  }

  @Override
  public String toString() {
    return "XmlJpTasks{" +
        "jpTask=" + Arrays.toString(jpTask) +
        '}';
  }
}
