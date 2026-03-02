package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.JPObjectAccessServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPSave;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.files.JPFileCommonInfo;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.files.beans.JPFileCommonInfoBean;
import mp.jprime.globalsettings.JPGlobalSettingsService;
import mp.jprime.globalsettings.enums.JPGlobalSettings;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPFileUploader;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.streams.UploadInputStream;
import mp.jprime.utils.JPStringUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.input.BoundedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Базовая реализация JPFileUploader
 */
@Service
public class JPFileCommonUploader implements JPFileUploader, JPObjectAccessServiceAware {
  private static final Logger LOG = LoggerFactory.getLogger(JPFileCommonUploader.class);

  @Value("${jprime.file.precheck.enabled:false}")
  private boolean precheck;
  @Value("${jprime.file.precheck.storage:}")
  private String precheckFs;
  private Collection<String> ignoreArchiveExtList;

  @Value("${jprime.file.archiveCheck.ignoreExtList:docx,xlsx,pptx}")
  private void setIgnoreExtList(String[] ignoreExtList) {
    this.ignoreArchiveExtList = ignoreExtList != null ? Arrays.stream(ignoreExtList)
        .map(String::toLowerCase)
        .collect(Collectors.toSet()) : Collections.emptySet();
  }

  private JPGlobalSettingsService globalSettingsService;
  private JPMetaStorage metaStorage;
  private JPObjectAccessService objectAccessService;
  private JPSecurityStorage securityManager;
  private RepositoryGlobalStorage repositoryStorage;

