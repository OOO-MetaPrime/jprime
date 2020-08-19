package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.files.beans.JPFileInfoBase;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.JPFileLoader;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Базовая реализация JPFileLoader
 */
@Service
public class JPFileCommonLoader implements JPFileLoader {
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPObjectRepositoryService repo;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * API для работы с хранилищами
   */
  private RepositoryStorage repositoryStorage;

  @Autowired
  private void setRepo(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setRepositoryStorage(RepositoryStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public JPFileInfo getInfo(JPId id, String attr, AuthInfo auth) {
    if (id == null || attr == null) {
      return null;
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return null;
    }
    JPAttr jpAttr = jpClass.getAttr(attr);
    JPFile jpFile = jpAttr != null ? jpAttr.getRefJpFile() : null;
    if (jpAttr == null || jpAttr.getType() != JPType.FILE || jpFile == null) {
      return null;
    }
    String storageCode = jpFile.getStorageCode();
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (!(storage instanceof JPFileStorage)) {
      return null;
    }

    JPObject obj = repo.getObject(
        JPSelect.from(id.getJpClass())
            .attr(jpAttr)
            .attr(jpFile.getStorageFilePathAttrCode())
            .attr(jpFile.getFileTitleAttrCode())
            .attr(jpFile.getFileExtAttrCode())
            .auth(auth)
            .source(Source.USER)
            .where(
                Filter.attr(jpClass.getPrimaryKeyAttr()).eq(id.getId())
            )
            .build()
    );
    if (obj == null) {
      return null;
    }
    String fileName = obj.getAttrValue(jpAttr);
    String fileTitle = obj.getAttrValue(jpFile.getFileTitleAttrCode());
    if (fileTitle == null) {
      fileTitle = fileName;
    }
    String fileExt = obj.getAttrValue(jpFile.getFileExtAttrCode());

    String storageFilePath = obj.getAttrValue(jpFile.getStorageFilePathAttrCode());
    if (storageFilePath == null) {
      storageFilePath = jpFile.getStorageFilePath();
    }
    JPFileStorage fileStorage = (JPFileStorage) storage;
    FileInfo fileInfo = fileStorage.getInfo(storageFilePath, fileName);
    if (fileInfo == null) {
      return null;
    }
    return JPFileInfoBase.newBuilder()
        .storageCode(storageCode)
        .storageFilePath(storageFilePath)
        .storageFileName(fileName)
        .fileCode(fileName)
        .fileTitle(fileTitle)
        .fileExt(fileExt != null ? fileExt : getFileExtension(fileTitle))
        .fileSize(fileInfo.getLength())
        .fileDate(fileInfo.getCreatedTime())
        .build();
  }

  private String getFileExtension(String fileTitle) {
    if (fileTitle == null) {
      return null;
    }
    int dotIndex = fileTitle.lastIndexOf('.');
    return dotIndex == -1 ? "" : fileTitle.substring(dotIndex + 1);
  }
}
