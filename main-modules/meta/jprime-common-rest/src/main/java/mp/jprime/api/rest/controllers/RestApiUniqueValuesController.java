package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesService;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonSelect;
import mp.jprime.json.beans.JsonUniqueValue;
import mp.jprime.json.beans.JsonUniqueValues;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class RestApiUniqueValuesController {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Логика вычисления значений по умолчанию
   */
  private JPUniqueValuesService jpUniqueValuesService;
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

  @Autowired(required = false)
  public void setJPObjectUniqueValuesService(JPUniqueValuesService jpUniqueValuesService) {
    this.jpUniqueValuesService = jpUniqueValuesService;
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
  @PostMapping(value = "/{code}/uniquevalues", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonUniqueValues> getUniqueValues(ServerWebExchange swe,
                                                @PathVariable("code") String code,
                                                @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (jpUniqueValuesService == null) {
      return Mono.just(JsonUniqueValues.newInstance(null));
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    JsonSelect jsonSelect;
    try {
      jsonSelect = queryService.getQuery(query);
      builder = queryService.getSelect(jpClass.getCode(), jsonSelect, auth)
          .timeout(queryTimeout)
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return jpUniqueValuesService
        .getUniqueValues(builder.build(), jsonSelect.getAttrs())
        .collectList()
        .map(x -> JsonUniqueValues.newInstance(toJson(x, new Counter())));
  }

  private Collection<JsonUniqueValue> toJson(Collection<JPUniqueValue> values, Counter counter) {
    if (CollectionUtils.isEmpty(values)) {
      return null;
    }
    return values.stream()
        .map(x -> JsonUniqueValue.newBuilder()
            .id(counter.newId())
            .attr(x.getAttr())
            .value(x.getValue())
            .subValues(toJson(x.getSubValues(), counter))
            .build())
        .collect(Collectors.toList());
  }

  private static class Counter {
    private int id = 1;

    public int newId() {
      return id++;
    }
  }
}
