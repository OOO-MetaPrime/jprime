package mp.jprime.api.rest.controllers;

import mp.jprime.configurations.JPQuerySettings;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesService;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonSelect;
import mp.jprime.json.beans.JsonUniqueValue;
import mp.jprime.json.beans.JsonUniqueValues;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RestApiUniqueValuesBaseController extends JPQuerySettings {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Логика вычисления значений по умолчанию
   */
  private JPUniqueValuesService jpUniqueValuesService;
  /**
   * Обработчик JWT
   */
  protected JWTService jwtService;
  /**
   * Фильтр меты
   */
  protected JPMetaFilter jpMetaFilter;

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  @Autowired(required = false)
  public void setJPObjectUniqueValuesService(JPUniqueValuesService jpUniqueValuesService) {
    this.jpUniqueValuesService = jpUniqueValuesService;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  protected Mono<JsonUniqueValues> getJsonUniqueValues(ServerWebExchange swe, String jpClassCode, String query) {
    if (jpUniqueValuesService == null) {
      return Mono.just(JsonUniqueValues.EMPTY);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPSelect.Builder builder;
    JsonSelect jsonSelect;
    try {
      jsonSelect = queryService.getQuery(query);
      builder = queryService.getSelect(jpClassCode, jsonSelect, auth)
          .timeout(getQueryTimeout())
          .source(Source.USER);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Collection<String> attrs = jsonSelect.getAttrs();
    return getUniqueValues(builder, attrs == null ? Collections.emptyList() : new ArrayList<>(attrs))
        .map(x -> JsonUniqueValues.newInstance(toJson(x, new Counter())));
  }

  protected Mono<Collection<JPUniqueValue>> getUniqueValues(JPSelect.Builder builder, List<String> hierarchy) {
    return JPMono.fromCallable(() -> jpUniqueValuesService.getUniqueValues(builder.build(), hierarchy));
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
