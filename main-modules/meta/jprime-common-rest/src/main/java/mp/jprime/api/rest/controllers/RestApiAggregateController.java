package mp.jprime.api.rest.controllers;

import mp.jprime.configurations.JPQuerySettings;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonAggregateResult;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class RestApiAggregateController extends JPQuerySettings implements JPObjectRepositoryServiceAware {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPObjectRepositoryService repo;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;
  /**
   * Фильтр меты
   */
  private JPMetaFilter jpMetaFilter;

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @ResponseBody
  @PostMapping(value = "/{code}/aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonAggregateResult> getAggregate(ServerWebExchange swe,
                                                @PathVariable("code") String code,
                                                @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPClass jpClass = jpMetaFilter.get(code, auth);
    if (jpClass == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPAggregate.Builder builder;
    try {
      builder = queryService.getAggregate(jpClass.getCode(), query, authInfo)
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return repo.getAsyncAggregate(builder.build())
        .map(x -> JsonAggregateResult.newBuilder()
            .classCode(jpClass.getCode())
            .aggrs(x.toMap())
            .build()
        );
  }
}
