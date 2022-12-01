package mp.jprime.meta.controllers;

import mp.jprime.meta.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("meta/v1")
public class RestMetaController {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage jpMetaStorage;
  /**
   * Фильтр меты
   */
  private JPMetaFilter jpMetaFilter;

  @Autowired
  private void setJpMetaFilter(JPMetaFilter jpMetaFilter) {
    this.jpMetaFilter = jpMetaFilter;
  }

  @Autowired
  private void setJpMetaStorage(JPMetaStorage jpMetaStorage) {
    this.jpMetaStorage = jpMetaStorage;
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses/{classCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClass> getClass(@PathVariable("classCode") String classCode) {
    JPClass jpClass = jpMetaStorage.getJPClassByCode(classCode);
    if (!jpMetaFilter.filter(jpClass)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return Mono.just(toJson(jpClass));
  }

  @ResponseBody
  @GetMapping(value = "/jpClasses", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClassList> getClassList() {
    Collection<JPClass> classes = jpMetaStorage.getJPClasses();
    List<JsonJPClass> list = classes == null || classes.isEmpty() ? Collections.emptyList() : classes
        .stream()
        .filter(jpClass -> jpMetaFilter.filter(jpClass))
        .map(this::toJson)
        .collect(Collectors.toList());
    return Mono.just(JsonJPClassList.newBuilder()
        .classes(list)
        .totalCount(list.size())
        .count(list.size())
        .build());
  }

  @ResponseBody
  @GetMapping(value = "attrTypes",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Flux<JsonType> getAttrTypes() {
    return Flux.fromArray(JPType.values())
        .filter(x -> x != JPType.NONE)
        .map(JsonType::from);
  }

  private JsonJPClass toJson(JPClass jpClass) {
    return JsonJPClass.newBuilder()
        .code(jpClass.getCode())
        .guid(jpClass.getGuid())
        .qName(jpClass.getQName())
        .tags(jpClass.getTags())
        .name(jpClass.getName())
        .shortName(jpClass.getShortName())
        .description(jpClass.getDescription())
        .jpPackage(jpClass.getJpPackage())
        .immutable(jpClass.isImmutable())
        .attrs(jpClass
            .getAttrs()
            .stream()
            .map(this::toJson)
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
        )
        .build();
  }

  private JsonJPAttr toJson(JPAttr jpAttr) {
    JPType type = jpAttr.getValueType();
    JPFile jpFile = jpAttr.getRefJpFile();
    JPSimpleFraction simpleFraction = jpAttr.getSimpleFraction();
    JPMoney money = jpAttr.getMoney();
    JPGeometry geometry = jpAttr.getGeometry();
    if (type == null) {
      return null;
    }
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
        .type(type.getCode())
        .updatable(jpAttr.isUpdatable())
        .length(jpAttr.getLength())
        // Настройка ссылки класс+атрибут
        .refJpClass(jpAttr.getRefJpClassCode())
        .refJpAttr(jpAttr.getRefJpAttrCode())
        // Настройка файла
        .refJpFile(
            type != JPType.FILE || jpFile == null ? null :
                JsonJPFile.newBuilder()
                    .titleAttr(jpFile.getFileTitleAttrCode())
                    .extAttr(jpFile.getFileExtAttrCode())
                    .sizeAttr(jpFile.getFileSizeAttrCode())
                    .dateAttr(jpFile.getFileDateAttrCode())
                    .infoAttr(jpFile.getFileInfoAttrCode())
                    .build()
        )
        // Настройка простой дроби
        .simpleFraction(
            type != JPType.SIMPLEFRACTION || simpleFraction == null ? null :
                JsonJPSimpleFraction.newBuilder()
                    .integerAttr(simpleFraction.getIntegerAttrCode())
                    .denominatorAttr(simpleFraction.getDenominatorAttrCode())
                    .build()
        )
        // Настройка денежного типа
        .money(
            type != JPType.MONEY || money == null ? null :
                JsonJPMoney.newBuilder()
                    .currencyCode(money.getCurrencyCode())
                    .build()
        )
        // Настройка геометрии
        .geometry(
            type != JPType.GEOMETRY || geometry == null ? null : JsonJPGeometry.newBuilder()
                .srid(geometry.getSRID())
                .build()
        )
        // Свойства псевдо-меты
        .schemaProps(
            toJsonJPProperty(jpAttr.getSchemaProps())
        )
        .build();
  }

  private Collection<JsonJPProperty> toJsonJPProperty(Collection<JPProperty> properties) {
    return properties == null ? null :
        properties.stream().map(this::toJsonJPProperty).collect(Collectors.toList());
  }

  private JsonJPProperty toJsonJPProperty(JPProperty property) {
    return JsonJPProperty.builder()
        .code(property.getCode())
        .type(property.getType() == null ? null : property.getType().getCode())
        .length(property.getLength())
        .multiple(property.isMultiple())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .shortName(property.getShortName())
        .description(property.getDescription())
        .qName(property.getQName())
        .refJpClassCode(property.getRefJpClassCode())
        .refJpAttrCode(property.getRefJpAttrCode())
        .schemaProps(toJsonJPProperty(property.getSchemaProps()))
        .build();
  }

}