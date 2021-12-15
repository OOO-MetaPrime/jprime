package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовая реализация JPFileLoader
 */
@Service
public class JPFileCommonLoader implements JPFileLoader, JPObjectRepositoryServiceAware {
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

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
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
  public Collection<JPFileInfo> getInfos(String classCode, Filter filter, String attr, AuthInfo auth) {
    if (classCode == null || attr == null) {
      return null;
    }
    Info info = info(classCode, attr);
    if (info == null) {
      return Collections.emptyList();
    }
    JPFile jpFile = info.jpAttr.getRefJpFile();

    Collection<JPObject> list = repo.getList(
        JPSelect.from(classCode)
            .attr(info.jpAttr)
            .attr(jpFile.getStorageFilePathAttrCode())
            .attr(jpFile.getFileTitleAttrCode())
            .attr(jpFile.getFileExtAttrCode())
            .auth(auth)
            .source(Source.USER)
            .where(
                filter
            )
            .build()
    );
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    return list.stream()
        .map(obj -> toJPFileInfo(obj, info.jpAttr, info.fileStorage))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public JPFileInfo getInfo(JPId id, String attr, AuthInfo auth) {
    if (id == null || attr == null) {
      return null;
    }
    Info info = info(id.getJpClass(), attr);
    if (info == null) {
      return null;
    }
    JPFile jpFile = info.jpAttr.getRefJpFile();

    JPObject obj = repo.getObject(
        JPSelect.from(id.getJpClass())
            .attr(info.jpAttr)
            .attr(jpFile.getStorageFilePathAttrCode())
            .attr(jpFile.getFileTitleAttrCode())
            .attr(jpFile.getFileExtAttrCode())
            .auth(auth)
            .source(Source.USER)
            .where(
                Filter.attr(info.jpClass.getPrimaryKeyAttr()).eq(id.getId())
            )
            .build()
    );
    if (obj == null) {
      return null;
    }
    return toJPFileInfo(obj, info.jpAttr, info.fileStorage);
  }

  private Info info(String classCode, String fileAttr) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      return null;
    }
    JPAttr jpAttr = jpClass.getAttr(fileAttr);
    JPFile jpFile = jpAttr != null ? jpAttr.getRefJpFile() : null;
    if (jpAttr == null || jpAttr.getType() != JPType.FILE || jpFile == null) {
      return null;
    }
    String storageCode = jpFile.getStorageCode();
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (!(storage instanceof JPFileStorage)) {
      return null;
    }
    return new Info(jpClass, jpAttr, (JPFileStorage) storage);
  }

  private class Info {
    private JPClass jpClass;
    private JPAttr jpAttr;
    private JPFileStorage fileStorage;

    private Info(JPClass jpClass, JPAttr jpAttr, JPFileStorage fileStorage) {
      this.jpClass = jpClass;
      this.jpAttr = jpAttr;
      this.fileStorage = fileStorage;
    }
  }

  private JPFileInfo toJPFileInfo(JPObject obj, JPAttr jpAttr, JPFileStorage fileStorage) {
    JPFile jpFile = jpAttr.getRefJpFile();
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
    FileInfo fileInfo = storageFilePath != null && fileName != null ? fileStorage.getInfo(storageFilePath, fileName) : null;
    if (fileInfo == null) {
      return null;
    }
    return JPFileInfoBase.newBuilder()
        .storageCode(fileStorage.getCode())
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
