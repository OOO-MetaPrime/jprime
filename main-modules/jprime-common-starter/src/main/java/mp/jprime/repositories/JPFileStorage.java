package mp.jprime.repositories;

import mp.jprime.dataaccess.beans.FileInfo;

import java.io.InputStream;

/**
 * Описание типового хранилища файлов
 */
public interface JPFileStorage extends JPStorage {

  /**
   * Получаем информацию о файле
   *
   * @param path     Путь
   * @param fileName Имя файла
   * @return Инфо о файле
   */
  FileInfo getInfo(String path, String fileName);

  /**
   * Запись файла
   *
   * @param fileName Имя файла
   * @param stream   Поток с данными
   * @return Инфо о файле
   */
  FileInfo save(String fileName, InputStream stream);

  /**
   * Запись файла
   *
   * @param path     Путь
   * @param fileName Имя файла
   * @param stream   Поток с данными
   */
  FileInfo save(String path, String fileName, InputStream stream);

  /**
   * Чтение файла
   *
   * @param path     Путь
   * @param fileName Имя файла
   * @return Поток для чтения файла
   */
  InputStream read(String path, String fileName);

  /**
   * Удаление файла
   *
   * @param path     Путь
   * @param fileName Имя файла
   */
  void delete(String path, String fileName);
}
