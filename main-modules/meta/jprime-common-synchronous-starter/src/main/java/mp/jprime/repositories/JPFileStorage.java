package mp.jprime.repositories;

import mp.jprime.files.beans.FileInfo;

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
   *
   * @return Инфо о файле
   */
  FileInfo getInfo(String path, String fileName);

  /**
   * Запись файла
   *
   * @param fileName Имя файла
   * @param stream   Поток с данными
   *
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
   *
   * @return Поток для чтения файла
   */
  InputStream read(String path, String fileName);

  /**
   * Копировать файл
   *
   * @param sourcePath     исходный путь файла
   * @param sourceFileName исходное имя файла
   * @param targetPath     путь для копирования файла
   *
   * @return Инфо о файле
   */
  FileInfo copy(String sourcePath, String sourceFileName, String targetPath);

  /**
   * Копировать файл с заменой имени
   *
   * @param sourcePath     исходный путь файла
   * @param sourceFileName исходное имя файла
   * @param targetPath     путь для копии файла
   * @param targetFileName имя для копии файла
   *
   * @return Инфо о файле
   */
  FileInfo copy(String sourcePath, String sourceFileName, String targetPath, String targetFileName);

  /**
   * Переместить файл
   *
   * @param sourcePath     исходный путь файла
   * @param sourceFileName исходное имя файла
   * @param targetPath     новый путь файла
   *
   * @return Инфо о файле
   */
  FileInfo move(String sourcePath, String sourceFileName, String targetPath);

  /**
   * Переместить файл с заменой имени
   *
   * @param sourcePath     исходный путь файла
   * @param sourceFileName исходное имя файла
   * @param targetPath     новый путь файла
   * @param targetFileName новое имя файла
   *
   * @return Инфо о файле
   */
  FileInfo move(String sourcePath, String sourceFileName, String targetPath, String targetFileName);

  /**
   * Удаление файла
   *
   * @param path     Путь
   * @param fileName Имя файла
   */
  void delete(String path, String fileName);
}
