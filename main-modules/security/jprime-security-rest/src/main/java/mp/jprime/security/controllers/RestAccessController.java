package mp.jprime.security.controllers;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.beans.JPAccessType;
import mp.jprime.security.json.beans.JsonJPClassAccess;
import mp.jprime.security.json.beans.JsonJPObjectAccess;
import mp.jprime.security.json.beans.JsonSecurityAccess;
import mp.jprime.security.json.beans.JsonSecurityPackage;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("access/v1")
public class RestAccessController {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Хранилище настроек безопасности
   */
  private JPSecurityStorage securityManager;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;
  /**
   * Интерфейс проверки доступа к объекту
   */
  private JPObjectAccessService objectAccessService;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @ResponseBody
  @GetMapping(value = "jpObjects/{pluralCode}/{objectId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPObjectAccess> getAccess(ServerWebExchange swe,
                                            @PathVariable("pluralCode") String pluralCode,
                                            @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner() || objectId == null || objectId.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    return Mono.just(toJPObjectAccessModel(jpClass, objectId, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpClasses/{classCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClassAccess> getInfo(ServerWebExchange swe,
                                         @PathVariable("classCode") String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Mono.just(toJPClassAccessModel(jpClass, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpClasses",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Flux<JsonJPClassAccess> getInfoList(ServerWebExchange swe) {
    Collection<JPClass> classes = metaStorage.getJPClasses();
    if (classes == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Flux.fromIterable(classes)
        .filter(x -> !x.isInner())
        .map(x -> toJPClassAccessModel(x, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpPackages",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyAuthority(T(mp.jprime.security.security.Role).AUTH_ADMIN, T(mp.jprime.meta.security.Role).META_ADMIN)")
  public Flux<JsonSecurityPackage> getJPPackages(ServerWebExchange swe) {
    Collection<JPSecurityPackage> packages = securityManager.getPackages();
    if (packages == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Flux.fromIterable(packages)
        .map(this::toSecurityPackage)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @ResponseBody
  @GetMapping(value = "jpPackages/{code}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyAuthority(T(mp.jprime.security.security.Role).AUTH_ADMIN, T(mp.jprime.meta.security.Role).META_ADMIN)")
  public Mono<JsonSecurityPackage> getJPPackages(@PathVariable("code") String code) {
    Collection<JPSecurityPackage> packages = securityManager.getPackages();
    if (packages == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Flux.fromIterable(packages)
        .filter(x -> x.getCode().equals(code))
        .map(this::toSecurityPackage)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .next();
  }

  private JsonJPObjectAccess toJPObjectAccessModel(JPClass jpClass, String objectId, AuthInfo auth) {
    return JsonJPObjectAccess.newBuilder()
        .objectClassCode(jpClass.getCode())
        .objectId(objectId)
        .read(
            objectAccessService.checkRead(JPId.get(jpClass.getCode(), objectId), auth)
        )
        .create(
            objectAccessService.checkCreate(jpClass.getCode(), auth)
        )
        .update(
            objectAccessService.checkUpdate(JPId.get(jpClass.getCode(), objectId), auth)
        )
        .delete(
            objectAccessService.checkDelete(JPId.get(jpClass.getCode(), objectId), auth)
        )
        .build();
  }

  private JsonJPClassAccess toJPClassAccessModel(JPClass jpClass, AuthInfo auth) {
    return JsonJPClassAccess.newBuilder()
        .classCode(jpClass.getCode())
        .read(securityManager.checkRead(jpClass.getJpPackage(), auth.getRoles()))
        .create(objectAccessService.checkCreate(jpClass.getCode(), auth))
        .update(securityManager.checkUpdate(jpClass.getJpPackage(), auth.getRoles()))
        .delete(securityManager.checkDelete(jpClass.getJpPackage(), auth.getRoles()))
        .build();
  }


  private JsonSecurityPackage toSecurityPackage(JPSecurityPackage pkg) {
    return JsonSecurityPackage.newBuilder()
        .code(pkg.getCode())
        .name(pkg.getName())
        .description(pkg.getDescription())
        .qName(pkg.getQName())
        .accesses(
            Stream.concat(
                pkg.getPermitAccess().stream()
                    .map(x -> JsonSecurityAccess.newBuilder()
                        .type(JPAccessType.PERMIT.getCode())
                        .role(x.getRole())
                        .read(x.isRead())
                        .create(x.isCreate())
                        .update(x.isUpdate())
                        .delete(x.isDelete())
                        .build())
                , pkg.getProhibitionAccess().stream()
                    .map(x -> JsonSecurityAccess.newBuilder()
                        .type(JPAccessType.PROHIBITION.getCode())
                        .role(x.getRole())
                        .read(x.isRead())
                        .create(x.isCreate())
                        .update(x.isUpdate())
                        .delete(x.isDelete())
                        .build())
            ).collect(Collectors.toList())
        )
        .build();
  }
}
