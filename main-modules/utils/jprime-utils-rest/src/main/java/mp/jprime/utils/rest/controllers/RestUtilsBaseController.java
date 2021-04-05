package mp.jprime.utils.rest.controllers;

import mp.jprime.common.JPClassAttr;
import mp.jprime.common.JPEnum;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.parsers.exceptions.JPParseException;
import mp.jprime.utils.json.*;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.streams.UploadInputStream;
import mp.jprime.streams.services.UploadInputStreamService;
import mp.jprime.utils.*;
import mp.jprime.utils.exceptions.JPUtilNotFoundException;
import mp.jprime.utils.services.JPUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

public abstract class RestUtilsBaseController {
  private static final Logger LOG = LoggerFactory.getLogger(RestUtilsBaseController.class);
  /**
   * Сервис работы с утилитами
   */
  protected JPUtilService jpUtilService;
  /**
   * Обработчик JWT
   */
  protected JWTService jwtService;
  /**
   * Работа с UploadInputStream
   */
  protected UploadInputStreamService uploadInputStreamService;
  protected JPJsonMapper jpJsonMapper;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Autowired
  private void setJpUtilService(JPUtilService jpUtilService) {
    this.jpUtilService = jpUtilService;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setUploadInputStreamService(UploadInputStreamService uploadInputStreamService) {
    this.uploadInputStreamService = uploadInputStreamService;
  }

  @ResponseBody
  @PostMapping(value = "/{utilCode}/mode/{modeCode}",
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE},
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JPUtilOutParams> executeUtilJsonStep(ServerWebExchange swe,
                                                   @PathVariable("utilCode") String utilCode,
                                                   @PathVariable("modeCode") String modeCode,
                                                   @RequestBody String query) {
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Mono.just(authInfo)
        .flatMap(x -> jpUtilService.apply(utilCode, modeCode, x))
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException(utilCode)))
        .flatMap(mode -> {
          try {
            Class inClass = mode.getInClass();
            JPUtilInParams inParams = inClass == null ? null : (JPUtilInParams) jpJsonMapper.getObjectMapper().readValue(query, inClass);
            return Mono.zip(Mono.just(mode), Mono.justOrEmpty(inParams));
          } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JPParseException("utils.params.badFormat", "Неверный формат данных");
          }
        })
        .flatMap(tuple -> jpUtilService.apply(tuple.getT1(), tuple.getT2(), swe, authInfo));
  }

  @ResponseBody
  @PostMapping(value = "/{utilCode}/mode/{modeCode}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JPUtilOutParams> executeUtilMultiPartStep(ServerWebExchange swe,
                                                        @PathVariable("utilCode") String utilCode,
                                                        @PathVariable("modeCode") String modeCode) {
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Mono.just(authInfo)
        .flatMap(x -> jpUtilService.apply(utilCode, modeCode, x))
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException(utilCode)))
        .flatMap(mode ->
            Mono.zip(
                Mono.just(mode),
                swe.getMultipartData()
                    .map(MultiValueMap::toSingleValueMap)
                    .flatMapIterable(Map::entrySet)
                    .filter(x -> x.getValue() instanceof FormFieldPart)
                    .flatMap(x -> Mono.zip(Mono.just(x.getKey()), Mono.just(((FormFieldPart) x.getValue()).value())))
                    .collectMap(Tuple2::getT1, Tuple2::getT2),
                swe.getMultipartData()
                    .map(MultiValueMap::toSingleValueMap)
                    .flatMapIterable(Map::entrySet)
                    .filter(x -> x.getValue() instanceof FilePart)
                    .flatMap(x -> Mono.zip(Mono.just(x.getKey()), getIsValue((FilePart) x.getValue())))
                    .collectMap(Tuple2::getT1, Tuple2::getT2)
            )
        )
        .flatMap(tuple -> {
          try {
            JPUtilMode mode = tuple.getT1();
            Map<String, String> stringData = tuple.getT2();
            Map<String, UploadInputStream> isData = tuple.getT3();

            Class inClass = mode.getInClass();
            JPUtilInParams inParams = null;
            if (inClass != null) {
              inParams = (JPUtilInParams) jpJsonMapper.getObjectMapper().readValue(
                  jpJsonMapper.getObjectMapper().writeValueAsString(stringData), inClass)
              ;

              BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(inParams);
              wrapper.setAutoGrowNestedPaths(true);
              wrapper.setPropertyValues(isData);
            }

            return Mono.zip(Mono.just(mode), Mono.justOrEmpty(inParams));
          } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JPParseException("utils.params.badFormat", "Неверный формат данных");
          }
        })
        .flatMap(tuple -> jpUtilService.apply(tuple.getT1(), tuple.getT2(), swe, authInfo));
  }

  public Mono<UploadInputStream> getIsValue(FilePart part) {
    return Mono.just(part)
        .flatMap(uploadInputStreamService::read);
  }

  @ResponseBody
  @GetMapping(value = "/labels",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).ADMIN)")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilModeLabel> getUtilModeLabelList(ServerWebExchange swe) {
    return jpUtilService.getUtils(jwtService.getAuthInfo(swe))
        .map(this::toUtilModeLabel);
  }

  @ResponseBody
  @GetMapping(value = "/settings",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilMode> getUtilModeList(ServerWebExchange swe) {
    return jpUtilService.getUtils(jwtService.getAuthInfo(swe))
        .map(this::toUtilMode);
  }

  @ResponseBody
  @GetMapping(value = "/settings/{classCode}",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilMode> getUtilModeList(ServerWebExchange swe,
                                            @PathVariable("classCode") String classCode) {
    return jpUtilService.getUtils(classCode, jwtService.getAuthInfo(swe))
        .map(this::toUtilMode);
  }

  private JsonUtilModeLabel toUtilModeLabel(JPUtilMode utilMode) {
    return JsonUtilModeLabel.newBuilder()
        .utilCode(utilMode.getUtilCode())
        .modeCode(utilMode.getModeCode())
        .title(utilMode.getTitle())
        .qName(utilMode.getQName())
        .build();
  }

  private JsonUtilMode toUtilMode(JPUtilMode utilMode) {
    return JsonUtilMode.newBuilder()
        .utilCode(utilMode.getUtilCode())
        .modeCode(utilMode.getModeCode())
        .title(utilMode.getTitle())
        .qName(utilMode.getQName())
        .confirmMessage(utilMode.getConfirmMessage())
        .jpClasses(utilMode.getJpClasses())
        .type(utilMode.getType().getCode())
        .jpAttrs(utilMode.getJpAttrs()
            .stream()
            .map(this::toUtilClassAttr)
            .collect(Collectors.toList())
        )
        .inParams(utilMode.getInParams()
            .stream()
            .map(this::toUtilParam)
            .collect(Collectors.toList())
        )
        .resultType(utilMode.getResultType())
        .outCustomParams(utilMode.getOutCustomParams()
            .stream()
            .map(this::toUtilParam)
            .collect(Collectors.toList())
        )
        .build();
  }

  private JsonUtilParam toUtilParam(JPUtilParam utilParam) {
    return JsonUtilParam.newBuilder()
        .code(utilParam.getCode())
        .type(utilParam.getType() != null ? utilParam.getType().getCode() : null)
        .length(utilParam.getLength())
        .description(utilParam.getDescription())
        .qName(utilParam.getQName())
        .mandatory(utilParam.isMandatory())
        .multiple(utilParam.isMultiple())
        .refJpClass(utilParam.getRefJpClassCode())
        .refJpAttr(utilParam.getRefJpAttrCode())
        .refFilter(utilParam.getRefFilter())
        .enums(utilParam.getEnums()
            .stream()
            .map(this::toUtilEnum)
            .collect(Collectors.toList()))
        .build();
  }

  private JsonUtilEnum toUtilEnum(JPEnum paramEnum) {
    return JsonUtilEnum.newBuilder()
        .description(paramEnum.getDescription())
        .qName(paramEnum.getQName())
        .value(paramEnum.getValue())
        .build();
  }

  private JsonUtilClassAttr toUtilClassAttr(JPClassAttr classAttr) {
    return JsonUtilClassAttr.newBuilder()
        .jpClass(classAttr.getJpClass())
        .jpAttr(classAttr.getJpAttr())
        .build();
  }
}