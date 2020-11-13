package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPFile {
  /**
   * Атрибут для хранения - Заголовок файла
   */
  private String titleAttr;

  /**
   * Атрибут для хранения - Расширение файла
   */
  private String extAttr;

  /**
   * Атрибут для хранения - Размер файла
   */
  private String sizeAttr;

  /**
   * Атрибут для хранения - Возвращает дату файла
   */
  private String dateAttr;

  public String getTitleAttr() {
    return titleAttr;
  }

  public String getExtAttr() {
    return extAttr;
  }

  public String getSizeAttr() {
    return sizeAttr;
  }

  public String getDateAttr() {
    return dateAttr;
  }

  public JsonJPFile() {

  }

  private JsonJPFile(String titleAttr, String extAttr, String sizeAttr, String dateAttr) {
    this.titleAttr = titleAttr;
    this.extAttr = extAttr;
    this.sizeAttr = sizeAttr;
    this.dateAttr = dateAttr;
  }

  /**
   * Построитель JsonJPFile
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPFile
   */
  public static final class Builder {
    private String titleAttr;
    private String extAttr;
    private String sizeAttr;
    private String dateAttr;

    private Builder() {

    }

    public Builder titleAttr(String titleAttr) {
      this.titleAttr = titleAttr;
      return this;
    }

    public Builder extAttr(String extAttr) {
      this.extAttr = extAttr;
      return this;
    }

    public Builder sizeAttr(String sizeAttr) {
      this.sizeAttr = sizeAttr;
      return this;
    }

    public Builder dateAttr(String dateAttr) {
      this.dateAttr = dateAttr;
      return this;
    }

    public JsonJPFile build() {
      return new JsonJPFile(titleAttr, extAttr, sizeAttr, dateAttr);
    }
  }
}
