package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.addinfos.beans.JPObjectAddInfoParamsBean;
import mp.jprime.dataaccess.addinfos.JPObjectAddInfoService;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonAddInfo;
import mp.jprime.json.beans.JsonIdentityData;
import mp.jprime.json.services.QueryService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private void setJPObjectAddInfoService(JPObjectAddInfoService jpObjectAddInfoService) {
    this.jpObjectAddInfoService = jpObjectAddInfoService;
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
  @PostMapping(value = "/{code}/addinfo", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonAddInfo> getAddInfo(ServerWebExchange swe,
                                      @PathVariable("code") String code,
                                      @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPClass jpClass = jpMetaFilter.get(code, auth);
    if (jpClass == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    JsonIdentityData jsonIdentityData;
    try {
      jsonIdentityData = queryService.getIdentityData(query);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return JPMono.fromCallable(() -> jpObjectAddInfoService.getAddInfo(
            JPObjectAddInfoParamsBean.newBuilder(jpClass.getCode(), jsonIdentityData.getId())
                .auth(auth)
                .source(Source.USER)
                .build()
        ))
        .flatMapMany(Flux::fromIterable)
        .map(x -> JsonAddInfo.newBuilder()
            .code(x.getCode())
            .info(x.getInfo())
            .build()
        );
  }
}
