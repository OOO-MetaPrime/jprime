package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpGeometry {
  private int srid;

  public int getSrid() {
    return srid;
  }

  public void setSrid(int srid) {
    this.srid = srid;
  }

  @Override
  public String toString() {
    return "XmlJpGeometry{" +
        "srid='" + srid + '\'' +
        '}';
  }
}
