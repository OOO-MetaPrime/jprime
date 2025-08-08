package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpClassTags {
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
    return "XmlJpClassTags{" +
        "tags=" + Arrays.toString(tags) +
        '}';
  }
}
