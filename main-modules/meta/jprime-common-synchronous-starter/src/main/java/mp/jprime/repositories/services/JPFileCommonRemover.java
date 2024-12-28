package mp.jprime.repositories.services;

import mp.jprime.repositories.JPFileRemover;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис физического удаления файла из хранилища
 */
@Service
public class JPFileCommonRemover implements JPFileRemover {

  /**
   * API для работы с хранилищами
   */
  private RepositoryStorage repositoryStorage;

  @Autowired
  private void setRepositoryStorage(RepositoryStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  /**
   * Удаляет файл
   *
   * @param storageCode код хранилища файла
   * @param path        путь к файлу в хранилище
   * @param fileName    имя файла в хранилище
   */
  @Override
  public void delete(String storageCode, String path, String fileName) {
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (storage instanceof JPFileStorage) {
      JPFileStorage fileStorage = (JPFileStorage) storage;
      fileStorage.delete(path, fileName);
    }
  }
}
