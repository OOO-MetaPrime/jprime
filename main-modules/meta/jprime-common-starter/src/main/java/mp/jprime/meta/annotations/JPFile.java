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
   * @return Кодовое имя атрибута
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
   * @return Кодовое имя атрибута
   */
  String storageFilePathAttrCode() default "";

  /**
   * Атрибут для хранения - Заголовок файла
   *
   * @return Кодовое имя атрибута
   */
  String fileTitleAttrCode() default "";

  /**
   * Атрибут для хранения -  Расширение файла
   *
   * @return Кодовое имя атрибута
   */
  String fileExtAttrCode() default "";

  /**
   * Атрибут для хранения -  Размер файла
   *
   * @return Кодовое имя атрибута
   */
  String fileSizeAttrCode() default "";

  /**
   * Атрибут для хранения - Возвращает дату файла
   *
   * @return Кодовое имя атрибута
   */
  String fileDateAttrCode() default "";
}
