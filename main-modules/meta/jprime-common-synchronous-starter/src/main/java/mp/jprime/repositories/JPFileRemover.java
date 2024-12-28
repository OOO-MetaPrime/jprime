package mp.jprime.repositories;

/**
 * Сервис физического удаления файла из хранилища
 */
public interface JPFileRemover {
  /**
   * Удаляет файл
   *
   * @param storageCode код хранилища файла
   * @param path        путь к файлу в хранилище
   * @param fileName    имя файла в хранилище
   */
  void delete(String storageCode, String path, String fileName);
}
