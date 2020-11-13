package mp.jprime.meta.annotations;

/**
 * Описание хранения файла
 */
public @interface JPFile {
  /**
   * Код хранилища
   *
   * @return Код хранилища
   */
  String storageCode();

  /**
   * Атрибут для хранения - Код хранилища
   *
   * @return Код хранилища
   */
  String storageCodeAttrCode() default "";

  /**
   * Путь в хранилище
   *
   * @return Путь в хранилище
   */
  String storageFilePath();

  /**
   * Атрибут для хранения - Путь в хранилище
   *
   * @return Путь в хранилище
   */
  String storageFilePathAttrCode() default "";

  /**
   * Атрибут для хранения - Заголовок файла
   *
   * @return Заголовок файла
   */
  String fileTitleAttrCode() default "";

  /**
   * Атрибут для хранения -  Расширение файла
   *
   * @return guid файла
   */
  String fileExtAttrCode() default "";

  /**
   * Атрибут для хранения -  Размер файла
   *
   * @return Размер файла
   */
  String fileSizeAttrCode() default "";

  /**
   * Атрибут для хранения - Возвращает дату файла
   *
   * @return Дата файла
   */
  String fileDateAttrCode() default "";
}
