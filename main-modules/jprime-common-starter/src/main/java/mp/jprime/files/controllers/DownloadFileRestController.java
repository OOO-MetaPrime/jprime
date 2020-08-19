package mp.jprime.files.controllers;

import mp.jprime.controllers.DownloadFile;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.files.JPFileInfo;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * Базовый класс для скачивания файлов
 */
public abstract class DownloadFileRestController implements DownloadFile {
  protected static final Logger LOG = LoggerFactory.getLogger(DownloadFileRestController.class);

  /**
   * Описание всех хранилищ системы
   */
  private RepositoryStorage repositoryStorage;

  @Autowired
  private void setRepositoryStorage(RepositoryStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  protected RepositoryStorage getRepositoryStorage() {
    return repositoryStorage;
  }

  protected Mono<ResponseEntity> read(JPFileInfo info, String userAgent) {
    JPFileStorage storage = (JPFileStorage) repositoryStorage.getStorage(info.getStorageCode());
    if (storage == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    String path = info.getStorageFilePath();
    String fileName = info.getStorageFileName();
    FileInfo fileInfo = storage.getInfo(path, fileName);
    if (fileInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return read(storage.read(path, fileName), info.getFileTitle(), fileInfo.getLength(), fileInfo.getContentType(),
        userAgent);
  }
}
