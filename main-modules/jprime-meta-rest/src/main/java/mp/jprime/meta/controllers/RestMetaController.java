package mp.jprime.meta.controllers;

import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.*;
import mp.jprime.meta.services.JPMetaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("meta/v1")
public class RestMetaController {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;

  /**
   * Конструктор
   *
   * @param metaStorage Хранилище метаинформации
   */
  public RestMetaController(@Autowired JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses/{classCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClass> getClass(@PathVariable("classCode") String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null || jpClass.isInner()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Mono.just(toJson(jpClass));
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClassList> getClassList() {
    Collection<JPClass> classes = metaStorage.getJPClasses();
    if (classes == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    List<JsonJPClass> list = classes
        .stream()
        .filter(x -> !x.isInner())
        .map(this::toJson)
        .collect(Collectors.toList());
    return Mono.just(JsonJPClassList.newBuilder()
        .classes(list)
        .totalCount(list.size())
        .count(list.size())
        .build());
  }

  @ResponseBody
  @GetMapping(value = "/attrTypes",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.meta.security.Role).META_ADMIN)")
  public Flux<JsonType> getAttrTypes() {
    return Flux.fromArray(JPType.values())
        .filter(x-> x != JPType.NONE)
        .map(x -> new JsonType(x.getCode(), x.getTitle()));
  }

  private JsonJPClass toJson(JPClass jpClass) {
    return JsonJPClass.newBuilder()
        .code(jpClass.getCode())
        .guid(jpClass.getGuid())
        .pluralCode(jpClass.getPluralCode())
        .qName(jpClass.getQName())
        .name(jpClass.getName())
        .shortName(jpClass.getShortName())
        .description(jpClass.getDescription())
        .jpPackage(jpClass.getJpPackage())
        .immutable(jpClass.isImmutable())
        .attrs(jpClass
            .getAttrs()
            .stream()
            .map(this::toJson)
            .collect(Collectors.toList())
        )
        .build();
  }

  private JsonJPAttr toJson(JPAttr jpAttr) {
    JPFile jpFile = jpAttr.getRefJpFile();
    return JsonJPAttr.newBuilder()
        .code(jpAttr.getCode())
        .guid(jpAttr.getGuid())
        .qName(jpAttr.getQName())
        .name(jpAttr.getName())
        .shortName(jpAttr.getShortName())
        .description(jpAttr.getDescription())
        .jpPackage(jpAttr.getJpPackage())
        .identifier(jpAttr.isIdentifier())
        .mandatory(jpAttr.isMandatory())
        .type(jpAttr.getValueType().getCode())
        .refJpClass(jpAttr.getRefJpClassCode())
        .refJpAttr(jpAttr.getRefJpAttrCode())
        .length(jpAttr.getLength())
        .refJpFile(
            jpFile == null || jpAttr.getType() != JPType.FILE ? null : JsonJPFile.newBuilder()
                .titleAttr(jpFile.getFileTitleAttrCode())
                .extAttr(jpFile.getFileExtAttrCode())
                .sizeAttr(jpFile.getFileSizeAttrCode())
                .dateAttr(jpFile.getFileDateAttrCode())
                .build()
        )
        .build();
  }
}
