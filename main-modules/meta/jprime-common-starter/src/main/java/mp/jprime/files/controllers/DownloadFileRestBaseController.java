package mp.jprime.files.controllers;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.files.DownloadFile;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPObjectNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.files.JPFileInfo;
import mp.jprime.files.JPIdFileInfo;
import mp.jprime.files.beans.FileInfo;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.parsers.ParserService;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.repositories.JPFileLoader;
import mp.jprime.repositories.JPFileStorage;
import mp.jprime.repositories.JPFileUploader;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.streams.UploadInputStream;
import mp.jprime.streams.services.UploadInputStreamService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Базовый класс для скачивания файлов
 */
public abstract class DownloadFileRestBaseController implements DownloadFile, JPReactiveObjectRepositoryServiceAware {
  protected static final Logger LOG = LoggerFactory.getLogger(DownloadFileRestBaseController.class);

  private final static DateTimeFormatter TS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH-mm");
  /**
   * Поле в multipart запросе, содержащее json с данными для создания/удаления
   */
  private static final String JSON_BODY_FIELD = "body";

  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPReactiveObjectRepositoryService repo;
  /**
   * Загрузка файлов объекта
   */
  private JPFileUploader jpFileUploader;
  /**
   * Работа с UploadInputStream
   */
  private UploadInputStreamService uploadInputStreamService;
  /**
   * Описание всех хранилищ системы
   */
  private RepositoryGlobalStorage repositoryStorage;
  /**
   * Парсер типов
   */
  protected ParserService parserService;
  /**
   * Выгрузка файлов объекта
   */
  protected JPFileLoader jpFileLoader;
  /**
   * Обработчик JWT
   */
  protected JWTService jwtService;
  /**
   * Формирование JsonJPObject
   */
  protected JsonJPObjectService jsonJPObjectService;
  /**
   * Фильтр меты
   */
  protected JPMetaFilter jpMetaFilter;

