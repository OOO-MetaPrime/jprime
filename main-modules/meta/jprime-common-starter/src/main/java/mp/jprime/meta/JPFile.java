package mp.jprime.meta;

/**
 * Метаописание хранение файла
 */
public interface JPFile {
  /**
   * Код атрибута типа файл
   *
   * @return Код атрибута
   */
  String getFileAttrCode();

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
   * Атрибут для хранения - Код хранилища
   *
   * @return Кодовое имя атрибута
   */
  String getStorageCodeAttrCode();

  /**
   * Атрибут для хранения - Путь в хранилище
   *
   * @return Кодовое имя атрибута
   */
  String getStorageFilePathAttrCode();

  /**
   * Атрибут для хранения - Заголовок файла
   *
   * @return Кодовое имя атрибута
   */
  String getFileTitleAttrCode();

  /**
   * Атрибут для хранения - Расширение файла
   *
   * @return Кодовое имя атрибута
   */
  String getFileExtAttrCode();

  /**
   * Атрибут для хранения - Размер файла
   *
   * @return Кодовое имя атрибута
   */
  String getFileSizeAttrCode();

  /**
   * Атрибут для хранения - Возвращает дату файла
   *
   * @return Кодовое имя атрибута
   */
  String getFileDateAttrCode();
}
