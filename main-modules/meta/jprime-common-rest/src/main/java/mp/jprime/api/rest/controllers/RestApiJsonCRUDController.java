package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObjectAccess;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPObjectNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.json.beans.JsonJPObjectList;
import mp.jprime.json.beans.JsonSelect;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.requesthistory.services.RequestHistoryPublisher;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class RestApiJsonCRUDController implements JPObjectAccessServiceAware, JPReactiveObjectRepositoryServiceAware {
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
   * Интерфейс проверки доступа к объекту
   */
  private JPObjectAccessService objectAccessService;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;
  /**
   * Работа с отправкой Истории запросов
   */
  private RequestHistoryPublisher historyPublisher;
  /**
   * Формирование JsonJPObject
   */
  private JsonJPObjectService jsonJPObjectService;

  /**
   * Фильтр меты
   */
  private JPMetaFilter jpMetaFilter;

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

  @Override
  public void setJpReactiveObjectRepositoryService(JPReactiveObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired(required = false)
  private void setHistoryPublisher(RequestHistoryPublisher historyPublisher) {
    this.historyPublisher = historyPublisher;
  }

  @Autowired
  private void setJsonJPObjectService(JsonJPObjectService jsonJPObjectService) {
    this.jsonJPObjectService = jsonJPObjectService;
  }

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  /**
   * Возвращает список
   *
   * @param builder JPSelect.Builder
   * @param access  Признак рассчета доступа для каждого объекта
   * @param swe     ServerWebExchange
   * @param auth    AuthInfo
   * @return Список
   */
  private Mono<JsonJPObjectList> getListResult(JPSelect.Builder builder, boolean access,
                                               ServerWebExchange swe, AuthInfo auth) {
    Integer limit = builder.limit();

    if (checkLimit && limit != null && limit > maxLimit) {
      LOG.error("Warning. Select query limit for {} exceeded: {}", builder.getJpClass(), limit);
      builder.limit(maxLimit);
    }
    JPSelect s = builder.build();

    JPClass jpClass = metaStorage.getJPClassByCode(s.getJpClass());

    return Mono.zip(
            // Общее количество
            s.isTotalCount() ? repo.getAsyncTotalCount(s) : Mono.just(0L),
            // Выборка
            repo.getAsyncList(s)
                .collectList()
                .map(list ->
                    {
                      Map<Comparable, JPObjectAccess> mapAccess = access ?
                          objectAccessService.objectsChangeAccess(
                                  jpClass,
                                  list.stream().map(o -> o.getJpId().getId()).collect(Collectors.toList()),
                                  auth
                              )
                              .stream()
                              .collect(Collectors.toMap(JPObjectAccess::getId, j -> j))
                          : Collections.emptyMap();

                      return list.stream()
                          .map(x -> jsonJPObjectService.toJsonJPObject(x, !access ? null : mapAccess.get(x.getJpId().getId()), swe))
                          .collect(Collectors.toList());
                    }
                )
            ,
            // Создаем результат
            (x, y) -> JsonJPObjectList.newBuilder()
                .limit(s.getLimit())
                .offset(s.getOffset())
                .classCode(jpClass.getCode())
                .objects(y)
                .totalCount(builder.isTotalCount() ? x : null)
                .build())
        // Ошибка
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("code") String code,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
    return getListResult(builder, false, swe, auth);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("code") String code,
                                              @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    if (historyPublisher != null) {
      historyPublisher.sendSearch(jpClass.getCode(), query, auth, swe);
    }

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, auth)
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, access, swe, auth);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("code") String code,
                                              @PathVariable("attrCode") String attrCode,
                                              @PathVariable("attrValue") String attrValue,
                                              @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpClass == null || jpClass.isInner() || jpAttr == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, auth)
          .andWhere(Filter.attr(attrCode).eq(attrValue))
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, access, swe, auth);
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getLinkList(ServerWebExchange swe,
                                            @PathVariable("code") String code,
                                            @PathVariable("objectId") String objectId,
                                            @PathVariable("attrCode") String attrCode,
                                            @PathVariable("attrValue") String attrValue) {
    return getSearchLinkList(swe, code, objectId, attrCode, attrValue, null);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchLinkList(ServerWebExchange swe,
                                                  @PathVariable("code") String code,
                                                  @PathVariable("objectId") String objectId,
                                                  @PathVariable("attrCode") String attrCode,
                                                  @PathVariable("attrValue") String attrValue,
                                                  @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpAttr.getRefJpClassCode(), jsonSelect, auth)
          .andWhere(Filter.attr(jpAttr.getRefJpAttrCode()).eq(attrValue))
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(refClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, access, swe, auth);
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getBackReferenceList(ServerWebExchange swe,
                                                     @PathVariable("code") String code,
                                                     @PathVariable("objectId") String objectId,
                                                     @PathVariable("attrCode") String attrCode) {
    return getSearchBackReferenceList(swe, code, objectId, attrCode, null);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchBackReferenceList(ServerWebExchange swe,
                                                           @PathVariable("code") String code,
                                                           @PathVariable("objectId") String objectId,
                                                           @PathVariable("attrCode") String attrCode,
                                                           @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
                    .flatMap(o -> {
                      Object attrValue = o.getAttrValue(targetAttr.getCode());
                      return attrValue == null ? Mono.empty() : Mono.just(attrValue);
                    });
              }
            }
        )
        .flatMap(key -> {
          AuthInfo auth = jwtService.getAuthInfo(swe);

          JPSelect.Builder builder;
          boolean access;
          try {
            JsonSelect jsonSelect = queryService.getQuery(query);
            access = jsonSelect != null && jsonSelect.isAccess();
            builder = queryService.getSelect(refJpClass.getCode(), jsonSelect, auth)
                .andWhere(Filter.attr(refJpAttr.getCode()).eq(key))
                .timeout(queryTimeout)
                .source(Source.USER);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (builder.isOrderByEmpty()) {
            builder.orderByDesc(refJpClass.getPrimaryKeyAttr());
          }
          return getListResult(builder, access, swe, auth);
        });
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> getObject(ServerWebExchange swe,
                                      @PathVariable("code") String code,
                                      @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    if (historyPublisher != null) {
      historyPublisher.sendFind(jpClass.getCode(), objectId, auth, swe);
    }

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(queryTimeout)
        .useDefaultJpAttrs(Boolean.TRUE)
        .auth(auth)
        .source(Source.USER)
        .build();

    return repo.getAsyncObject(jpSelect)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(object -> jsonJPObjectService.toJsonJPObject(object, swe))
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @DeleteMapping(value = "/{code}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> deleteObject(ServerWebExchange swe,
                                 @PathVariable("code") String code,
                                 @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
  @PostMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JsonJPObject> createObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
        .map(object -> jsonJPObjectService.toJsonJPObject(object, swe));
  }

  @ResponseBody
  @PutMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> updateObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
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
        .map(object -> jsonJPObjectService.toJsonJPObject(object, swe));
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search/anonymous", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectListAnonymous(ServerWebExchange swe,
                                                       @PathVariable("code") String code,
                                                       @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner() || !jpMetaFilter.anonymousFilter(jpClass)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (historyPublisher != null) {
      historyPublisher.sendSearch(jpClass.getCode(), query, null, swe);
    }

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, null)
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, access, swe, null);
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}/anonymous", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> getObjectAnonymous(ServerWebExchange swe,
                                               @PathVariable("code") String code,
                                               @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner() || !jpMetaFilter.anonymousFilter(jpClass)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (historyPublisher != null) {
      historyPublisher.sendFind(jpClass.getCode(), objectId, null, swe);
    }

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(queryTimeout)
        .useDefaultJpAttrs(Boolean.TRUE)
        .auth(null)
        .source(Source.USER)
        .build();

    return repo.getAsyncObject(jpSelect)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(object -> jsonJPObjectService.toJsonJPObject(object, swe))
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search/{attrCode}/{attrValue}/anonymous", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectListAnonymous(ServerWebExchange swe,
                                                       @PathVariable("code") String code,
                                                       @PathVariable("attrCode") String attrCode,
                                                       @PathVariable("attrValue") String attrValue,
                                                       @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpClass == null || jpClass.isInner() || !jpMetaFilter.anonymousFilter(jpClass) || jpAttr == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, null)
          .andWhere(Filter.attr(attrCode).eq(attrValue))
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(builder, access, swe, null);
  }
}