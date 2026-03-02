package mp.jprime.globalsettings.enums;

/**
 * Типовые настройки
 */
public enum JPGlobalSettings {
  /**
   * Максимальный размер загружаемого файла (Мб)
   */
  FILES_LIMITS_MAX_FILE_SIZE("files.limits.maxFileSize"),
  /**
   * Максимальный размер файлов в архиве (Мб)
   */
  FILES_LIMITS_MAX_ARCHIVE_FILES_SIZE("files.limits.maxArchiveFilesSize"),
  /**
   * Максимальное количество файлов за одну загрузку
   */
  FILES_LIMITS_MAX_UPLOAD_FILE_COUNT("files.limits.maxUploadFileCount");

  private final String code;

  JPGlobalSettings(String code) {
    this.code = code;
  }

  /**
   * Получить код настройки
   *
   * @return Код настройки
   */
  public String getCode() {
    return code;
  }

}
