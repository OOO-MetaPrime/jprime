package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDbJPFile {
  private String storageCode;
  private String storageCodeAttrCode;
  private String storageFilePath;
  private String storageFilePathAttrCode;
  private String fileTitleAttrCode;
  private String fileExtAttrCode;
  private String fileSizeAttrCode;
  private String fileDateAttrCode;
  private String fileInfoAttrCode;
  private String fileStampAttrCode;

  /**
   * Код хранилища
   *
   * @return Код хранилища
   */
  public String getStorageCode() {
    return storageCode;
  }

  private void setStorageCode(String storageCode) {
    this.storageCode = storageCode;
  }

  /**
   * Атрибут для хранения - Код хранилища
   *
   * @return Код хранилища
   */
  public String getStorageCodeAttrCode() {
    return storageCodeAttrCode;
  }

  private void setStorageCodeAttrCode(String storageCodeAttrCode) {
    this.storageCodeAttrCode = storageCodeAttrCode;
  }

  /**
   * Путь в хранилище
   *
   * @return Путь в хранилище
   */
  public String getStorageFilePath() {
    return storageFilePath;
  }

  private void setStorageFilePath(String storageFilePath) {
    this.storageFilePath = storageFilePath;
  }

  /**
   * Атрибут для хранения - Путь в хранилище
   *
   * @return Путь в хранилище
   */
  public String getStorageFilePathAttrCode() {
    return storageFilePathAttrCode;
  }

  private void setStorageFilePathAttrCode(String storageFilePathAttrCode) {
    this.storageFilePathAttrCode = storageFilePathAttrCode;
  }

  /**
   * Атрибут для хранения - Заголовок файла
   *
   * @return Заголовок файла
   */
  public String getFileTitleAttrCode() {
    return fileTitleAttrCode;
  }

  private void setFileTitleAttrCode(String fileTitleAttrCode) {
    this.fileTitleAttrCode = fileTitleAttrCode;
  }

  /**
   * Атрибут для хранения - Расширение файла
   *
   * @return guid файла
   */
  public String getFileExtAttrCode() {
    return fileExtAttrCode;
  }

  private void setFileExtAttrCode(String fileExtAttrCode) {
    this.fileExtAttrCode = fileExtAttrCode;
  }

  /**
   * Атрибут для хранения - Размер файла
   *
   * @return Размер файла
   */
  public String getFileSizeAttrCode() {
    return fileSizeAttrCode;
  }

  private void setFileSizeAttrCode(String fileSizeAttrCode) {
    this.fileSizeAttrCode = fileSizeAttrCode;
  }

  /**
   * Атрибут для хранения - Возвращает дату файла
   *
   * @return Дата файла
   */
  public String getFileDateAttrCode() {
    return fileDateAttrCode;
  }

  private void setFileDateAttrCode(String fileDateAttrCode) {
    this.fileDateAttrCode = fileDateAttrCode;
  }

  /**
   * Атрибут для хранения - Возвращает дополнительную информацию о файле
   *
   * @return Доп. инфа о файле
   */
  public String getFileInfoAttrCode() {
    return fileInfoAttrCode;
  }

  private void setFileInfoAttrCode(String fileInfoAttrCode) {
    this.fileInfoAttrCode = fileInfoAttrCode;
  }

  /**
   * Атрибут для хранения - Файл со штампом для подписи
   *
   * @return Кодовое имя атрибута
   */
  public String getFileStampAttrCode() {
    return fileStampAttrCode;
  }

  public void setFileStampAttrCode(String fileStampAttrCode) {
    this.fileStampAttrCode = fileStampAttrCode;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String storageCode;
    private String storageCodeAttrCode;
    private String storageFilePath;
    private String storageFilePathAttrCode;
    private String fileTitleAttrCode;
    private String fileExtAttrCode;
    private String fileSizeAttrCode;
    private String fileDateAttrCode;
    private String fileInfoAttrCode;
    private String fileStampAttrCode;

    private Builder() {
    }

    public Builder storageCode(String storageCode) {
      this.storageCode = storageCode;
      return this;
    }

    public Builder storageCodeAttrCode(String storageCodeAttrCode) {
      this.storageCodeAttrCode = storageCodeAttrCode;
      return this;
    }

    public Builder storageFilePath(String storageFilePath) {
      this.storageFilePath = storageFilePath;
      return this;
    }

    public Builder storageFilePathAttrCode(String storageFilePathAttrCode) {
      this.storageFilePathAttrCode = storageFilePathAttrCode;
      return this;
    }

    public Builder fileTitleAttrCode(String fileTitleAttrCode) {
      this.fileTitleAttrCode = fileTitleAttrCode;
      return this;
    }

    public Builder fileExtAttrCode(String fileExtAttrCode) {
      this.fileExtAttrCode = fileExtAttrCode;
      return this;
    }

    public Builder fileSizeAttrCode(String fileSizeAttrCode) {
      this.fileSizeAttrCode = fileSizeAttrCode;
      return this;
    }

    public Builder fileDateAttrCode(String fileDateAttrCode) {
      this.fileDateAttrCode = fileDateAttrCode;
      return this;
    }

    public Builder fileInfoAttrCode(String fileInfoAttrCode) {
      this.fileInfoAttrCode = fileInfoAttrCode;
      return this;
    }

    public Builder fileStampAttrCode(String fileStampAttrCode) {
      this.fileStampAttrCode = fileStampAttrCode;
      return this;
    }

    public JsonDbJPFile build() {
      JsonDbJPFile jpStorageAttrFileBean = new JsonDbJPFile();
      jpStorageAttrFileBean.setFileExtAttrCode(this.fileExtAttrCode);
      jpStorageAttrFileBean.setFileTitleAttrCode(this.fileTitleAttrCode);
      jpStorageAttrFileBean.setFileSizeAttrCode(this.fileSizeAttrCode);
      jpStorageAttrFileBean.setStorageCode(storageCode);
      jpStorageAttrFileBean.setStorageFilePath(this.storageFilePath);
      jpStorageAttrFileBean.setFileDateAttrCode(this.fileDateAttrCode);
      jpStorageAttrFileBean.setFileInfoAttrCode(this.fileInfoAttrCode);
      jpStorageAttrFileBean.setStorageFilePathAttrCode(storageFilePathAttrCode);
      jpStorageAttrFileBean.setStorageCodeAttrCode(storageCodeAttrCode);
      jpStorageAttrFileBean.setFileStampAttrCode(fileStampAttrCode);
      return jpStorageAttrFileBean;
    }
  }
}
