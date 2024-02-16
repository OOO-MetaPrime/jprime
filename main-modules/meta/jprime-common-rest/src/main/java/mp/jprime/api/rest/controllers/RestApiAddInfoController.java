package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoParamsBean;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoService;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonAddInfo;
import mp.jprime.json.beans.JsonIdentityData;
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
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1")
public class RestApiAddInfoController {
  /**
   * Заполнение запросов на основе JSON
   */
  private QueryService queryService;
  /**
   * Логика вычисления доп. информации
   */
  private JPObjectAddInfoService jpObjectAddInfoService;
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
  private void setJPObjectAddInfoService(JPObjectAddInfoService jpObjectAddInfoService) {
    this.jpObjectAddInfoService = jpObjectAddInfoService;
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
  @PostMapping(value = "/{code}/addinfo", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonAddInfo> getAddInfo(ServerWebExchange swe,
                                      @PathVariable("code") String code,
                                      @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JsonIdentityData jsonIdentityData;
    try {
      jsonIdentityData = queryService.getIdentityData(query);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return jpObjectAddInfoService
        .getAsyncAddInfo(
            JPObjectAddInfoParamsBean.newBuilder(jpClass.getCode(), jsonIdentityData.getId())
                .auth(auth)
                .source(Source.USER)
                .build()
        )
        .map(x -> JsonAddInfo.newBuilder()
            .code(x.getCode())
            .info(x.getInfo())
            .build()
        );
  }
}
