package mp.jprime.api.rest.controllers;

import mp.jprime.configurations.JPQuerySettings;
import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectAccess;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.json.beans.JsonJPObjectList;
import mp.jprime.json.beans.JsonSelect;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.requesthistory.RequestHistoryPublisher;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class RestApiJsonCRUDBaseController extends JPQuerySettings {

  @Service
  private static final class Links {
    private static JPReactiveObjectRepositoryService REPO;
    private static QueryService QUERY_SERVICE;
    private static JPObjectAccessService OBJECT_ACCESS_SERVICE;
    private static JWTService JWT_SERVICE;
    private static JPMetaFilter META_FILTER;
    private static JsonJPObjectService JSON_JP_OBJECT_SERVICE;
    private static RequestHistoryPublisher REQUEST_HISTORY_PUBLISHER;

    private Links(@Autowired JPReactiveObjectRepositoryService repo,
                  @Autowired QueryService queryService,
                  @Autowired JPObjectAccessService objectAccessService,
                  @Autowired JWTService jwtService,
                  @Autowired JPMetaFilter jpMetaFilter,
                  @Autowired JsonJPObjectService jsonJPObjectService,
                  @Autowired(required = false) RequestHistoryPublisher historyPublisher) {
      REPO = repo;
      QUERY_SERVICE = queryService;
      OBJECT_ACCESS_SERVICE = objectAccessService;
      JWT_SERVICE = jwtService;
      META_FILTER = jpMetaFilter;
      JSON_JP_OBJECT_SERVICE = jsonJPObjectService;
      REQUEST_HISTORY_PUBLISHER = historyPublisher;
    }
  }

  protected JPReactiveObjectRepositoryService getRepo() {
    return Links.REPO;
  }

  protected QueryService getQueryService() {
    return Links.QUERY_SERVICE;
  }

  protected JPObjectAccessService getObjectAccessService() {
    return Links.OBJECT_ACCESS_SERVICE;
  }

  protected JWTService getJWTService() {
    return Links.JWT_SERVICE;
  }

  protected JPMetaFilter getMetaFilter() {
    return Links.META_FILTER;
  }

  protected JsonJPObjectService getJsonJPObjectService() {
    return Links.JSON_JP_OBJECT_SERVICE;
  }

  private RequestHistoryPublisher getRequestHistoryPublisher() {
    return Links.REQUEST_HISTORY_PUBLISHER;
  }

  protected Mono<JsonJPObjectList> getJsonJPObjectList(ServerWebExchange swe, String code, String query) {
    ParsedQuery parsedQuery = parseQuery(swe, code, query);
    return getListResult(parsedQuery.jpClass, checkAndBuild(parsedQuery.builder), parsedQuery.access, swe, parsedQuery.auth);
  }

  protected ParsedQuery parseQuery(ServerWebExchange swe, String code, String query) {
    AuthInfo auth = getJWTService().getAuthInfo(swe);
    JPClass jpClass = getMetaFilter().get(code, auth);
    if (jpClass == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    JPSelect.Builder builder;
    boolean access;
    try {
      JsonSelect jsonSelect = getQueryService().getQuery(query);
      access = jsonSelect != null && jsonSelect.isAccess();
      builder = getQueryService().getSelect(jpClass.getCode(), jsonSelect, auth)
          .timeout(getQueryTimeout())
          .source(Source.USER)
          .useDefaultOrder(true);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return new ParsedQuery(jpClass, builder, access, auth);
  }

  protected record ParsedQuery(JPClass jpClass, JPSelect.Builder builder, boolean access, AuthInfo auth) {
  }

  /**
   * Возвращает список
   *
   * @param jpClass JPClass
   * @param select  JPSelect
   * @param access  Признак рассчета доступа для каждого объекта
   * @param swe     ServerWebExchange
   * @param auth    AuthInfo
   * @return Список
   */
  protected Mono<JsonJPObjectList> getListResult(JPClass jpClass, JPSelect select, boolean access,
                                                 ServerWebExchange swe, AuthInfo auth) {
    return JPMono.zip(
            // Общее количество
            select.isTotalCount() ? getRepo().getAsyncTotalCount(select) : Mono.just(0L),
            // Выборка
            getRepo().getAsyncList(select)
                .collectList()
                .map(list -> toJsonJPObjectList(jpClass, list, access, swe, auth)),
            // Создаем результат
            (x, y) -> JsonJPObjectList.newBuilder()
                .limit(select.getLimit())
                .offset(select.getOffset())
                .classCode(jpClass.getCode())
                .objects(y)
                .totalCount(select.isTotalCount() ? x : null)
                .build())
        // Лог поиска
        .doOnSuccess(result -> sendSearch(jpClass.getCode(), select.getWhere(), result.getObjects(), auth, swe))
        // Ошибка
        .doOnError(e -> sendSearch(jpClass.getCode(), select.getWhere(), Collections.emptyList(), auth, swe))
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  protected Collection<JsonJPObject> toJsonJPObjectList(JPClass jpClass, Collection<JPObject> list, boolean access,
                                                        ServerWebExchange swe, AuthInfo auth) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    Map<Comparable, JPObjectAccess> mapAccess = access ?
        getObjectAccessService().objectsChangeAccess(
                jpClass,
                list.stream()
                    .map(o -> o.getJpId().getId())
                    .collect(Collectors.toList()),
                auth
            )
            .stream()
            .collect(Collectors.toMap(JPObjectAccess::getId, j -> j))
        : Collections.emptyMap();

    return list.stream()
        .map(x -> getJsonJPObjectService().toJsonJPObject(x, !access ? null : mapAccess.get(x.getJpId().getId()), swe))
        .collect(Collectors.toList());
  }

  protected Collection<JsonJPObject> toJsonJPObjectList(Collection<JPObject> list, ServerWebExchange swe) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    return list.stream()
        .map(object -> toJsonJPObject(object, swe))
        .collect(Collectors.toList());
  }

  protected JsonJPObject toJsonJPObject(JPObject object, ServerWebExchange swe) {
    if (object == null) {
      return null;
    }
    return getJsonJPObjectService().toJsonJPObject(object, swe);
  }

  protected void sendObject(String classCode, Object objectId, JsonJPObject result, AuthInfo auth, ServerWebExchange swe) {
    RequestHistoryPublisher historyPublisher = getRequestHistoryPublisher();
    if (historyPublisher == null) {
      return;
    }
    historyPublisher.sendObject(auth, swe, classCode, objectId,
        () -> result == null ? null : historyPublisher.toRequestHistoryObject(result.getClassCode(), result.getId(), result));
  }

  protected void sendSearch(String classCode, Filter where, Collection<JsonJPObject> result, AuthInfo auth, ServerWebExchange swe) {
    RequestHistoryPublisher historyPublisher = getRequestHistoryPublisher();
    if (historyPublisher == null) {
      return;
    }
    historyPublisher.sendSearch(auth, swe, classCode,
        () -> getQueryService().toExp(where),
        () -> result == null ? null : result.stream()
            .map(x -> historyPublisher.toRequestHistoryObject(x.getClassCode(), x.getId(), x))
            .collect(Collectors.toList())
    );
  }
}