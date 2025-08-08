package mp.jprime.utils.rest.controllers;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.parsers.exceptions.JPParseException;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.streams.UploadInputStream;
import mp.jprime.streams.services.UploadInputStreamService;
import mp.jprime.utils.*;
import mp.jprime.utils.exceptions.JPUtilNotFoundException;
import mp.jprime.utils.json.JsonUtilMode;
import mp.jprime.utils.json.JsonUtilModeLabel;
import mp.jprime.utils.json.converters.JsonUtilsConverter;
import org.apache.commons.lang3.StringUtils;
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
import reactor.util.function.Tuple4;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
  /**
   * Базовый класс JSON-обработчиков
   */
  protected JPJsonMapper jpJsonMapper;

  private JsonUtilsConverter converter;

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

  @Autowired
  private void setConverter(JsonUtilsConverter converter) {
    this.converter = converter;
  }

  @ResponseBody
  @PostMapping(value = "/batchCheck",
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE},
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JPUtilBatchCheckOutParams> batchCheck(ServerWebExchange swe,
                                                    @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    return JPMono.fromCallable(() -> auth)
        .flatMapMany(x -> {
          try {
            JPUtilBatchCheckInParams inParams = jpJsonMapper.toObject(JPUtilBatchCheckInParams.class, query);
            Collection<JPUtilBatchCheckInParams.CheckIds> checkIds = inParams.getIds();
            Collection<String> utils = inParams.getUtils();
            if (checkIds == null || checkIds.isEmpty() || utils == null || utils.isEmpty()) {
              return Flux.empty();
            }
            String rootObjectClassCode = inParams.getRootObjectClassCode();
            String rootObjectId = inParams.getRootObjectId();

            Collection<Mono<Tuple4<String, String, String, JPUtilCheckOutParams>>> p = new ArrayList<>();

            for (JPUtilBatchCheckInParams.CheckIds checkId : checkIds) {
              String objectClassCode = checkId.getObjectClassCode();
              Collection<String> ids = checkId.getObjectIds();
              if (StringUtils.isBlank(objectClassCode) || ids == null || ids.isEmpty()) {
                continue;
              }
              ids.forEach(id ->
                  utils.forEach(utilCode ->
                      p.add(
                          Mono.zip(
                              Mono.just(objectClassCode),
                              Mono.just(id),
                              Mono.just(utilCode),
                              jpUtilService.check(
                                  utilCode,
                                  JPUtil.Mode.CHECK_MODE,
                                  JPUtilCheckInParams.newBuilder()
                                      .rootObjectClassCode(rootObjectClassCode)
                                      .rootObjectId(rootObjectId)
                                      .objectClassCode(objectClassCode)
                                      .objectId(id)
                                      .build(),
                                  swe,
                                  auth
                              )
                          )
                      )
                  )
              );
            }
            return Flux.merge(p);
          } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JPParseException("utils.params.badFormat", "Неверный формат данных");
          }
        })
        .reduce(JPUtilBatchCheckOutParams.newBuilder(), (builder, next) -> builder.add(next.getT1(), next.getT2(), next.getT3(), next.getT4()))
        .map(JPUtilBatchCheckOutParams.Builder::build)
        .switchIfEmpty(Mono.just(JPUtilBatchCheckOutParams.newBuilder().build()));
  }

  @ResponseBody
  @PostMapping(value = "/{utilCode}/mode/{modeCode}",
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE},
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JPUtilOutParams> executeUtilJsonStep(ServerWebExchange swe,
                                                   @PathVariable("utilCode") String utilCode,
                                                   @PathVariable("modeCode") String modeCode,
                                                   @RequestBody String query) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    return JPMono.fromCallable(() -> auth)
        .flatMap(x -> jpUtilService.apply(utilCode, modeCode, x))
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException(utilCode)))
        .flatMap(mode -> {
          try {
            Class<?> inClass = mode.getInClass();
            JPUtilInParams inParams = inClass == null ? null : (JPUtilInParams) jpJsonMapper.toObject(inClass, query);
            return Mono.zip(Mono.just(mode), Mono.justOrEmpty(inParams));
          } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new JPParseException("utils.params.badFormat", "Неверный формат данных");
          }
        })
        .flatMap(tuple -> jpUtilService.apply(tuple.getT1(), tuple.getT2(), swe, auth));
  }

  @ResponseBody
  @PostMapping(value = "/{utilCode}/mode/{modeCode}",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JPUtilOutParams> executeUtilMultiPartStep(ServerWebExchange swe,
                                                        @PathVariable("utilCode") String utilCode,
                                                        @PathVariable("modeCode") String modeCode) {
    AuthInfo auth = jwtService.getAuthInfo(swe);
    AtomicReference<Collection<UploadInputStream>> is = new AtomicReference<>(Collections.emptyList());
    return JPMono.fromCallable(() -> auth)
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
            is.set(isData.values());

            Class<?> inClass = mode.getInClass();
            JPUtilInParams inParams = null;
            if (inClass != null) {
              Map<String, Object> inputData = new HashMap<>(stringData.size());
              for (Map.Entry<String, String> entry : stringData.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (v.startsWith("[") && v.endsWith("]")) {
                  inputData.put(k, jpJsonMapper.toObject(String[].class, v));
                } else {
                  inputData.put(k, v);
                }
              }

              inParams = (JPUtilInParams) jpJsonMapper.toObject(
                  inClass,
                  jpJsonMapper.toString(inputData)
              );

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
        .flatMap(tuple -> jpUtilService.apply(tuple.getT1(), tuple.getT2(), swe, auth))
        .doOnTerminate(() -> is.get().forEach(UploadInputStream::close));
  }

  public Mono<UploadInputStream> getIsValue(FilePart part) {
    return Mono.just(part)
        .flatMap(uploadInputStreamService::read);
  }

  @ResponseBody
  @GetMapping(value = "/labels",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getUiAdmin())")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilModeLabel> getUtilModeLabelList() {
    return jpUtilService.getUtils()
        .map(converter::toUtilModeLabel);
  }

  @ResponseBody
  @GetMapping(value = "/settings",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilMode> getUtilModeList(ServerWebExchange swe) {
    return jpUtilService.getUtils(jwtService.getAuthInfo(swe))
        .map(converter::toUtilMode);
  }

  @ResponseBody
  @GetMapping(value = "/settings/{classCode}",
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(@JPRoleConst.getAuthAccess())")
  @ResponseStatus(HttpStatus.OK)
  public Flux<JsonUtilMode> getUtilModeList(ServerWebExchange swe,
                                            @PathVariable("classCode") String classCode) {
    return jpUtilService.getUtils(classCode, jwtService.getAuthInfo(swe))
        .map(converter::toUtilMode);
  }
}
