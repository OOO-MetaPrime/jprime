package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPObjectNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.files.controllers.DownloadFileRestController;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.JPFileLoader;
import mp.jprime.repositories.JPFileUploader;
import mp.jprime.rest.v1.Controllers;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.streams.UploadInputStream;
import mp.jprime.streams.services.UploadInputStreamService;
import mp.jprime.web.services.ServerWebExchangeService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/v1")
public class RestApiMultipartCRUDController extends DownloadFileRestController implements JPReactiveObjectRepositoryServiceAware {
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
   * Выгрузка файлов объекта
   */
  private JPFileLoader jpFileLoader;
  /**
   * Загрузка файлов объекта
   */
  private JPFileUploader jpFileUploader;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Методы работы с ServerWebExchangeService
   */
  private ServerWebExchangeService sweService;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;
  /**
   * Работа с UploadInputStream
   */
  private UploadInputStreamService uploadInputStreamService;

  /**
   * Признак добавления блока links
   */
  @Value("${jprime.api.addLinks:false}")
  private boolean addLinks;

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
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSweService(ServerWebExchangeService sweService) {
    this.sweService = sweService;
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

  @GetMapping(value = "/{code}/{objectId}/file/{attrCode}/{bearer}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFile(ServerWebExchange swe,
                                 @PathVariable("code") String code,
                                 @PathVariable("objectId") String objectId,
                                 @PathVariable("attrCode") String attrCode,
                                 @PathVariable("bearer") String bearer,
                                 @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.just(metaStorage.getJPClassByCodeOrPluralCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || objectId == null || jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(
            jpClass -> jpFileLoader.asyncGetInfo(JPId.get(jpClass.getCode(), objectId), attrCode, jwtService.getAuthInfo(bearer, swe))
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .flatMap(
            fileInfo -> writeTo(swe, fileInfo, userAgent)
        );
  }

  @GetMapping(value = "/{code}/search/file/{attrCode}/{linkCode}/{linkValue}/{bearer}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFilesZip(ServerWebExchange swe,
                                     @PathVariable("code") String code,
                                     @PathVariable("attrCode") String attrCode,
                                     @PathVariable("linkCode") String linkCode,
                                     @PathVariable("linkValue") String linkValue,
                                     @PathVariable("bearer") String bearer,
                                     @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.just(metaStorage.getJPClassByCodeOrPluralCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMapMany(
            jpClass -> jpFileLoader.asyncGetInfos(jpClass.getCode(), Filter.attr(linkCode).eq(linkValue), attrCode, jwtService.getAuthInfo(bearer, swe))
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .collectList()
        .flatMap(
            fileInfos -> writeZipTo(swe, fileInfos, userAgent)
        );
  }

  @ResponseBody
  @PostMapping(value = "/{code}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JsonJPObject> createObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody Flux<Part> parts) {
    return Mono.just(metaStorage.getJPClassByCodeOrPluralCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(jpClass -> {
              Flux<Part> cache = parts.cache();
              return Mono.zip(
                  Flux.from(cache)
                      .filter(x -> JSON_BODY_FIELD.equals(x.name()))
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
                      .map(x -> {
                        JPCreate.Builder jpCreateBuilder;
                        try {
                          AuthInfo authInfo = jwtService.getAuthInfo(swe);
                          jpCreateBuilder = queryService.getCreate(x, Source.USER, authInfo);
                        } catch (JPRuntimeException e) {
                          throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        }
                        if (!jpClass.getCode().equals(jpCreateBuilder.getJpClass())) {
                          throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        }
                        return jpCreateBuilder;
                      })
                      .singleOrEmpty(),
                  Flux.from(cache)
                      .filter(x -> !JSON_BODY_FIELD.equals(x.name()))
                      .filter(x -> x instanceof FilePart)
                      .cast(FilePart.class)
                      .flatMap(x -> Mono.zip(Mono.just(x.name()), getStreamValue(x)))
                      .collectMap(Tuple2::getT1, Tuple2::getT2)
              );
            }
        )
        .flatMap(tuple -> Mono.fromCallable(() -> {
              JPCreate.Builder builder = tuple.getT1();
              tuple.getT2().forEach((attr, value) -> {
                try (UploadInputStream is = value) {
                  jpFileUploader.upload(builder, attr, is.getName(), is.getInputStream());
                }
              });
              return builder;
            })
        )
        .flatMap(builder -> repo.asyncCreateAndGet(builder.build())
            .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .map(x -> JsonJPObject.newBuilder()
                .metaStorage(metaStorage)
                .jpObject(x)
                .baseUrl(sweService.getBaseUrl(swe))
                .restMapping(Controllers.API_MAPPING)
                .addLinks(addLinks)
                .build())
        );
  }

  @ResponseBody
  @PutMapping(value = "/{code}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> updateObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody Flux<Part> parts) {
    return Mono.just(metaStorage.getJPClassByCodeOrPluralCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(jpClass -> {
              Flux<Part> cache = parts.cache();
              return Mono.zip(
                  Flux.from(cache)
                      .filter(x -> JSON_BODY_FIELD.equals(x.name()))
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
                      .map(x -> {
                        JPUpdate.Builder jpUpdateBuilder;
                        try {
                          AuthInfo authInfo = jwtService.getAuthInfo(swe);
                          jpUpdateBuilder = queryService.getUpdate(x, Source.USER, authInfo);
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
                      })
                      .singleOrEmpty(),
                  Flux.from(cache)
                      .filter(x -> !JSON_BODY_FIELD.equals(x.name()))
                      .filter(x -> x instanceof FilePart)
                      .cast(FilePart.class)
                      .flatMap(x -> Mono.zip(Mono.just(x.name()), getStreamValue(x)))
                      .collectMap(Tuple2::getT1, Tuple2::getT2)
              );
            }
        )
        .flatMap(tuple -> Mono.fromCallable(() -> {
              JPUpdate.Builder builder = tuple.getT1();
              tuple.getT2().forEach((attr, value) -> {
                try (UploadInputStream is = value) {
                  jpFileUploader.upload(builder, attr, is.getName(), is.getInputStream());
                }
              });
              return builder;
            })
        )
        .flatMap(builder -> repo.asyncUpdateAndGet(builder.build())
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
            .map(x -> JsonJPObject.newBuilder()
                .metaStorage(metaStorage)
                .jpObject(x)
                .baseUrl(sweService.getBaseUrl(swe))
                .restMapping(Controllers.API_MAPPING)
                .addLinks(addLinks)
                .build())
        );
  }

  private Mono<UploadInputStream> getStreamValue(FilePart part) {
    return Mono.just(part)
        .flatMap(uploadInputStreamService::read);
  }
}
