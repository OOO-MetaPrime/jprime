package mp.jprime.files.json.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * Описание ответа после загрузки файла
 */
@JsonPropertyOrder({
    "fileCode",
    "name",
    "createdDate",
    "length"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonFileInfo {
  /**
   * Уникальный код файла
   */
  private final String fileCode;
  /**
   * Имя файла
   */
  private final String name;
  /**
   * Дата начала формирования файла
   */
  private final Date createdDate;
  /**
   * Размер файла
   */
  private final Long length;

  public String getFileCode() {
    return fileCode;
  }

  public String getName() {
    return name;
  }

  public Date getCreatedDate() {
    return createdDate;
  }


  public Long getLength() {
    return length;
  }

  /**
   * Конструктор
   *
   * @param fileCode     Уникальный код файла
   * @param name         Имя файла
   * @param createdDate  Дата начала формирования файла
   * @param length       Размер файла
   */
  private JsonFileInfo(String fileCode, String name, Long length, Date createdDate) {
    this.fileCode = fileCode;
    this.name = name;
    this.length = length;
    this.createdDate = createdDate;
  }

  /**
   * Построитель FileUpload
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель FileUpload
   */
  public static final class Builder {
    private String fileCode;
    private String name;
    private Long length;
    private Date createdDate;

    private Builder() {
    }

    /**
     * Уникальный код файла
     *
     * @param fileCode Уникальный код файла
     * @return Builder
     */
    public Builder fileCode(String fileCode) {
      this.fileCode = fileCode;
      return this;
    }

    /**
     * Имя файла
     *
     * @param name Имя файла
     * @return Builder
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Дата начала формирования файла
     *
     * @param createdDate Дата начала формирования файла
     * @return Builder
     */
    public Builder createdDate(Date createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    /**
     * Размер файла
     *
     * @param length Размер файла
     * @return Builder
     */
    public Builder length(Long length) {
      this.length = length;
      return this;
    }


    /**
     * Создаем FileUpload
     *
     * @return FileUpload
     */
    public JsonFileInfo build() {
      return new JsonFileInfo(fileCode, name, length, createdDate);
    }
  }
}
