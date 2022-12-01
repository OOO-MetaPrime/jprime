package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueParamsBean;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueService;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueServiceAware;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonDefValueResult;
import mp.jprime.json.beans.JsonDefValuesQuery;
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
public class RestApiDefValueController implements JPObjectDefValueServiceAware {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Логика вычисления значений по умолчанию
   */
  private JPObjectDefValueService jpObjectDefValueService;
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

  @Override
  public void setJPObjectDefValueService(JPObjectDefValueService jpObjectDefValueService) {
    this.jpObjectDefValueService = jpObjectDefValueService;
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
  @PostMapping(value = "/{code}/defvalue", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonDefValueResult> getDefValue(ServerWebExchange swe,
                                              @PathVariable("code") String code,
                                              @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JsonDefValuesQuery jsonDefValuesQuery;
    try {
      jsonDefValuesQuery = queryService.getDefValuesQuery(query);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return jpObjectDefValueService
        .getAsyncDefValues(
            jpClass.getCode(),
            JPObjectDefValueParamsBean.newBuilder()
                .rootId(jsonDefValuesQuery.getId())
                .rootJpClassCode(jsonDefValuesQuery.getClassCode())
                .rootData(JPData.of(jsonDefValuesQuery.getData()))
                .refAttrCode(jsonDefValuesQuery.getRefAttrCode())
                .auth(auth)
                .source(Source.USER)
                .build()
        )
        .map(x -> JsonDefValueResult.newBuilder()
            .classCode(jpClass.getCode())
            .data(x.toMap())
            .build()
        );
  }
}
