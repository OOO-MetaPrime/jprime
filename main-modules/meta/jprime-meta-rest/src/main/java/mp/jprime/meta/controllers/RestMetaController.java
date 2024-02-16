package mp.jprime.meta.controllers;

import mp.jprime.beans.PropertyType;
import mp.jprime.controllers.DownloadFile;
import mp.jprime.exceptions.JPForbiddenException;
import mp.jprime.meta.JPAttrCsvWriterService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPClassJsonConverter;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.JsonJPClass;
import mp.jprime.meta.json.beans.JsonJPClassList;
import mp.jprime.meta.json.beans.JsonPropertyType;
import mp.jprime.meta.json.beans.JsonType;
import mp.jprime.meta.security.Role;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("meta/v1")
public class RestMetaController implements DownloadFile {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage jpMetaStorage;
  /**
   * Фильтр меты
   */
  private JPMetaFilter jpMetaFilter;
  /**
   * Конвертер JpClass
   */
  private JPClassJsonConverter converter;

  /**
   * Сервис выгрузки JPAttr в CSV
   */
  private JPAttrCsvWriterService writerService;

  /**
   * Обработчик JWT
   */
  private JWTService jwtService;

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @Autowired
  private void setJpMetaStorage(JPMetaStorage jpMetaStorage) {
    this.jpMetaStorage = jpMetaStorage;
  }

  @Autowired
  private void setConverter(JPClassJsonConverter converter) {
    this.converter = converter;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setWriterService(JPAttrCsvWriterService writerService) {
    this.writerService = writerService;
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses/{classCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  public Mono<JsonJPClass> getClass(@PathVariable("classCode") String classCode) {
    JPClass jpClass = jpMetaStorage.getJPClassByCode(classCode);
    if (!jpMetaFilter.filter(jpClass)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Mono.just(converter.toJson(jpClass));
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  public Mono<JsonJPClassList> getClassList() {
    Collection<JPClass> classes = jpMetaStorage.getJPClasses();
    List<JsonJPClass> list = classes == null || classes.isEmpty() ? Collections.emptyList() : classes
        .stream()
        .filter(jpClass -> jpMetaFilter.filter(jpClass))
        .map(converter::toJson)
        .collect(Collectors.toList());
    return Mono.just(JsonJPClassList.newBuilder()
        .classes(list)
        .totalCount(list.size())
        .count(list.size())
        .build());
  }

  @ResponseBody
  @GetMapping(value = "attrTypes", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  public Flux<JsonType> getAttrTypes() {
    return Flux.fromArray(JPType.values())
        .filter(x -> x != JPType.NONE)
        .map(JsonType::from);
  }

  @ResponseBody
  @GetMapping(value = "propertyTypes", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  public Flux<JsonPropertyType> getPropertyTypes() {
    return Flux.fromArray(PropertyType.values())
        .map(JsonPropertyType::from);
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses/{classCode}/csv-export/{bearer}")
  public Mono<Void> export(ServerWebExchange swe,
                           @PathVariable("classCode") String classCode,
                           @PathVariable("bearer") String bearer,
                           @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    AuthInfo authInfo = jwtService.getAuthInfo(bearer, swe);
    if (!authInfo.getRoles().contains(Role.META_ADMIN)) {
      return Mono.error(new JPForbiddenException());
    }

    return Mono.justOrEmpty(jpMetaStorage.getJPClassByCode(classCode))
        .filter(jpMetaFilter::filter)
        .switchIfEmpty(Mono.error(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .flatMap(jpClass -> writeTo(swe, writerService.of(jpClass), jpClass.getCode() + " (" + jpClass.getName() + ").csv", userAgent));
  }
}