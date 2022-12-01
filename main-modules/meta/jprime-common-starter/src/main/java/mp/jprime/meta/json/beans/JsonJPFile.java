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

  /**
   * Атрибут для хранения - Возвращает дополнительную информацию о файле
   */
  private String infoAttr;

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

  public String getInfoAttr() {
    return infoAttr;
  }

  public JsonJPFile() {

  }

  private JsonJPFile(String titleAttr, String extAttr, String sizeAttr, String dateAttr, String infoAttr) {
    this.titleAttr = titleAttr;
    this.extAttr = extAttr;
    this.sizeAttr = sizeAttr;
    this.dateAttr = dateAttr;
    this.infoAttr = infoAttr;
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
    private String infoAttr;

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

    public Builder infoAttr(String infoAttr) {
      this.infoAttr = infoAttr;
      return this;
    }

    public JsonJPFile build() {
      return new JsonJPFile(titleAttr, extAttr, sizeAttr, dateAttr, infoAttr);
    }
  }
}
