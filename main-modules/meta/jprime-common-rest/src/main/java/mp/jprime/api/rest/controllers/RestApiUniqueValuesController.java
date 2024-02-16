package mp.jprime.api.rest.controllers;

import mp.jprime.json.beans.JsonUniqueValues;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
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
public class RestApiUniqueValuesController extends RestApiUniqueValuesBaseController {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @ResponseBody
  @PostMapping(value = "/{code}/uniquevalues", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonUniqueValues> getUniqueValues(ServerWebExchange swe,
                                                @PathVariable("code") String code,
                                                @RequestBody String query) {
    JPClass jpClass = metaStorage.getJPClassByCode(code);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return getJsonUniqueValues(swe, code, query);
  }
}
