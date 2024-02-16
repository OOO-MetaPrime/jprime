package mp.jprime.api.rest.controllers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.files.controllers.DownloadFileRestController;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/v1")
public class RestApiMultipartCRUDController extends DownloadFileRestController {

  @GetMapping(value = "/{code}/{objectId}/file/{attrCode}/{bearer}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFile(ServerWebExchange swe,
                                 @PathVariable("code") String classCode,
                                 @PathVariable("objectId") String objectId,
                                 @PathVariable("attrCode") String attrCode,
                                 @PathVariable("bearer") String bearer,
                                 @RequestParam(value = "base", required = false) boolean base,
                                 @RequestParam(value = "stamp", required = false) boolean stamp,
                                 @RequestParam(value = "sign", required = false) boolean sign,
                                 @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.justOrEmpty(metaStorage.getJPClassByCode(classCode))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || objectId == null || jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMapMany(jpClass -> Flux.fromIterable(getRequestedFileAttrs(jpClass, attrCode, base, stamp, sign)))
        .parallel()
        .runOn(Schedulers.parallel())
        .flatMap(attr -> jpFileLoader.asyncGetInfo(JPId.get(classCode, objectId), attr, jwtService.getAuthInfo(bearer, swe)))
        .sequential()
        .switchIfEmpty(Mono.error(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .collectList()
        .flatMap(infoList -> infoList.size() == 1 ? writeTo(swe, infoList.iterator().next(), userAgent) : writeZipTo(swe, infoList, userAgent));
  }

  @GetMapping(value = "/{code}/{objectId}/file/{attrCode}/{linkCode}/{linkValue}/{bearer}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFile(ServerWebExchange swe,
                                 @PathVariable("code") String code,
                                 @PathVariable("objectId") String objectId,
                                 @PathVariable("attrCode") String attrCode,
                                 @PathVariable("linkCode") String linkCode,
                                 @PathVariable("linkValue") String linkValue,
                                 @PathVariable("bearer") String bearer,
                                 @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.justOrEmpty(metaStorage.getJPClassByCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || objectId == null || jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(
            jpClass -> jpFileLoader.asyncGetInfo(JPId.get(jpClass.getCode(), objectId), Filter.attr(linkCode).eq(linkValue), attrCode, jwtService.getAuthInfo(bearer, swe))
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .flatMap(
            fileInfo -> writeTo(swe, fileInfo, userAgent)
        );
  }

  @GetMapping(value = "/{code}/search/file/{attrCode}/{linkCode}/{linkValue}/{bearer}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFilesZip(ServerWebExchange swe,
                                     @PathVariable("code") String code,
                                     @PathVariable("attrCode") String attrCode,
                                     @PathVariable("linkCode") String linkCode,
                                     @PathVariable("linkValue") String linkValue,
                                     @PathVariable("bearer") String bearer,
                                     @RequestParam(value = "base", required = false) boolean base,
                                     @RequestParam(value = "stamp", required = false) boolean stamp,
                                     @RequestParam(value = "sign", required = false) boolean sign,
                                     @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.justOrEmpty(metaStorage.getJPClassByCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || jpClass == null || jpClass.isInner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMapMany(jpClass -> Flux.fromIterable(getRequestedFileAttrs(jpClass, attrCode, base, stamp, sign)))
        .parallel()
        .runOn(Schedulers.parallel())
        .flatMap(
            attr -> jpFileLoader.asyncGetInfos(code, Filter.attr(linkCode).eq(linkValue), attr, jwtService.getAuthInfo(bearer, swe))
        )
        .sequential()
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .collectList()
        .flatMap(
            fileInfos -> writeZipTo(swe, fileInfos, userAgent)
        );
  }

  @ResponseBody
  @PostMapping(value = "/{code}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JsonJPObject> createObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody Flux<Part> parts) {
    return super.createObject(swe, code, parts);
  }

  @ResponseBody
  @PutMapping(value = "/{code}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JsonJPObject> updateObject(ServerWebExchange swe,
                                         @PathVariable("code") String code,
                                         @RequestBody Flux<Part> parts) {
    return super.updateObject(swe, code, parts);
  }

  @GetMapping(value = "/{code}/{objectId}/file/{attrCode}/anonymous")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFileAnonymous(ServerWebExchange swe,
                                          @PathVariable("code") String code,
                                          @PathVariable("objectId") String objectId,
                                          @PathVariable("attrCode") String attrCode,
                                          @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.justOrEmpty(metaStorage.getJPClassByCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || objectId == null || jpClass == null || jpClass.isInner() || !jpMetaFilter.anonymousFilter(jpClass)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMap(
            jpClass -> jpFileLoader.asyncGetInfo(JPId.get(jpClass.getCode(), objectId), attrCode, null)
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .flatMap(
            fileInfo -> writeTo(swe, fileInfo, userAgent)
        );
  }

  @GetMapping(value = "/{code}/search/file/{attrCode}/{linkCode}/{linkValue}/anonymous")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> downloadFilesAnonymous(ServerWebExchange swe,
                                           @PathVariable("code") String code,
                                           @PathVariable("attrCode") String attrCode,
                                           @PathVariable("linkCode") String linkCode,
                                           @PathVariable("linkValue") String linkValue,
                                           @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
    return Mono.justOrEmpty(metaStorage.getJPClassByCode(code))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(jpClass -> {
          if (attrCode == null || jpClass == null || jpClass.isInner() || !jpMetaFilter.anonymousFilter(jpClass)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          }
          return jpClass;
        })
        .flatMapMany(
            jpClass -> jpFileLoader.asyncGetInfos(jpClass.getCode(), Filter.attr(linkCode).eq(linkValue), attrCode, null)
        )
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .collectList()
        .flatMap(
            fileInfos -> writeZipTo(swe, fileInfos, userAgent)
        );
  }

  private Collection<String> getRequestedFileAttrs(JPClass jpClass, String attrCode, boolean base, boolean stamp, boolean sign) {
    JPAttr baseAttr = jpClass.getAttr(attrCode);
    if (baseAttr == null || baseAttr.getType() != JPType.FILE) {
      return Collections.emptyList();
    }
    Collection<String> fileAttrs = new HashSet<>(3);
    if (base || !(stamp || sign)) {
      fileAttrs.add(attrCode);
    }
    if (stamp) {
      JPFile refJpFile = baseAttr.getRefJpFile();
      String fileStampAttrCode = refJpFile == null ? null : refJpFile.getFileStampAttrCode();
      JPAttr stampAttr = jpClass.getAttr(fileStampAttrCode);
      if (stampAttr != null && stampAttr.getType() == JPType.FILE) {
        fileAttrs.add(fileStampAttrCode);
      }
    }
    if (sign) {
      String signAttrCode = baseAttr.getSignAttrCode();
      JPAttr signAttr = jpClass.getAttr(signAttrCode);
      if (signAttr != null && signAttr.getType() == JPType.FILE) {
        fileAttrs.add(signAttrCode);
      }
    }
    return fileAttrs;
  }
}
