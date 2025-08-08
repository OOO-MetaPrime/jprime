package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPNotFoundException;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.JPIdFileInfo;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.files.beans.JPIdFileInfoBean;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.JPFileLoader;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовая реализация JPFileLoader
 */
@Service
public class JPFileCommonLoader implements JPFileLoader, JPObjectRepositoryServiceAware {
  private JPObjectRepositoryService repo;
  private JPMetaStorage metaStorage;
  private RepositoryGlobalStorage repositoryStorage;

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public Collection<JPIdFileInfo> getInfos(String classCode, Filter filter, String attr, AuthInfo auth) {
    return getInfos(classCode, filter, attr, auth, Source.USER);
  }


  @Override
  public Collection<JPIdFileInfo> getInfos(String classCode, Filter filter, String attr) {
    return getInfos(classCode, filter, attr, null, Source.SYSTEM);
  }

  private Collection<JPIdFileInfo> getInfos(String classCode, Filter filter, String attr, AuthInfo auth, Source source) {
    if (classCode == null || attr == null) {
      return Collections.emptyList();
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
            .source(source)
            .where(
                filter
            )
            .build()
    );
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    return list.stream()
        .map(obj -> {
          try {
            return toJPFileInfo(obj, info.jpAttr, info.fileStorage);
          } catch (JPNotFoundException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public JPIdFileInfo getInfo(JPId id, String attr) {
    return getInfo(id, null, attr, null, Source.SYSTEM);
  }

  @Override
  public JPIdFileInfo getInfo(JPId id, Filter filter, String attr, AuthInfo auth) {
    return getInfo(id, filter, attr, auth, Source.USER);
  }

  private JPIdFileInfo getInfo(JPId id, Filter filter, String attr, AuthInfo auth, Source source) {
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
            .source(source)
            .where(
                Filter.and(
                    Filter.attr(info.jpClass.getPrimaryKeyAttr()).eq(id.getId()),
                    filter
                )
            )
            .build()
    );
    if (obj == null) {
      return null;
    }
    return toJPFileInfo(obj, info.jpAttr, info.fileStorage);
  }

  @Override
  public InputStream getInputStream(JPFileInfo<?> info) {
    if (info == null) {
      return null;
    }
    JPFileStorage storage = getJpFileStorage(info.getStorageCode());
    String path = info.getStorageFilePath();
    String fileName = info.getStorageFileName();
    FileInfo fileInfo = storage.getInfo(path, fileName);
    if (fileInfo == null) {
      return null;
    }
    return storage.read(path, fileName);
  }

  private JPFileStorage getJpFileStorage(String storageCode) {
    JPFileStorage storage = (JPFileStorage) repositoryStorage.getStorage(storageCode);
    if (storage == null) {
      throw new JPRepositoryNotFoundException(storageCode);
    }
    return storage;
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

  private static class Info {
    private final JPClass jpClass;
    private final JPAttr jpAttr;
    private final JPFileStorage fileStorage;

    private Info(JPClass jpClass, JPAttr jpAttr, JPFileStorage fileStorage) {
      this.jpClass = jpClass;
      this.jpAttr = jpAttr;
      this.fileStorage = fileStorage;
    }
  }

  private JPIdFileInfo toJPFileInfo(JPObject obj, JPAttr jpAttr, JPFileStorage fileStorage) {
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
    return JPIdFileInfoBean.newBuilder(obj.getJpId())
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
