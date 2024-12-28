package mp.jprime.files.beans;

import java.time.LocalDateTime;

/**
 * Информация о файле
 */
public final class FileInfo {
  private final String path;
  private final String name;
  private final LocalDateTime createdTime;
  private final long length;
  private final String contentType;

  /**
   * @param path        Путь
   * @param name        Имя
   * @param createdTime Дата создания
   * @param length      Длина
   * @param contentType Тип
   */
  private FileInfo(String path, String name, LocalDateTime createdTime, long length, String contentType) {
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

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public long getLength() {
    return length;
  }

  public String getContentType() {
    return contentType;
  }

  /**
   * Построитель
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель FileInfo
   */
  public static final class Builder {
    private String path;
    private String name;
    private LocalDateTime createdTime;
    private long length = -1;
    private String contentType;

    private Builder() {

    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder createdTime(LocalDateTime createdTime) {
      this.createdTime = createdTime;
      return this;
    }

    public Builder length(long length) {
      this.length = length;
      return this;
    }

    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * Создаем FileInfo
     *
     * @return FileInfo
     */
    public FileInfo build() {
      return new FileInfo(path, name, createdTime, length, contentType);
    }
  }
}
