package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.applyvalues.beans.JPObjectApplyValueParamsBean;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueService;
import mp.jprime.dataaccess.applyvalues.JPObjectApplyValueServiceAware;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.beans.JsonApplyValueResult;
import mp.jprime.json.beans.JsonApplyValuesQuery;
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
public class RestApiApplyValueController implements JPObjectApplyValueServiceAware {
  private JPJsonMapper jpJsonMapper;
  private JPObjectApplyValueService jpObjectApplyValueService;
  private JWTService jwtService;
  private JPMetaFilter jpMetaFilter;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Override
  public void setJPObjectApplyValueService(JPObjectApplyValueService jpObjectApplyValueService) {
    this.jpObjectApplyValueService = jpObjectApplyValueService;
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
  @PostMapping(value = "/{code}/applyvalue", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonApplyValueResult> getApplyValue(ServerWebExchange swe,
                                                  @PathVariable("code") String code,
                                                  @RequestBody String query) {

    return JPMono.fromCallable(() -> {
          AuthInfo auth = jwtService.getAuthInfo(swe);
          JPClass jpClass = jpMetaFilter.get(code, auth);
          if (jpClass == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          JsonApplyValuesQuery jsonQuery;
          try {
            jsonQuery = jpJsonMapper.toObject(JsonApplyValuesQuery.class, query);
          } catch (JPRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          Object id = jsonQuery.getId();
          String classCode = jsonQuery.getClassCode();
          if (!jpClass.getCode().equals(classCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
          }
          JPData data = jpObjectApplyValueService.getApplyValues(
              JPObjectApplyValueParamsBean.newBuilder(id, classCode, JPData.of(jsonQuery.getData()))
                  .attrs(jsonQuery.getAttrs())
                  .auth(auth)
                  .source(Source.USER)
                  .build()
          );
          return JsonApplyValueResult.newBuilder()
              .id(id)
              .classCode(classCode)
              .data(data.toMap())
              .build();
        });
  }
}
