package mp.jprime.meta.beans;

import mp.jprime.meta.JPFile;

/**
 * Метаописание хранение файла
 */
public final class JPFileBean implements JPFile {
  /**
   * Код хранилища
   */
  private final String storageCode;

  /**
   * Путь в хранилище
   */
  private final String storageFilePath;

  /**
   * Атрибут для хранения - Код хранилища
   */
  private final String storageCodeAttrCode;

  /**
   * Атрибут для хранения - Путь в хранилище
   */
  private final String storageFilePathAttrCode;

  /**
   * Атрибут для хранения - Заголовок файла
   */
  private final String fileTitleAttrCode;

  /**
   * Атрибут для хранения - Расширение файла
   */
  private final String fileExtAttrCode;

  /**
   * Атрибут для хранения - Размер файла
   */
  private final String fileSizeAttrCode;

  /**
   * Атрибут для хранения - Возвращает дату файла
   */
  private final String fileDateAttrCode;

  private JPFileBean(String storageCode, String storageFilePath,
                     String storageCodeAttrCode, String storageFilePathAttrCode,
                     String fileTitleAttrCode, String fileExtAttrCode, String fileSizeAttrCode, String fileDateAttrCode) {
    this.storageCode = storageCode;
    this.storageFilePath = storageFilePath;
    this.storageCodeAttrCode = storageCodeAttrCode;
    this.storageFilePathAttrCode = storageFilePathAttrCode;
    this.fileTitleAttrCode = fileTitleAttrCode;
    this.fileExtAttrCode = fileExtAttrCode;
    this.fileSizeAttrCode = fileSizeAttrCode;
    this.fileDateAttrCode = fileDateAttrCode;
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
   * Атрибут для хранения - Код хранилища
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getStorageCodeAttrCode() {
    return storageCodeAttrCode;
  }

  /**
   * Атрибут для хранения - Путь в хранилище
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getStorageFilePathAttrCode() {
    return storageFilePathAttrCode;
  }

  /**
   * Атрибут для хранения - Заголовок файла
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getFileTitleAttrCode() {
    return fileTitleAttrCode;
  }

  /**
   * Атрибут для хранения - Расширение файла
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getFileExtAttrCode() {
    return fileExtAttrCode;
  }

  /**
   * Атрибут для хранения - Размер файла
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getFileSizeAttrCode() {
    return fileSizeAttrCode;
  }

  /**
   * Атрибут для хранения - Возвращает дату файла
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getFileDateAttrCode() {
    return fileDateAttrCode;
  }

  /**
   * Построитель JPFileBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPFileBean
   */
  public static final class Builder {
    private String storageCode;
    private String storageFilePath;
    private String storageCodeAttrCode;
    private String storageFilePathAttrCode;
    private String fileTitleAttrCode;
    private String fileExtAttrCode;
    private String fileSizeAttrCode;
    private String fileDateAttrCode;

    private Builder() {
    }

    /**
     * Создаем JPFileBean
     *
     * @return JPFileBean
     */
    public JPFileBean build() {
      return new JPFileBean(storageCode, storageFilePath,
          storageCodeAttrCode, storageFilePathAttrCode,
          fileTitleAttrCode, fileExtAttrCode, fileSizeAttrCode, fileDateAttrCode);
    }

    /**
     * Код хранилища
     *
     * @param storageCode Код хранилища
     * @return Builder
     */
    public Builder storageCode(String storageCode) {
      this.storageCode = storageCode;
      return this;
    }

    /**
     * Путь в хранилище
     *
     * @param storageFilePath Путь в хранилище
     * @return Builder
     */
    public Builder storageFilePath(String storageFilePath) {
      this.storageFilePath = storageFilePath;
      return this;
    }

    /**
     * Атрибут для хранения - Код хранилища
     *
     * @param storageCodeAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder storageCodeAttrCode(String storageCodeAttrCode) {
      this.storageCodeAttrCode = storageCodeAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения - Путь в хранилище
     *
     * @param storageFilePathAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder storageFilePathAttrCode(String storageFilePathAttrCode) {
      this.storageFilePathAttrCode = storageFilePathAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения - Заголовок файла
     *
     * @param fileTitleAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder fileTitleAttrCode(String fileTitleAttrCode) {
      this.fileTitleAttrCode = fileTitleAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения -
     * Расширение файла
     *
     * @param fileExtAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder fileExtAttrCode(String fileExtAttrCode) {
      this.fileExtAttrCode = fileExtAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения - Размер файла
     *
     * @param fileSizeAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder fileSizeAttrCode(String fileSizeAttrCode) {
      this.fileSizeAttrCode = fileSizeAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения - Возвращает дату файла
     *
     * @param fileDateAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder fileDateAttrCode(String fileDateAttrCode) {
      this.fileDateAttrCode = fileDateAttrCode;
      return this;
    }
  }
}
