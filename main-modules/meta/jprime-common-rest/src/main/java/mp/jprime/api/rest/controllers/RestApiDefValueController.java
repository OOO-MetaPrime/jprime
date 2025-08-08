package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.defvalues.beans.JPObjectDefValueParamsBean;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueService;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueServiceAware;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonDefValue;
import mp.jprime.json.beans.JsonDefValuesQuery;
import mp.jprime.json.services.JPJsonMapper;
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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class RestApiDefValueController implements JPObjectDefValueServiceAware {
  private JPJsonMapper jpJsonMapper;
  private JPObjectDefValueService jpObjectDefValueService;
  private JWTService jwtService;
  private JPMetaFilter jpMetaFilter;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Override
  public void setJPObjectDefValueService(JPObjectDefValueService jpObjectDefValueService) {
    this.jpObjectDefValueService = jpObjectDefValueService;
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
  @PostMapping(value = "/{code}/defvalue", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonDefValue> getDefValue(ServerWebExchange swe,
                                        @PathVariable("code") String code,
                                        @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPClass jpClass = jpMetaFilter.get(code, auth);
    if (jpClass == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    JsonDefValuesQuery jsonQuery;
    try {
      jsonQuery = jpJsonMapper.toObject(JsonDefValuesQuery.class, query);
    } catch (JPRuntimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return JPMono.fromCallable(() -> jpObjectDefValueService.getDefValues(
            jpClass.getCode(),
            JPObjectDefValueParamsBean.newBuilder()
                .rootId(jsonQuery.getId())
                .rootJpClassCode(jsonQuery.getClassCode())
                .rootData(JPData.of(jsonQuery.getData()))
                .refAttrCode(jsonQuery.getRefAttrCode())
                .auth(auth)
                .source(Source.USER)
                .build()
        ))
        .map(x -> JsonDefValue.of(jpClass.getCode(), x.toMap()));
  }
}
