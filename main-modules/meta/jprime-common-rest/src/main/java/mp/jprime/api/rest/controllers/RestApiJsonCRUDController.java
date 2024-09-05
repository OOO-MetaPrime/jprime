package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.*;
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
import mp.jprime.json.beans.JsonSelect;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.meta.beans.JPType;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RestApiJsonCRUDController extends RestApiJsonCRUDBaseController {
  /**
   * Фильтр меты
   */
  private JPMetaFilter jpMetaFilter;

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @ResponseBody
  @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
    JPSelect.Builder builder = JPSelect.from(jpClass.getCode())
        .offset(offset != null ? offset : 0)
        .limit(limit != null ? limit : QueryService.MAX_LIMIT)
        .orderByDesc(jpClass.getPrimaryKeyAttr())
        .timeout(getQueryTimeout())
        .useDefaultJpAttrs(Boolean.TRUE)
        .source(Source.USER)
        .auth(auth);
    return getListResult(jpClass, checkAndBuild(builder), false, swe, auth);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getObjectList(ServerWebExchange swe,
                                              @PathVariable("code") String code,
                                              @RequestBody String query) {
    return getJsonJPObjectList(swe, code, query);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/search/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(jpClass, checkAndBuild(builder), access, swe, auth);
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}/{attrCode}/{attrValue}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchLinkList(ServerWebExchange swe,
                                                  @PathVariable("code") String code,
                                                  @PathVariable("objectId") String objectId,
                                                  @PathVariable("attrCode") String attrCode,
                                                  @PathVariable("attrValue") String attrValue,
                                                  @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    JPAttr jpAttr = jpClass == null ? null : jpClass.getAttr(attrCode);
    if (jpAttr == null || jpAttr.getRefJpAttr() == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    JPClass refClass = metaStorage.getJPClassByCode(jpAttr.getRefJpAttr());
    JPAttr refAttr = refClass != null && jpAttr.getRefJpAttr() != null ?
        refClass.getAttr(jpAttr.getRefJpAttr()) : null;
    if (refAttr == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpAttr.getRefJpAttr(), jsonSelect, auth)
          .andWhere(Filter.attr(jpAttr.getRefJpAttr()).eq(attrValue))
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(refClass.getPrimaryKeyAttr());
    }
    return getListResult(refClass, checkAndBuild(builder), access, swe, auth);
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getBackReferenceList(ServerWebExchange swe,
                                                     @PathVariable("code") String code,
                                                     @PathVariable("objectId") String objectId,
                                                     @PathVariable("attrCode") String attrCode) {
    return getSearchBackReferenceList(swe, code, objectId, attrCode, null);
  }

  @ResponseBody
  @PostMapping(value = "/{code}/{objectId}/{attrCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObjectList> getSearchBackReferenceList(ServerWebExchange swe,
                                                           @PathVariable("code") String code,
                                                           @PathVariable("objectId") String objectId,
                                                           @PathVariable("attrCode") String attrCode,
                                                           @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    JPAttr jpAttr = jpClass == null || jpClass.isInner() ? null : jpClass.getAttr(attrCode);
    JPClass refJpClass = jpAttr == null || jpAttr.getRefJpClass() == null || jpAttr.getType() != JPType.BACKREFERENCE
        ? null : metaStorage.getJPClassByCode(jpAttr.getRefJpClass());
    JPAttr refJpAttr = refJpClass == null ? null : refJpClass.getAttr(jpAttr.getRefJpAttr());
    JPAttr targetAttr = refJpAttr == null ? null : jpClass.getAttr(refJpAttr.getRefJpAttr());
    if (objectId == null || targetAttr == null || !jpClass.getCode().equals(refJpAttr.getRefJpClass())) {
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
                .timeout(getQueryTimeout())
                .source(Source.USER);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          if (builder.isOrderByEmpty()) {
            builder.orderByDesc(refJpClass.getPrimaryKeyAttr());
          }
          return getListResult(refJpClass, checkAndBuild(builder), access, swe, auth);
        });
  }

  @ResponseBody
  @GetMapping(value = "/{code}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> getObject(ServerWebExchange swe,
                                      @PathVariable("code") String code,
                                      @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(getQueryTimeout())
        .useDefaultJpAttrs(Boolean.TRUE)
        .auth(auth)
        .source(Source.USER)
        .build();

    return repo.getAsyncObject(jpSelect)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(object -> jsonJPObjectService.toJsonJPObject(object, swe))
        .doOnSuccess(x -> sendObject(code, objectId, x, auth, swe))
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @DeleteMapping(value = "/{code}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
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
      JPUpdate.Builder jpUpdateBuilder = queryService.getUpdate(query, Source.USER, authInfo);
      if (jpUpdateBuilder != null) {
        jpUpdate = jpUpdateBuilder.build();
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }
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
  @PatchMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> patchObject(ServerWebExchange swe,
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
    return repo.asyncPatchAndGet(jpCreate)
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .onErrorResume(JPObjectNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage())))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)))
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

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = queryService.getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, null)
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(jpClass, checkAndBuild(builder), access, swe, null);
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

    JPSelect jpSelect = JPSelect
        .from(jpClass)
        .where(Filter.attr(jpClass.getPrimaryKeyAttr()).eq(objectId))
        .timeout(getQueryTimeout())
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
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (builder.isOrderByEmpty()) {
      builder.orderByDesc(jpClass.getPrimaryKeyAttr());
    }
    return getListResult(jpClass, checkAndBuild(builder), access, swe, null);
  }
}