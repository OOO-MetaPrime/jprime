package mp.jprime.api.rest.controllers;

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
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.requesthistory.services.RequestHistoryPublisher;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class RestApiJsonCRUDBaseController implements JPObjectAccessServiceAware, JPReactiveObjectRepositoryServiceAware {
  protected static final Logger LOG = LoggerFactory.getLogger(RestApiJsonCRUDController.class);

  @Value("${jprime.query.queryTimeout:}")
  protected Integer queryTimeout;
  @Value("${jprime.api.checkLimit:true}")
  protected boolean checkLimit;
  /**
   * Максимальное количество в выборке через api
   */
  @Value("${jprime.api.maxLimit:1000}")
  protected Integer maxLimit;

  /**
   * Интерфейс создания / обновления объекта
   */
  protected JPReactiveObjectRepositoryService repo;
  /**
   * Заполнение запросов на основе JSON
   */
  protected QueryService queryService;
  /**
   * Интерфейс проверки доступа к объекту
   */
  protected JPObjectAccessService objectAccessService;
  /**
   * Хранилище метаинформации
   */
  protected JPMetaStorage metaStorage;
  /**
   * Обработчик JWT
   */
  protected JWTService jwtService;
  /**
   * Формирование JsonJPObject
   */
  protected JsonJPObjectService jsonJPObjectService;
  /**
   * Работа с отправкой Истории запросов
   */
  private RequestHistoryPublisher historyPublisher;

  @Override
  public void setJpReactiveObjectRepositoryService(JPReactiveObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setJsonJPObjectService(JsonJPObjectService jsonJPObjectService) {
    this.jsonJPObjectService = jsonJPObjectService;
  }

  @Autowired(required = false)
  private void setHistoryPublisher(RequestHistoryPublisher historyPublisher) {
    this.historyPublisher = historyPublisher;
  }

  protected Mono<JsonJPObjectList> getJsonJPObjectList(ServerWebExchange swe, String code, String query) {
    ParsedQuery parsedQuery = parseQuery(swe, code, query);
    return getListResult(parsedQuery.jpClass, checkAndBuild(parsedQuery.builder), parsedQuery.access, swe, parsedQuery.auth);
  }

  protected ParsedQuery parseQuery(ServerWebExchange swe, String code, String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);

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
    return new ParsedQuery(jpClass, builder, access, auth);
  }

  protected record ParsedQuery(JPClass jpClass, JPSelect.Builder builder, boolean access, AuthInfo auth) {
  }

  protected JPSelect checkAndBuild(JPSelect.Builder builder) {
    Integer limit = builder.limit();

    if (checkLimit && limit != null && limit > maxLimit) {
      LOG.error("Warning. Select query limit for {} exceeded: {}", builder.getJpClass(), limit);
      builder.limit(maxLimit);
    }
    return builder.build();
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
    return Mono.zip(
            // Общее количество
            select.isTotalCount() ? repo.getAsyncTotalCount(select) : Mono.just(0L),
            // Выборка
            repo.getAsyncList(select)
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
        .doOnSuccess(x -> sendSearch(jpClass.getCode(), select.getWhere(), x.getObjects(), auth, swe))
        // Ошибка
        .onErrorResume(JPClassNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  protected Collection<JsonJPObject> toJsonJPObjectList(JPClass jpClass, Collection<JPObject> list, boolean access,
                                                        ServerWebExchange swe, AuthInfo auth) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
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
    return jsonJPObjectService.toJsonJPObject(object, swe);
  }

  protected void sendObject(String classCode, Object objectId, JsonJPObject result, AuthInfo auth, ServerWebExchange swe) {
    if (historyPublisher == null) {
      return;
    }
    historyPublisher.sendObject(auth, swe, classCode, objectId,
        () -> historyPublisher.toRequestHistoryObject(result.getClassCode(), result.getId(), result));
  }

  protected void sendSearch(String classCode, Filter where, Collection<JsonJPObject> result, AuthInfo auth, ServerWebExchange swe) {
    if (historyPublisher == null) {
      return;
    }
    historyPublisher.sendSearch(auth, swe, classCode,
        () -> queryService.toExp(where),
        () -> result.stream()
            .map(x -> historyPublisher.toRequestHistoryObject(x.getClassCode(), x.getId(), x))
            .collect(Collectors.toList())
    );
  }
}