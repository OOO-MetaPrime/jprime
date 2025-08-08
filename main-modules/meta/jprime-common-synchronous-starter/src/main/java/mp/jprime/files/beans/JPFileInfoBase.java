package mp.jprime.files.beans;

import mp.jprime.files.JPFileInfo;

import java.time.LocalDateTime;

/**
 * Информация о файле
 */
public class JPFileInfoBase<T> implements JPFileInfo<T> {
  /**
   * Код файла
   */
  private final T fileCode;
  /**
   * Код хранилища
   */
  private final String storageCode;
  /**
   * Путь в хранилище
   */
  private final String storageFilePath;
  /**
   * Имя в хранилище
   */
  private final String storageFileName;
  /**
   * Заголовок файла
   */
  private final String fileTitle;
  /**
   * Расширение файла
   */
  private final String fileExt;
  /**
   * Размер файла
   */
  private final Long fileSize;
  /**
   * Дата файла
   */
  private final LocalDateTime fileDate;

  /**
   * Информация о файле
   *
   * @param fileCode        Код файла
   * @param storageCode     Код хранилища
   * @param storageFilePath Путь в хранилище
   * @param storageFileName Имя в хранилище
   * @param fileTitle       Заголовок файла
   * @param fileExt         Расширение файла
   * @param fileSize        Размер файла
   * @param fileDate        Дата файла
   */
  protected JPFileInfoBase(T fileCode, String storageCode, String storageFilePath, String storageFileName,
                           String fileTitle, String fileExt, Long fileSize, LocalDateTime fileDate) {
    this.fileCode = fileCode;
    this.storageCode = storageCode;
    this.storageFilePath = storageFilePath;
    this.storageFileName = storageFileName;
    this.fileTitle = fileTitle;
    this.fileExt = fileExt;
    this.fileSize = fileSize;
    this.fileDate = fileDate;
  }

  /**
   * Информация о файле
   *
   * @return Информация о файле
   */
  @Override
  public T getFileCode() {
    return fileCode;
  }

  /**
   * Код хранилища
   *
   * @return Код хранилища
   */
  @Override
  public String getStorageCode() {
    return storageCode;
  }

  /**
   * Путь в хранилище
   *
   * @return Путь в хранилище
   */
  @Override
  public String getStorageFilePath() {
    return storageFilePath;
  }

  /**
   * Имя в хранилище
   *
   * @return Имя в хранилище
   */
  @Override
  public String getStorageFileName() {
    return storageFileName;
  }

  /**
   * Заголовок файла
   *
   * @return Заголовок файла
   */
  @Override
  public String getFileTitle() {
    return fileTitle;
  }


  /**
   * Расширение файла
   *
   * @return guid файла
   */
  @Override
  public String getFileExt() {
    return fileExt;
  }

  /**
   * Размер файла
   *
   * @return Размер файла
   */
  @Override
  public Long getFileSize() {
    return fileSize;
  }

  /**
   * Возвращает дату файла
   *
   * @return Дата файла
   */
  @Override
  public LocalDateTime getFileDate() {
    return fileDate;
  }

  /**
   * Построитель JPFileInfoBase
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder<>();
  }

  /**
   * Построитель JPFileInfoBase
   */
  public static class Builder<V, T extends Builder> {
    protected V fileCode;
    protected String storageCode;
    protected String storageFilePath;
    protected String storageFileName;
    protected String fileTitle;
    protected String fileExt;
    protected Long fileSize;
    protected LocalDateTime fileDate;

    protected Builder() {
    }

    /**
     * Уникальный код файла
     *
     * @param fileCode Уникальный код файла
     * @return Builder
     */
    public T fileCode(V fileCode) {
      this.fileCode = fileCode;
      return (T) this;
    }

    /**
     * Код хранилища
     *
     * @param storageCode Код хранилища
     * @return Builder
     */
    public T storageCode(String storageCode) {
      this.storageCode = storageCode;
      return (T) this;
    }

    /**
     * Путь в хранилище
     *
     * @param storageFilePath Путь в хранилище
     * @return Builder
     */
    public T storageFilePath(String storageFilePath) {
      this.storageFilePath = storageFilePath;
      return (T) this;
    }

    /**
     * Имя в хранилище
     *
     * @param storageFileName Имя в хранилище
     * @return Builder
     */
    public T storageFileName(String storageFileName) {
      this.storageFileName = storageFileName;
      return (T) this;
    }

    /**
     * Заголовок файла
     *
     * @param fileTitle Заголовок файла
     * @return Builder
     */
    public T fileTitle(String fileTitle) {
      this.fileTitle = fileTitle;
      return (T) this;
    }

    /**
     * Расширение файла
     *
     * @param fileExt Расширение файла
     * @return Builder
     */
    public T fileExt(String fileExt) {
      this.fileExt = fileExt;
      return (T) this;
    }

    /**
     * Размер файла
     *
     * @param fileSize Размер файла
     * @return Builder
     */
    public T fileSize(Long fileSize) {
      this.fileSize = fileSize;
      return (T) this;
    }

    /**
     * Дата файла
     *
     * @param fileDate Дата файла
     * @return Builder
     */
    public T fileDate(LocalDateTime fileDate) {
      this.fileDate = fileDate;
      return (T) this;
    }

    /**
     * Создаем JPFileInfoBase
     *
     * @return JPFileInfoBase
     */
    public JPFileInfoBase build() {
      return new JPFileInfoBase(fileCode, storageCode, storageFilePath, storageFileName, fileTitle, fileExt, fileSize, fileDate);
    }
  }
}
