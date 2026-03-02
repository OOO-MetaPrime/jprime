package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpUtilTags")
public class XmlJpUtilTags {
  @JacksonXmlProperty(localName = "tag")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] tags;

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    return "jpUtilTags{" +
        "tags=" + Arrays.toString(tags) +
        '}';
  }
}