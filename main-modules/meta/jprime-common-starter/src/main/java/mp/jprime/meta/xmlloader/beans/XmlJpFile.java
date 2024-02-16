package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpFile {
  private String storageCode;
  private String storageFilePath;
  private String storageCodeAttrCode;
  private String storageFilePathAttrCode;
  private String fileTitleAttrCode;
  private String fileExtAttrCode;
  private String fileSizeAttrCode;
  private String fileDateAttrCode;
  private String fileInfoAttrCode;
  private String fileStampAttrCode;
  public String getStorageCode() {
    return storageCode;
  }

  public void setStorageCode(String storageCode) {
    this.storageCode = storageCode;
  }

  public String getStorageFilePath() {
    return storageFilePath;
  }

  public void setStorageFilePath(String storageFilePath) {
    this.storageFilePath = storageFilePath;
  }

  public String getStorageCodeAttrCode() {
    return storageCodeAttrCode;
  }

  public void setStorageCodeAttrCode(String storageCodeAttrCode) {
    this.storageCodeAttrCode = storageCodeAttrCode;
  }

  public String getStorageFilePathAttrCode() {
    return storageFilePathAttrCode;
  }

  public void setStorageFilePathAttrCode(String storageFilePathAttrCode) {
    this.storageFilePathAttrCode = storageFilePathAttrCode;
  }

  public String getFileTitleAttrCode() {
    return fileTitleAttrCode;
  }

  public void setFileTitleAttrCode(String fileTitleAttrCode) {
    this.fileTitleAttrCode = fileTitleAttrCode;
  }

  public String getFileExtAttrCode() {
    return fileExtAttrCode;
  }

  public void setFileExtAttrCode(String fileExtAttrCode) {
    this.fileExtAttrCode = fileExtAttrCode;
  }

  public String getFileSizeAttrCode() {
    return fileSizeAttrCode;
  }

  public void setFileSizeAttrCode(String fileSizeAttrCode) {
    this.fileSizeAttrCode = fileSizeAttrCode;
  }

  public String getFileDateAttrCode() {
    return fileDateAttrCode;
  }

  public void setFileDateAttrCode(String fileDateAttrCode) {
    this.fileDateAttrCode = fileDateAttrCode;
  }

  public String getFileInfoAttrCode() {
    return fileInfoAttrCode;
  }

  public void setFileInfoAttrCode(String fileInfoAttrCode) {
    this.fileInfoAttrCode = fileInfoAttrCode;
  }

  public String getFileStampAttrCode() {
    return fileStampAttrCode;
  }

  public void setFileStampAttrCode(String fileStampAttrCode) {
    this.fileStampAttrCode = fileStampAttrCode;
  }

  @Override
  public String toString() {
    return "XmlJpFile{" +
        "storageCode='" + storageCode + '\'' +
        ", storageFilePath='" + storageFilePath + '\'' +
        ", storageCodeAttrCode='" + storageCodeAttrCode + '\'' +
        ", storageFilePathAttrCode='" + storageFilePathAttrCode + '\'' +
        ", fileTitleAttrCode='" + fileTitleAttrCode + '\'' +
        ", fileExtAttrCode='" + fileExtAttrCode + '\'' +
        ", fileSizeAttrCode='" + fileSizeAttrCode + '\'' +
        ", fileDateAttrCode='" + fileDateAttrCode + '\'' +
        ", fileInfoAttrCode='" + fileInfoAttrCode + '\'' +
        ", fileStampAttrCode='" + fileStampAttrCode + '\'' +
        '}';
  }
}
