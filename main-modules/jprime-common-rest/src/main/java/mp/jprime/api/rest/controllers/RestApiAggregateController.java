package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonAggregateResult;
import mp.jprime.json.services.JsonMapper;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class RestApiAggregateController implements JsonMapper {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Интерфейс создания / обновления объекта
   */
  private JPObjectRepositoryService repo;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;

  @Value("${jprime.query.queryTimeout:}")
  private Integer queryTimeout;

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Autowired
  private void setRepo(JPObjectRepositoryService repo) {
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

  @ResponseBody
  @PostMapping(value = "/{pluralCode}/aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonAggregateResult> getAggregate(ServerWebExchange swe,
                                                @PathVariable("pluralCode") String pluralCode,
                                                @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    JPAggregate.Builder builder;
    try {
      builder = queryService.getAggregate(jpClass.getCode(), query, authInfo)
          .timeout(queryTimeout)
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
