package mp.jprime.rest.v1;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPId;
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
import mp.jprime.json.services.JsonMapper;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.web.services.ServerWebExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface RestApiBaseController extends JsonMapper {
  /**
   * Возвращает маппинг контроллера
   *
   * @return Маппинг контроллера
   */
  String getApiMapping();

  /**
   * Максимальное количество в выборке по-умолчанию
   */
  int MAX_LIMIT = 50;

  /**
   * Заполнение запросов на основе JSON
   *
   * @return Заполнение запросов на основе JSON
   */
  QueryService getQueryService();

  /**
   * Интерфейс создания / обновления объекта
   *
   * @return Интерфейс создания / обновления объекта
   */
  JPObjectRepositoryService getRepo();

  /**
   * Хранилище метаинформации
   *
   * @return Хранилище метаинформации
   */
  JPMetaStorage getMetaStorage();

  /**
   * Методы работы с ServerWebExchangeService
   *
   * @return Методы работы с ServerWebExchangeService
   */
  ServerWebExchangeService getSweService();

  /**
   * Обработчик JWT
   *
   * @return Обработчик JWT
   */
  JWTService getJwtService();

  /**
   * queryTimeout
   *
   * @return queryTimeout
   */
  Integer getQueryTimeout();

  /**
   * Возвращает список
   *
   * @param s       JPSelect
   * @return Список
   */
  default Mono<JsonJPObjectList> getListResult(final JPSelect s, final ServerWebExchange swe) {
    JPObjectRepositoryService repo = getRepo();
    JPMetaStorage metaStorage = getMetaStorage();
    ServerWebExchangeService sweService = getSweService();
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
                .restMapping(getApiMapping())
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
  @GetMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                               @PathVariable("pluralCode") String pluralCode,
                                               @RequestParam(value = "offset", required = false) Integer offset,
                                               @RequestParam(value = "limit", required = false) Integer limit) {
    JPMetaStorage metaStorage = getMetaStorage();
    JWTService jwtService = getJwtService();

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return getListResult(
        JPSelect
            .from(jpClass.getCode())
            .offset(offset != null ? offset : 0)
            .limit(limit != null ? limit : MAX_LIMIT)
            .timeout(getQueryTimeout())
            .useDefaultJpAttrs(Boolean.TRUE)
            .auth(authInfo)
            .source(Source.USER)
            .build(), swe);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                               @PathVariable("pluralCode") String pluralCode,
                                               @RequestBody String query) {
    JPMetaStorage metaStorage = getMetaStorage();
    JWTService jwtService = getJwtService();
    QueryService queryService = getQueryService();

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    try {
      builder = queryService.getSelect(jpClass.getCode(), query, authInfo)
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return getListResult(builder.build(), swe);
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getLinkList(ServerWebExchange swe,
                                             @PathVariable("pluralCode") String pluralCode,
                                             @PathVariable("objectId") String objectId,
                                             @PathVariable("attrCode") String attrCode,
                                             @PathVariable("attrValue") String attrValue) {
    return getSearchLinkList(swe, pluralCode, objectId, attrCode, attrValue, null);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getSearchLinkList(ServerWebExchange swe,
                                                   @PathVariable("pluralCode") String pluralCode,
                                                   @PathVariable("objectId") String objectId,
                                                   @PathVariable("attrCode") String attrCode,
                                                   @PathVariable("attrValue") String attrValue,
                                                   @RequestBody String query) {
    JPMetaStorage metaStorage = getMetaStorage();
    JWTService jwtService = getJwtService();
    QueryService queryService = getQueryService();

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
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return getListResult(builder.build(), swe);
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getBackReferenceList(ServerWebExchange swe,
                                                      @PathVariable("pluralCode") String pluralCode,
                                                      @PathVariable("objectId") String objectId,
                                                      @PathVariable("attrCode") String attrCode) {
    return getSearchBackReferenceList(swe, pluralCode, objectId, attrCode, null);
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObjectList> getSearchBackReferenceList(ServerWebExchange swe,
                                                            @PathVariable("pluralCode") String pluralCode,
                                                            @PathVariable("objectId") String objectId,
                                                            @PathVariable("attrCode") String attrCode,
                                                            @RequestBody String query) {
    JPMetaStorage metaStorage = getMetaStorage();
    JWTService jwtService = getJwtService();
    QueryService queryService = getQueryService();

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpAttr == null || jpAttr.getRefJpClassCode() == null || jpAttr.getType() != JPType.BACKREFERENCE || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    try {
      builder = queryService.getSelect(jpAttr.getRefJpClassCode(), query, authInfo)
          .andWhere(Filter.attr(jpAttr.getRefJpAttrCode()).eq(objectId))
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return getListResult(builder.build(), swe);
  }

  @ResponseBody
  @GetMapping(value = "/{pluralCode}/{objectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObject> getObject(ServerWebExchange swe,
                                       @PathVariable("pluralCode") String pluralCode,
                                       @PathVariable("objectId") String objectId) {
    JPObjectRepositoryService repo = getRepo();
    JPMetaStorage metaStorage = getMetaStorage();
    ServerWebExchangeService sweService = getSweService();
    JWTService jwtService = getJwtService();

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(getQueryTimeout())
        .useDefaultJpAttrs(Boolean.TRUE)
        .auth(authInfo)
        .source(Source.USER)
        .build();

    return repo
        .getAsyncObject(jpSelect)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(x -> JsonJPObject.newBuilder()
            .metaStorage(metaStorage)
            .jpObject(x)
            .baseUrl(sweService.getBaseUrl(swe))
            .restMapping(getApiMapping())
            .build())
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @DeleteMapping(value = "/{pluralCode}/{objectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.ACCEPTED)
  default Mono<Void> deleteObject(ServerWebExchange swe,
                                  @PathVariable("pluralCode") String pluralCode,
                                  @PathVariable("objectId") String objectId) {
    JPObjectRepositoryService repo = getRepo();
    JPMetaStorage metaStorage = getMetaStorage();
    JWTService jwtService = getJwtService();

    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    JPDelete jpDelete = JPDelete
        .delete(JPId.get(jpClass.getCode(), objectId))
        .auth(authInfo)
        .source(Source.USER)
        .build();

    return repo.asyncDelete(jpDelete)
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .then();
  }

  @ResponseBody
  @PostMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.CREATED)
  default Mono<JsonJPObject> createObject(ServerWebExchange swe,
                                          @PathVariable("pluralCode") String pluralCode,
                                          @RequestBody String query) {
    JPObjectRepositoryService repo = getRepo();
    JPMetaStorage metaStorage = getMetaStorage();
    ServerWebExchangeService sweService = getSweService();
    JWTService jwtService = getJwtService();
    QueryService queryService = getQueryService();

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
            .restMapping(getApiMapping())
            .build());
  }

  @ResponseBody
  @PutMapping(value = "/{pluralCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  default Mono<JsonJPObject> updateObject(ServerWebExchange swe,
                                          @PathVariable("pluralCode") String pluralCode,
                                          @RequestBody String query) {
    JPObjectRepositoryService repo = getRepo();
    JPMetaStorage metaStorage = getMetaStorage();
    ServerWebExchangeService sweService = getSweService();
    JWTService jwtService = getJwtService();
    QueryService queryService = getQueryService();

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
            .restMapping(getApiMapping())
            .build());
  }
}
