package mp.jprime.api.rest.controllers;

import mp.jprime.json.beans.JsonUniqueValues;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class RestApiUniqueValuesController extends RestApiUniqueValuesBaseController {

  @ResponseBody
  @PostMapping(value = "/{code}/uniquevalues", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonUniqueValues> getUniqueValues(ServerWebExchange swe,
                                                @PathVariable("code") String code,
                                                @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    JPClass jpClass = jpMetaFilter.get(code, auth);
    if (jpClass == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return getJsonUniqueValues(swe, code, query);
  }
}
