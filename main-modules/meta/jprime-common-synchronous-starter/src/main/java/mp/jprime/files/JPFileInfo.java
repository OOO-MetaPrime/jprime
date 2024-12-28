package mp.jprime.files;

import java.time.LocalDateTime;

/**
 * Информация о файле
 */
public interface JPFileInfo {
  /**
   * Информация о файле
   *
   * @return Информация о файле
   */
  String getFileCode();

  /**
   * Заголовок файла
   *
   * @return Заголовок файла
   */
  String getFileTitle();

  /**
   * Расширение файла
   *
   * @return guid файла
   */
  String getFileExt();

  /**
   * Размер файла
   *
   * @return Размер файла
   */
  Long getFileSize();

  /**
   * Возвращает дату файла
   *
   * @return Дата файла
   */
  LocalDateTime getFileDate();

  /**
   * Код хранилища
   *
   * @return Код хранилища
   */
  String getStorageCode();

  /**
   * Путь в хранилище
   *
   * @return Путь в хранилище
   */
  String getStorageFilePath();

  /**
   * Имя в хранилище
   *
   * @return Имя в хранилище
   */
  String getStorageFileName();
}