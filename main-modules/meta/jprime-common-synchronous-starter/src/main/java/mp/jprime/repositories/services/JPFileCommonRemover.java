package mp.jprime.repositories.services;

import mp.jprime.repositories.JPFileRemover;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Сервис физического удаления файла из хранилища
 */
@Service
public class JPFileCommonRemover implements JPFileRemover {
  private RepositoryGlobalStorage repositoryStorage;

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public void delete(String storageCode, String path, String fileName) {
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (storage instanceof JPFileStorage fileStorage) {
      fileStorage.delete(path, fileName);
    }
  }
}
