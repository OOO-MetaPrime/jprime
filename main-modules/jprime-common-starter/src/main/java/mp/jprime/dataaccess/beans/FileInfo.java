package mp.jprime.dataaccess.beans;

import java.util.Date;

/**
 * Информация о файле
 */
public class FileInfo {
  private final String path;
  private final String name;
  private final Date createdTime;
  private final long length;
  private final String contentType;

  /**
   * @param path        Путь
   * @param name        Имя
   * @param createdTime Дата создания
   * @param length      Длина
   * @param contentType Тип
   */
  public FileInfo(String path, String name, Date createdTime, long length, String contentType) {
    this.path = path;
    this.name = name;
    this.createdTime = createdTime;
    this.length = length;
    this.contentType = contentType;
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public long getLength() {
    return length;
  }

  public String getContentType() {
    return contentType;
  }
}
