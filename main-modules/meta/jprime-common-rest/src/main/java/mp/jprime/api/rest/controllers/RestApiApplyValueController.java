package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueParamsBean;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueService;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueServiceAware;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonApplyValueResult;
import mp.jprime.json.beans.JsonApplyValuesQuery;
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
public class RestApiApplyValueController implements JPObjectApplyValueServiceAware {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Логика для дополнения значений
   */
  private JPObjectApplyValueService jpObjectApplyValueService;
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
  public void setJPObjectApplyValueService(JPObjectApplyValueService jpObjectApplyValueService) {
    this.jpObjectApplyValueService = jpObjectApplyValueService;
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
  @PostMapping(value = "/{code}/applyvalue", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonApplyValueResult> getApplyValue(ServerWebExchange swe,
                                                @PathVariable("code") String code,
                                                @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JsonApplyValuesQuery jsonApplyValuesQuery;
    try {
      jsonApplyValuesQuery = queryService.getApplyValuesQuery(query);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Object id = jsonApplyValuesQuery.getId();
    String classCode = jsonApplyValuesQuery.getClassCode();
    if (!jpClass.getCode().equals(classCode)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return jpObjectApplyValueService
        .getAsyncApplyValues(
            JPObjectApplyValueParamsBean.newBuilder(id, classCode, JPData.of(jsonApplyValuesQuery.getData()))
                .attrs(jsonApplyValuesQuery.getAttrs())
                .auth(auth)
                .source(Source.USER)
                .build()
        )
        .map(x -> JsonApplyValueResult.newBuilder()
            .id(id)
            .classCode(classCode)
            .data(x.toMap())
            .build()
        );
  }
}
