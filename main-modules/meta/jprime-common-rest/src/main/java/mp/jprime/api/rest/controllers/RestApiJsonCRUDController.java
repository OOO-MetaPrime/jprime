package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.*;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPObjectNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.json.beans.JsonJPObjectList;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.rest.v1.Controllers;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.web.services.ServerWebExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("api/v1")
public class RestApiJsonCRUDController {
  private static final Logger LOG = LoggerFactory.getLogger(RestApiJsonCRUDController.class);

  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPReactiveObjectRepositoryService repo;
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

  @Value("${jprime.query.queryTimeout:}")
  private Integer queryTimeout;
  @Value("${jprime.api.checkLimit:true}")
  private boolean checkLimit;
  /**
   * Максимальное количество в выборке через api
   */
  @Value("${jprime.api.maxLimit:1000}")
  private Integer maxLimit;

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Autowired
  private void setRepo(JPReactiveObjectRepositoryService repo) {
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

  /**
   * Возвращает список
   *
   * @param builder JPSelect.Builder
   * @return Список
   */
  private Mono<JsonJPObjectList> getListResult(final JPSelect.Builder builder, final ServerWebExchange swe) {
    Integer limit = builder.limit();
    if (checkLimit && limit != null && limit > maxLimit) {
      LOG.error("Warning. Select query limit for " + builder.getJpClass() + " exceeded: " + limit);
      builder.limit(maxLimit);
    }
    JPSelect s = builder.build();

    JPClass jpClass = metaStorage.getJPClassByCode(s.getJpClass());
    return Mono.zip(
        // Общее количество
        s.isTotalCount() ? repo.getAsyncTotalCount(s) : Mono.just(0L),
        // Выборка
        repo.getAsyncList(s)
            .map(x -> JsonJPObject.newBuilder()
                .jpObject(x)
                .metaStorage(metaStorage)
                .baseUrl(sweService.getBaseUrl(swe))
                .restMapping(Controllers.API_MAPPING)
                .build())
            .collectList(),
        // Создаем результат
        (x, y) -> JsonJPObjectList.newBuilder()
            .limit(s.getLimit())
            .offset(s.getOffset())
            .classCode(jpClass.getCode())
            .pluralCode(jpClass.getPluralCode())
            .objects(y)
            .totalCount(x)
            .build())
        // Ошибка
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("pluralCode") String pluralCode,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit) {

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder = JPSelect
        .from(jpClass.getCode())
        .offset(offset != null ? offset : 0)
        .limit(limit != null ? limit : QueryService.MAX_LIMIT)
        .orderByDesc(jpClass.getPrimaryKeyAttr())
        .timeout(queryTimeout)
        .useDefaultJpAttrs(Boolean.TRUE)
        .source(Source.USER)
        .auth(auth);
    return getListResult(builder, swe);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("pluralCode") String pluralCode,
                                              @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    try {
      builder = queryService.getSelect(jpClass.getCode(), query, authInfo)
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, swe);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/search/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("pluralCode") String pluralCode,
                                              @PathVariable("attrCode") String attrCode,
                                              @PathVariable("attrValue") String attrValue,
                                              @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpClass == null || jpClass.isInner() || jpAttr == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    try {
      builder = queryService.getSelect(jpClass.getCode(), query, authInfo)
          .andWhere(Filter.attr(attrCode).eq(attrValue))
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, swe);
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getLinkList(ServerWebExchange swe,
                                            @PathVariable("pluralCode") String pluralCode,
                                            @PathVariable("objectId") String objectId,
                                            @PathVariable("attrCode") String attrCode,
                                            @PathVariable("attrValue") String attrValue) {
    return getSearchLinkList(swe, pluralCode, objectId, attrCode, attrValue, null);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchLinkList(ServerWebExchange swe,
                                                  @PathVariable("pluralCode") String pluralCode,
                                                  @PathVariable("objectId") String objectId,
                                                  @PathVariable("attrCode") String attrCode,
                                                  @PathVariable("attrValue") String attrValue,
                                                  @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpAttr == null || jpAttr.getRefJpClassCode() == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    JPClass refClass = metaStorage.getJPClassByCode(jpAttr.getRefJpClassCode());
    JPAttr refAttr = refClass != null && jpAttr.getRefJpAttrCode() != null ?
        refClass.getAttr(jpAttr.getRefJpAttrCode()) : null;
    if (refAttr == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    try {
      builder = queryService.getSelect(jpAttr.getRefJpClassCode(), query, authInfo)
          .andWhere(Filter.attr(jpAttr.getRefJpAttrCode()).eq(attrValue))
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(refClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, swe);
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getBackReferenceList(ServerWebExchange swe,
                                                     @PathVariable("pluralCode") String pluralCode,
                                                     @PathVariable("objectId") String objectId,
                                                     @PathVariable("attrCode") String attrCode) {
    return getSearchBackReferenceList(swe, pluralCode, objectId, attrCode, null);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchBackReferenceList(ServerWebExchange swe,
                                                           @PathVariable("pluralCode") String pluralCode,
                                                           @PathVariable("objectId") String objectId,
                                                           @PathVariable("attrCode") String attrCode,
                                                           @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    JPAttr jpAttr = jpClass == null || jpClass.isInner() ? null : jpClass.getAttr(attrCode);
    JPClass refJpClass = jpAttr == null || jpAttr.getRefJpClassCode() == null || jpAttr.getType() != JPType.BACKREFERENCE
        ? null : metaStorage.getJPClassByCode(jpAttr.getRefJpClassCode());
    JPAttr refJpAttr = refJpClass == null ? null : refJpClass.getAttr(jpAttr.getRefJpAttrCode());
    JPAttr targetAttr = refJpAttr == null ? null : jpClass.getAttr(refJpAttr.getRefJpAttrCode());
    if (objectId == null || targetAttr == null || !jpClass.getCode().equals(refJpAttr.getRefJpClassCode())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Mono.just(objectId)
        .flatMap(key -> {
              if (targetAttr.isIdentifier()) {
                return Mono.just(key).cast(Object.class);
              } else {
                return repo.getAsyncObject(
                    JPSelect.from(jpClass.getCode())
                        .attr(targetAttr.getCode())
                        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(key))
                        .build()
                )
                    .filter(Objects::nonNull)
                    .map(o -> o.getAttrValue(targetAttr.getCode()));
              }
            }
        )
        .flatMap(key -> {
          AuthInfo authInfo = jwtService.getAuthInfo(swe);
          JPSelect.Builder builder;
          try {
            builder = queryService.getSelect(refJpClass.getCode(), query, authInfo)
                .andWhere(Filter.attr(refJpAttr.getCode()).eq(key))
                .timeout(queryTimeout)
                .source(Source.USER);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (builder.isOrderByEmpty()) {
            builder.orderByDesc(refJpClass.getPrimaryKeyAttr());
          }
          return getListResult(builder, swe);
        });
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> getObject(ServerWebExchange swe,
                                      @PathVariable("pluralCode") String pluralCode,
                                      @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(queryTimeout)
        .useDefaultJpAttrs(Boolean.TRUE)
        .auth(auth)
        .source(Source.USER)
        .build();

    return repo
        .getAsyncObject(jpSelect)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(x -> JsonJPObject.newBuilder()
            .metaStorage(metaStorage)
            .jpObject(x)
            .baseUrl(sweService.getBaseUrl(swe))
            .restMapping(Controllers.API_MAPPING)
            .build())
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @DeleteMapping(value = "/{pluralCode}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> deleteObject(ServerWebExchange swe,
                                 @PathVariable("pluralCode") String pluralCode,
                                 @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPDelete jpDelete = JPDelete
        .delete(JPId.get(jpClass.getCode(), objectId))
        .auth(auth)
        .source(Source.USER)
        .build();

    return repo.asyncDelete(jpDelete)
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .then();
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JsonJPObject> createObject(ServerWebExchange swe,
                                         @PathVariable("pluralCode") String pluralCode,
                                         @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    JPCreate jpCreate;
    try {
      jpCreate = queryService.getCreate(query, Source.USER, authInfo)
          .build();
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!jpClass.getCode().equals(jpCreate.getJpClass())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return repo.asyncCreateAndGet(jpCreate)
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)))
        .map(x -> JsonJPObject.newBuilder()
            .metaStorage(metaStorage)
            .jpObject(x)
            .baseUrl(sweService.getBaseUrl(swe))
            .restMapping(Controllers.API_MAPPING)
            .build());
  }

  @ResponseBody
  @PutMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> updateObject(ServerWebExchange swe,
                                         @PathVariable("pluralCode") String pluralCode,
                                         @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    JPUpdate jpUpdate;
    try {
      jpUpdate = queryService.getUpdate(query, Source.USER, authInfo)
          .build();
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (jpUpdate.getJpId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!jpClass.getCode().equals(jpUpdate.getJpId().getJpClass())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return repo.asyncUpdateAndGet(jpUpdate)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .map(x -> JsonJPObject.newBuilder()
            .metaStorage(metaStorage)
            .jpObject(x)
            .baseUrl(sweService.getBaseUrl(swe))
            .restMapping(Controllers.API_MAPPING)
            .build());
  }
}
