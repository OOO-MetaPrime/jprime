package mp.jprime.security.abac.xmlloader.beans;

public class XmlJpTime {
  private String daysOfWeek;
  private String fromTime;
  private String toTime;
  private String fromDateTime;
  private String toDateTime;

  public String getDaysOfWeek() {
    return daysOfWeek;
  }

  public void setDaysOfWeek(String daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
  }

  public String getFromTime() {
    return fromTime;
  }

  public void setFromTime(String fromTime) {
    this.fromTime = fromTime;
  }

  public String getToTime() {
    return toTime;
  }

  public void setToTime(String toTime) {
    this.toTime = toTime;
  }

  public String getFromDateTime() {
    return fromDateTime;
  }

  public void setFromDateTime(String fromDateTime) {
    this.fromDateTime = fromDateTime;
  }

  public String getToDateTime() {
    return toDateTime;
  }

  public void setToDateTime(String toDateTime) {
    this.toDateTime = toDateTime;
  }

  @Override
  public String toString() {
    return "XmlJpTime{" +
        "daysOfWeek='" + daysOfWeek + '\'' +
        ", fromTime='" + fromTime + '\'' +
        ", toTime='" + toTime + '\'' +
        ", fromDateTime='" + fromDateTime + '\'' +
        ", toDateTime='" + toDateTime + '\'' +
        '}';
  }
}
