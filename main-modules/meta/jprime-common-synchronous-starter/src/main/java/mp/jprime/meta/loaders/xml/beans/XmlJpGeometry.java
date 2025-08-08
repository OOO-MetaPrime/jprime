package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpGeometry {
  private int SRID;

  public int getSRID() {
    return SRID;
  }

  public void setSRID(int SRID) {
    this.SRID = SRID;
  }

  @Override
  public String toString() {
    return "XmlJpGeometry{" +
        "SRID='" + SRID + '\'' +
        '}';
  }
}