  @Autowired
  private void setUploadInputStreamService(UploadInputStreamService uploadInputStreamService) {
    this.uploadInputStreamService = uploadInputStreamService;
  }

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Override
  public void setJpReactiveObjectRepositoryService(JPReactiveObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setJpFileLoader(JPFileLoader jpFileLoader) {
    this.jpFileLoader = jpFileLoader;
  }

  @Autowired
  private void setJpFileUploader(JPFileUploader jpFileUploader) {
    this.jpFileUploader = jpFileUploader;
  }

  @Autowired
  private void setJsonJPObjectService(JsonJPObjectService jsonJPObjectService) {
    this.jsonJPObjectService = jsonJPObjectService;
  }

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @Autowired
  private void setRepositoryStorage(RepositoryGlobalStorage repositoryStorage) {
    this.repositoryStorage = repositoryStorage;
  }

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  protected RepositoryGlobalStorage getRepositoryStorage() {
    return repositoryStorage;
  }


  protected Mono<Void> writeTo(ServerWebExchange swe, JPFileInfo<?> info, String userAgent) {
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
    return writeTo(
        swe, storage.read(path, fileName), info.getFileTitle(), fileInfo.getLength(), fileInfo.getContentType(), userAgent
    );
  }

  protected Mono<Void> writeZipTo(ServerWebExchange swe, Collection<JPIdFileInfo> infoList, String userAgent) {
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

  private Mono<InputStream> getIs(Collection<JPIdFileInfo> list, AtomicReference<Path> tmpFileRef) {
    return Mono.create(sink -> {
      try {
        Path tmpFile = Files.createTempFile("download", "zip");
        tmpFileRef.set(tmpFile);
        try (FileOutputStream fos = new FileOutputStream(tmpFile.toFile())) {
          try (ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            Map<String, Integer> fileCounter = new HashMap<>();
            for (JPIdFileInfo info : list) {
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
                String title = parserService.parseTo(String.class, info.getJPId().getId()) + "_" + info.getFileTitle();
                Integer i = fileCounter.get(title);
                if (i == null) {
                  i = 0;
                } else {
                  i = i + 1;
                }
                fileCounter.put(title, i);

                zipOut.putNextEntry(new ZipEntry((i > 0 ? i + "_" : "") + title));

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

  protected Mono<JsonJPObject> createObject(ServerWebExchange swe, String code) {
    return save(swe, code,
        (jpClass, query, auth) -> {
          JPCreate.Builder jpCreateBuilder;
          try {
            jpCreateBuilder = queryService.getCreate(query, Source.USER, auth);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (!jpClass.getCode().equals(jpCreateBuilder.getJpClass())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          return jpCreateBuilder;
        },
        (tuple) -> {
          JPCreate.Builder builder = tuple.getT1();

          tuple.getT2().forEach((attr, value) -> {
            try (UploadInputStream is = value) {
              jpFileUploader.upload(builder, attr, is.getName(), is.getInputStream());
            }
          });
          return builder;
        },
        builder -> repo.asyncCreateAndGet(builder.build()));
  }

  public Mono<JsonJPObject> updateObject(ServerWebExchange swe, String code) {
    return save(swe, code,
        (jpClass, query, auth) -> {
          JPUpdate.Builder jpUpdateBuilder;
          try {
            jpUpdateBuilder = queryService.getUpdate(query, Source.USER, auth);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (jpUpdateBuilder.getJpId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (!jpClass.getCode().equals(jpUpdateBuilder.getJpClass())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          return jpUpdateBuilder;
        },
        (tuple) -> {
          JPUpdate.Builder builder = tuple.getT1();

          tuple.getT2().forEach((attr, value) -> {
            try (UploadInputStream is = value) {
              jpFileUploader.upload(builder, attr, is.getName(), is.getInputStream());
            }
          });
          return builder;
        },
        builder -> repo.asyncUpdateAndGet(builder.build()));
  }

  @FunctionalInterface
  public interface Builder<T> {
    T build(JPClass jpClass, String query, AuthInfo auth);
  }

  @FunctionalInterface
  public interface AppendUpload<T> {
    T append(Tuple2<T, Map<String, UploadInputStream>> tuple);
  }

  @FunctionalInterface
  public interface Executor<T> {
    Mono<JPObject> append(T builder);
  }

  protected Mono<Void> upload(FilePart file, JPCreate.Builder builder, String attr,
                              Consumer<JPCreate.Builder> executor) {
    return getStreamValue(file)
        .map(value-> {
          try (UploadInputStream is = value) {
            jpFileUploader.upload(builder, attr, is.getName(), is.getInputStream());
          }
          executor.accept(builder);
          return true;
        })
        .then();
  }

  private <T> Mono<JsonJPObject> save(ServerWebExchange swe, String code,
                                      Builder<T> builderFunc, AppendUpload<T> uploadFunc,
                                      Executor<T> executorFunc) {
    AuthInfo auth = jwtService.getAuthInfo(swe);

    return JPMono.fromCallable(
            () -> jpMetaFilter.get(code, auth)
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (jpClass == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(jpClass -> {
              Mono<Map<String, Part>> cache = swe.getMultipartData()
                  .map(MultiValueMap::toSingleValueMap)
                  .cache();
              return Mono.zip(
                  Flux.from(cache)
                      .map(x -> x.get(JSON_BODY_FIELD))
                      .flatMap(
                          x -> x.content()
                              .collect(
                                  () -> new UploadInputStream(x.name()),
                                  (t, dataBuffer) -> t.collectInputStream(dataBuffer.asInputStream(true))
                              )
                      )
                      .map(x -> {
                        try (InputStream v = x.getInputStream()) {
                          return IOUtils.toString(v, StandardCharsets.UTF_8);
                        } catch (Exception e) {
                          return "";
                        }
                      })
                      .map(x -> builderFunc.build(jpClass, x, auth))
                      .singleOrEmpty(),
                  Flux.from(cache)
                      .flatMapIterable(Map::values)
                      .filter(x -> !JSON_BODY_FIELD.equals(x.name()))
                      .filter(x -> x instanceof FilePart)
                      .cast(FilePart.class)
                      .flatMap(x -> Mono.zip(Mono.just(x.name()), getStreamValue(x)))
                      .collectMap(Tuple2::getT1, Tuple2::getT2)
              );
            }
        )
        .flatMap(tuple -> JPMono.fromCallable(() -> uploadFunc.append(tuple)))
        .flatMap(builder -> executorFunc.append(builder)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .map(object -> jsonJPObjectService.toJsonJPObject(object, swe))
        );
  }

  private Mono<UploadInputStream> getStreamValue(FilePart part) {
    return Mono.just(part)
        .flatMap(uploadInputStreamService::read);
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
