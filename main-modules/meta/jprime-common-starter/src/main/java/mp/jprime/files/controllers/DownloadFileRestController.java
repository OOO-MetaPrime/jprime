package mp.jprime.files.controllers;

import mp.jprime.controllers.DownloadFile;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.files.JPFileInfo;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Базовый класс для скачивания файлов
 */
public abstract class DownloadFileRestController implements DownloadFile {
  protected static final Logger LOG = LoggerFactory.getLogger(DownloadFileRestController.class);

  private final static DateTimeFormatter TS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH-mm");

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

  protected Mono<Void> writeTo(ServerWebExchange swe, JPFileInfo info, String userAgent) {
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
    return writeTo(swe, storage.read(path, fileName), info.getFileTitle(), fileInfo.getLength(), fileInfo.getContentType(),
        userAgent);
  }

  protected Mono<Void> writeZipTo(ServerWebExchange swe, Collection<JPFileInfo> infoList, String userAgent) {
    AtomicReference<Path> tmpFileRef = new AtomicReference<>();
    return Mono.just(infoList)
        .flatMap(list -> getIs(list, tmpFileRef))
        .flatMap(is -> {
          String ts = LocalDateTime.now().format(TS_FORMATTER);
          return writeTo(swe, is, "Архив." + ts + ".zip", null, "application/zip", userAgent);
        })
        .doFinally(response ->
            delete(tmpFileRef.get())
        );
  }

  private Mono<InputStream> getIs(Collection<JPFileInfo> list, AtomicReference<Path> tmpFileRef) {
    return Mono.create(sink -> {
      try {
        Path tmpFile = Files.createTempFile("download", "zip");
        tmpFileRef.set(tmpFile);
        try (FileOutputStream fos = new FileOutputStream(tmpFile.toFile())) {
          try (ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            Map<String, Integer> fileCounter = new HashMap<>();
            for (JPFileInfo info : list) {
              JPFileStorage storage = (JPFileStorage) repositoryStorage.getStorage(info.getStorageCode());
              if (storage == null) {
                continue;
              }
              String path = info.getStorageFilePath();
              String fileName = info.getStorageFileName();
              try (InputStream is = storage.read(path, fileName)) {
                if (is == null) {
                  continue;
                }
                String title = info.getFileTitle();
                Integer i = fileCounter.get(title);
                if (i == null) {
                  i = 0;
                } else {
                  i = i + 1;
                }
                fileCounter.put(title, i);

                ZipEntry zipEntry = new ZipEntry((i > 0 ? i + "_" : "") + title);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                  zipOut.write(bytes, 0, length);
                }
              }
            }
          }
        }
        sink.success(Files.newInputStream(tmpFile));
        sink.onCancel(() ->
            delete(tmpFileRef.get())
        );
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        sink.error(new JPRuntimeException(e));
      }
    });
  }

  private void delete(Path file) {
    if (file == null) {
      return;
    }
    try {
      Files.deleteIfExists(file);
    } catch (Exception e) {
      file.toFile().deleteOnExit();
      LOG.error(e.getMessage(), e);
      throw new JPRuntimeException(e);
    }
  }
}