  @Autowired
  private void setGlobalSettingsService(JPGlobalSettingsService globalSettingsService) {
    this.globalSettingsService = globalSettingsService;
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

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Override
  public JPFileCommonInfo upload(String storageCode, String storagePath, String fileName, InputStream is) {
    JPFileStorage storage = getStorage(storageCode);
    if (is == null || storage == null) {
      return JPFileCommonInfoBean.newBuilder().build();
    }
    return upload(storage, storagePath, fileName, null, is);
  }

  private JPFileStorage getStorage(String storageCode) {
    JPStorage storage = repositoryStorage.getStorage(storageCode);
    if (!(storage instanceof JPFileStorage fileStorage)) {
      return null;
    }
    return fileStorage;
  }

  private JPFileCommonInfo upload(JPFileStorage storage, String storagePath, String fileName, String fileNamePath, InputStream is) {
    if (is == null || storage == null) {
      return JPFileCommonInfoBean.newBuilder().build();
    }

    String fileExt = getFileExtension(fileName);
    String fileCode = getNewFileCode() + "." + fileExt;
    if (fileNamePath != null) {
      fileCode = fileNamePath.concat("/").concat(fileCode);
    }

    FileInfo fileInfo = storage.save(storagePath, fileCode, is);

    JPFileCommonInfoBean.Builder builder = JPFileCommonInfoBean.newBuilder()
        .storageCode(storage.getCode())
        .storageFilePath(storagePath)
        .storageFileName(fileCode)
        .fileCode(fileCode)
        .fileTitle(fileName)
        .fileExt(fileExt);
    if (fileInfo != null) {
      builder.fileSize(fileInfo.getLength());
      builder.fileDate(fileInfo.getCreatedTime());
    }
    return builder.build();
  }

  @Override
  public JPCreate.Builder upload(JPCreate.Builder builder, Map<String, UploadInputStream> attrStreams) {
    if (attrStreams == null || attrStreams.isEmpty()) {
      return builder;
    }
    check(attrStreams, builder.getSource());

    attrStreams.forEach((attr, value) -> {
      try (UploadInputStream is = value) {
        upload(builder, attr, is.getName(), is.getInputStream());
      }
    });
    return builder;
  }

  @Override
  public JPCreate.Builder upload(JPCreate.Builder builder, String attr, String fileName, InputStream is) {
    return fileUpload(builder, attr, fileName, is, (jpClass, jpAttr) -> {
      AuthInfo auth = builder.getAuth();
      if (builder.getSource() != Source.USER) {
        return true;
      }
      return objectAccessService.checkCreate(jpClass.getCode(), auth) &&
          (auth == null || securityManager.checkCreate(jpAttr.getJpPackage(), auth.getRoles()));
    });
  }

  @Override
  public JPUpdate.Builder upload(JPUpdate.Builder builder, Map<String, UploadInputStream> attrStreams) {
    if (attrStreams == null || attrStreams.isEmpty()) {
      return builder;
    }
    check(attrStreams, builder.getSource());

    attrStreams.forEach((attr, value) -> {
      try (UploadInputStream is = value) {
        upload(builder, attr, is.getName(), is.getInputStream());
      }
    });
    return builder;
  }

  @Override
  public JPUpdate.Builder upload(JPUpdate.Builder builder, String attr, String fileName, InputStream is) {
    return fileUpload(builder, attr, fileName, is, (jpClass, jpAttr) -> {
      AuthInfo auth = builder.getAuth();
      if (builder.getSource() != Source.USER) {
        return true;
      }
      return objectAccessService.checkUpdate(builder.getJpId(), auth) &&
          (auth == null || securityManager.checkUpdate(jpAttr.getJpPackage(), auth.getRoles()));
    });
  }

  @Override
  public String getStoragePath(String path) {
    if (path == null || path.isEmpty()) {
      return path;
    }
    return JPStringUtils.applyDataTimeTemplate(path);
  }

  private Long getMaxFileSize() {
    Integer maxFileSize = globalSettingsService.getValue(JPGlobalSettings.FILES_LIMITS_MAX_FILE_SIZE.getCode());
    // В байтах
    return maxFileSize != null && maxFileSize > 0 ? maxFileSize.longValue() * 1024 * 1024 : null;
  }

  private void check(Map<String, UploadInputStream> attrStreams, Source source) {
    if (source != Source.USER) {
      return;
    }

    Integer maxUploadFileCount = globalSettingsService.getValue(JPGlobalSettings.FILES_LIMITS_MAX_UPLOAD_FILE_COUNT.getCode());
    if (maxUploadFileCount != null && maxUploadFileCount > 0 && maxUploadFileCount < attrStreams.size()) {
      throw new JPAppRuntimeException("mp.jprime.upload.limit.maxUploadFileCount.error", "Превышен лимит одновременно загружаемых файлов");
    }

    Long maxFileSize = getMaxFileSize();
    if (maxFileSize != null) {
      for (UploadInputStream uis : attrStreams.values()) {
        Long bytes = uis.getBytes();
        if (bytes != null && maxFileSize < bytes) {
          throw new JPAppRuntimeException("mp.jprime.upload.limit.maxFileSize.error", "Превышен разрешенный размер загружаемого файла");
        }
      }
    }
  }

  private InputStream wrapAndCheck(InputStream is, Source source) {
    if (source != Source.USER) {
      return is;
    }

    Long maxFileSize = getMaxFileSize();
    if (maxFileSize == null) {
      return is;
    }

    try {
      return BoundedInputStream.builder()
          .setInputStream(is)
          .setMaxCount(maxFileSize + 1) // Ошибка идет при достижении setMaxCount
          .setOnMaxCount((x, y) -> {
            throw new JPAppRuntimeException("mp.jprime.upload.limit.maxFileSize.error", "Превышен разрешенный размер загружаемого файла");
          })
          .get();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private void checkArchive(JPFileStorage storage, JPFileCommonInfo fileInfo, Source source) {
    if (source != Source.USER) {
      return;
    }

    if (ignoreArchiveExtList.contains(fileInfo.getFileExt().toLowerCase())) {
      return;
    }

    Long fileMaxSize = getMaxFileSize();
    Integer maxUploadFileCount = globalSettingsService.getValue(JPGlobalSettings.FILES_LIMITS_MAX_UPLOAD_FILE_COUNT.getCode());
    Integer maxArchiveFilesSize = globalSettingsService.getValue(JPGlobalSettings.FILES_LIMITS_MAX_ARCHIVE_FILES_SIZE.getCode());

    if ((fileMaxSize == null || fileMaxSize == 0) &&
        (maxUploadFileCount == null || maxUploadFileCount == 0) &&
        (maxArchiveFilesSize == null || maxArchiveFilesSize == 0)) {
      return;
    }

    AtomicInteger fileCount = new AtomicInteger(0);
    AtomicLong totalSize = new AtomicLong(0L);

    boolean isNotCompress = false;
    try (InputStream is = storage.read(fileInfo.getStorageFilePath(), fileInfo.getStorageFileName());
         BufferedInputStream bis = new BufferedInputStream(is);
         CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(bis);
         BufferedInputStream bisCis = new BufferedInputStream(cis);
         ArchiveInputStream<? extends ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(bisCis)) {
      calc(ais, fileMaxSize, fileCount, totalSize);
    } catch (ArchiveException e) {
      // Все хорошо, не архив
      return;
    } catch (CompressorException e) {
      isNotCompress = true;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }

    if (isNotCompress) {
      try (InputStream is = storage.read(fileInfo.getStorageFilePath(), fileInfo.getStorageFileName());
           BufferedInputStream bis = new BufferedInputStream(is);
           ArchiveInputStream<? extends ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(bis)) {
        calc(ais, fileMaxSize, fileCount, totalSize);
      } catch (ArchiveException e) {
        // Все хорошо, не архив
        return;
      } catch (IOException e) {
        throw JPRuntimeException.wrapException(e);
      }
    }

    try {
      if (maxUploadFileCount != null && maxUploadFileCount > 0 && maxUploadFileCount < fileCount.get()) {
        throw new JPAppRuntimeException("mp.jprime.upload.limit.maxUploadFileCount.error", "Превышен лимит одновременно загружаемых файлов");
      }
      if (maxArchiveFilesSize != null && maxArchiveFilesSize > 0 && maxArchiveFilesSize * 1024 * 1024 < totalSize.get()) {
        throw new JPAppRuntimeException("mp.jprime.upload.limit.maxArchiveFilesSize.error", "Превышен максимальный размер загружаемых файлов в архиве");
      }
    } catch (Exception e) {
      storage.delete(fileInfo.getStorageFilePath(), fileInfo.getStorageFileName());
      throw e;
    }
  }

  private void calc(ArchiveInputStream<? extends ArchiveEntry> ais,
                    Long fileMaxSize,
                    AtomicInteger fileCount,
                    AtomicLong totalSize) throws IOException {
    ArchiveEntry entry;
    while ((entry = ais.getNextEntry()) != null) {
      if (entry.isDirectory()) {
        continue;
      }
      long fileSize = entry.getSize();

      if (fileMaxSize != null && fileMaxSize > 0 && fileMaxSize < fileSize) {
        throw new JPAppRuntimeException("mp.jprime.upload.limit.maxArchiveFilesSize.error", "Превышен разрешенный размер загружаемого в архиве файла");
      }

      fileCount.addAndGet(1);
      totalSize.addAndGet(fileSize);
    }
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
    Source source = builder.getSource();
    String storageCode = jpFile.getStorageCode();
    String storageFilePath = jpFile.getStorageFilePath();

    SplitFilePathTemplateRecord pathTemplate = splitFilePathTemplate(storageFilePath);
    String storagePath;
    String fileNamePath = null;

    if (pathTemplate != null) {
      String storageTemplate = pathTemplate.storageTemplate();
      storagePath = getStoragePath(storageTemplate);

      String fileNameTemplate = pathTemplate.fileNameTemplate();
      if (fileNameTemplate != null) {
        fileNamePath = JPStringUtils.applyDataTimeTemplate(fileNameTemplate);
      }
    } else {
      storagePath = getStoragePath(storageFilePath);
    }

    JPFileCommonInfo fileInfo;

    JPFileStorage storage = getStorage(storageCode);

    boolean userPrecheck = precheck && source == Source.USER;
    JPFileStorage precheckStorage = userPrecheck ? getStorage(precheckFs) : null;
    if (storage != null) {
      JPFileStorage saveStorage = userPrecheck ? precheckStorage : storage;
      // Загружаем
      fileInfo = upload(saveStorage, storagePath, fileName, fileNamePath, wrapAndCheck(is, source));

      try {
        // Проверяем загруженный архив
        checkArchive(saveStorage, fileInfo, source);

        if (userPrecheck && saveStorage != null) {
          String path = fileInfo.getStorageFilePath();
          String tmpFileName = fileInfo.getStorageFileName();
          // Перегружаем в нормальное хранилище
          fileInfo = upload(storage, storagePath, fileName, fileNamePath, saveStorage.read(path, tmpFileName));
          saveStorage.delete(path, tmpFileName);
        }
      } catch (JPAppRuntimeException e) {
        throw e;
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw new JPAppRuntimeException("fileUpload.precheck.error", "Отклонена загрузка файлов, представляющих потенциальную угрозу. Повторите попытку");
      }
    } else {
      fileInfo = JPFileCommonInfoBean.newBuilder().build();
    }

    builder.setSystem(attr, fileInfo.getFileCode());
    builder.setSystem(jpFile.getStorageCodeAttrCode(), fileInfo.getStorageCode());
    builder.setSystem(jpFile.getStorageFilePathAttrCode(), fileInfo.getStorageFilePath());
    builder.setSystem(jpFile.getFileTitleAttrCode(), fileInfo.getFileTitle());
    builder.setSystem(jpFile.getFileExtAttrCode(), fileInfo.getFileExt());
    builder.setSystem(jpFile.getFileSizeAttrCode(), fileInfo.getFileSize());
    builder.setSystem(jpFile.getFileDateAttrCode(), fileInfo.getFileDate());
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

  private SplitFilePathTemplateRecord splitFilePathTemplate(String storagePath) {
    if (storagePath == null || storagePath.isEmpty()) {
      return null;
    }
    String[] pathParts =  storagePath.split("/", 2);
    String filePart = pathParts.length == 2 ? pathParts[1] : null;
    String fileNameTemplate = filePart != null && !filePart.isBlank() ? filePart : null;

    return new SplitFilePathTemplateRecord(pathParts[0], fileNameTemplate);
  }

  private record SplitFilePathTemplateRecord(String storageTemplate, String fileNameTemplate) {
  }
}
