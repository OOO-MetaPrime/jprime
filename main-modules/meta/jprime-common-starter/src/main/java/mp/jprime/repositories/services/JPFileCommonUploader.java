package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.JPObjectAccessServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.dataaccess.params.JPSave;
import mp.jprime.files.beans.JPFileInfoBase;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPFileUploader;
import mp.jprime.repositories.JPStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Базовая реализация JPFileUploader
 */
@Service
public class JPFileCommonUploader implements JPFileUploader, JPObjectAccessServiceAware {
  /**
   * API для работы с хранилищами
   */
  private RepositoryStorage repositoryStorage;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  // Интерфейс проверки доступа к объекту
  private JPObjectAccessService objectAccessService;
  // Хранилище настроек RBAC
  private JPSecurityStorage securityManager;

  @Autowired
  private void setRepositoryStorage(RepositoryStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  /**
   * Загружает файл и сохраняет данные в JPFileInfo
   *
   * @param storageCode Код хранилища
   * @param storagePath Путь в хранилище
   * @param fileName    Имя файла
   * @param is          InputStream
   * @return JPFileInfo
   */
  @Override
  public JPFileInfo upload(String storageCode, String storagePath, String fileName, InputStream is) {
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (!(storage instanceof JPFileStorage)) {
      return JPFileInfoBase.newBuilder().build();
    }
    String fileExt = getFileExtension(fileName);
    String fileCode = getNewFileCode() + "." + fileExt;

    JPFileInfoBase.Builder builder = JPFileInfoBase.newBuilder();
    if (is != null) {
      JPFileStorage fileStorage = (JPFileStorage) storage;
      FileInfo fileInfo = fileStorage.save(storagePath, fileCode, is);

      builder
          .storageCode(storageCode)
          .storageFilePath(storagePath)
          .storageFileName(fileCode)
          .fileCode(fileCode)
          .fileTitle(fileName)
          .fileExt(fileExt);
      if (fileInfo != null) {
        builder.fileSize(fileInfo.getLength());
        builder.fileDate(fileInfo.getCreatedTime());
      }
    }
    return builder.build();
  }

  @Override
  public JPCreate.Builder upload(JPCreate.Builder builder, String attr, String fileName, InputStream is) {
    return fileUpload(builder, attr, fileName, is, (jpClass, jpAttr) -> {
      AuthInfo authInfo = builder.getAuth();
      if (builder.getSource() != Source.USER) {
        return true;
      }
      return objectAccessService.checkCreate(jpClass.getCode(), authInfo) &&
          (authInfo == null || securityManager.checkCreate(jpAttr.getJpPackage(), authInfo.getRoles()));
    });
  }

  @Override
  public JPUpdate.Builder upload(JPUpdate.Builder builder, String attr, String fileName, InputStream is) {
    return fileUpload(builder, attr, fileName, is, (jpClass, jpAttr) -> {
      AuthInfo authInfo = builder.getAuth();
      if (builder.getSource() != Source.USER) {
        return true;
      }
      return objectAccessService.checkUpdate(builder.getJpId(), authInfo) &&
          (authInfo == null || securityManager.checkUpdate(jpAttr.getJpPackage(), authInfo.getRoles()));
    });
  }


  private <T extends JPSave.Builder> T fileUpload(T builder, String attr, String fileName, InputStream is,
                                                  BiFunction<JPClass, JPAttr, Boolean> f) {
    JPClass jpClass = metaStorage.getJPClassByCode(builder.getJpClass());
    if (jpClass == null || fileName == null) {
      return builder;
    }
    JPAttr jpAttr = attr != null ? jpClass.getAttr(attr) : null;
    JPFile jpFile = jpAttr != null ? jpAttr.getRefJpFile() : null;
    if (jpAttr == null || jpAttr.getType() != JPType.FILE || jpFile == null) {
      return builder;
    }
    // Проверяем доступ
    if (!f.apply(jpClass, jpAttr)) {
      return builder;
    }
    String storageCode = jpFile.getStorageCode();
    String storagePath = jpFile.getStorageFilePath();

    JPFileInfo fileInfo = upload(storageCode, storagePath, fileName, is);
    builder.set(attr, fileInfo.getFileCode());
    builder.set(jpFile.getStorageCodeAttrCode(), storageCode);
    builder.set(jpFile.getStorageFilePathAttrCode(), storagePath);
    builder.set(jpFile.getFileTitleAttrCode(), fileInfo.getFileTitle());
    builder.set(jpFile.getFileExtAttrCode(), fileInfo.getFileExt());
    builder.set(jpFile.getFileSizeAttrCode(), fileInfo.getFileSize());
    builder.set(jpFile.getFileDateAttrCode(), fileInfo.getFileDate());
    return builder;
  }

  private String getFileExtension(String fileName) {
    if (fileName == null) {
      return null;
    }
    int dotIndex = fileName.lastIndexOf('.');
    return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
  }

  /**
   * Возвращает новый код файла
   *
   * @return Новый код файла
   */
  private String getNewFileCode() {
    return UUID.randomUUID().toString();
  }
}
