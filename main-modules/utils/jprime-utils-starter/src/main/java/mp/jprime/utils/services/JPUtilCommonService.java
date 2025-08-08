package mp.jprime.utils.services;

import com.fasterxml.jackson.core.JsonParseException;
import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;
import mp.jprime.common.annotations.JPEnum;
import mp.jprime.meta.annotations.JPMoney;
import mp.jprime.common.annotations.JPParam;
import mp.jprime.common.beans.JPClassAttrBean;
import mp.jprime.common.beans.JPParamBase;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPBadFormatException;
import mp.jprime.exceptions.JPQueryException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.log.AppLogger;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.JPMoneyBean;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.utils.*;
import mp.jprime.utils.annotations.JPUtilModeLink;
import mp.jprime.utils.annotations.JPUtilProperties;
import mp.jprime.utils.annotations.JPUtilResultType;
import mp.jprime.utils.exceptions.JPUtilModeNotFoundException;
import mp.jprime.utils.exceptions.JPUtilNotFoundException;
import mp.jprime.utils.exceptions.JPUtilRunRightException;
import mp.jprime.utils.log.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис работы с утилитами
 */
@Service
public final class JPUtilCommonService implements JPUtilService {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilCommonService.class);

  private static final Collection<String> SYSTEM_MODES = Set.of(
      JPUtil.Mode.CHECK_MODE,
      JPUtil.Mode.IN_PARAMS_DEF_VALUES,
      JPUtil.Mode.VALIDATE_MODE
  );

  private final Map<String, UtilInfo> jpUtils = new HashMap<>();
  private final Map<String, UtilInfo> jpUniUtils = new HashMap<>();

  // Системный журнал
  private AppLogger appLogger;
  // Хранилище настроек RBAC
  private JPSecurityStorage securityManager;
  // Базовый класс JSON-обработчиков
  private JPJsonMapper jpJsonMapper;
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;

  @Autowired
  private void setAppLogger(AppLogger appLogger) {
    this.appLogger = appLogger;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  /**
   * Считываем аннотации
   */
  private JPUtilCommonService(@Autowired(required = false) Collection<JPUtil> utils) {
    if (utils == null) {
      return;
    }
    for (JPUtil util : utils) {
      try {
        String url = util.getUrl();
        if (url == null) {
          continue;
        }
        UtilInfo utilInfo = new UtilInfo(util);
        boolean isUni = util.isUni();
        String[] utilJpClasses = util.getJpClasses();
        String[] utilJpClassTags = util.getJpClassTags();

        for (Method method : util.getClass().getMethods()) {
          JPUtilModeLink anno = method.getAnnotation(JPUtilModeLink.class);
          if (anno == null) {
            continue;
          }
          Class<?> returnType = method.getReturnType();
          if (!Mono.class.isAssignableFrom(returnType)) {
            continue;
          }
          Class<?> returnClass = anno.outClass();
          if (!JPUtilOutParams.class.isAssignableFrom(returnClass)) {
            continue;
          }
          JPUtilResultType a = returnClass.getAnnotation(JPUtilResultType.class);
          String resultType = a != null ? a.code() : "";

          ModeInfo info = new ModeInfo(
              util,
              method.getName(),
              method.getParameterTypes(),
              resultType,
              isUni,
              utilJpClasses,
              utilJpClassTags,
              StringUtils.hasText(anno.jpPackage()) ? anno.jpPackage() : util.getJpPackage(),
              anno.authRoles().length > 0 ? anno.authRoles() : util.getAuthRoles(),
              anno
          );
          utilInfo.modes.putIfAbsent(anno.code(), info);
        }
        // Добавляем болванку шага check
        utilInfo.modes.putIfAbsent(
            JPUtil.Mode.CHECK_MODE,
            new ModeInfo(util, isUni, utilJpClasses, utilJpClassTags, util.getJpPackage(), util.getAuthRoles())
        );

        if (isUni) {
          jpUniUtils.put(url, utilInfo);
        } else {
          jpUtils.put(url, utilInfo);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
  }

  /**
   * Возвращает список всех кодовых имен утилит
   *
   * @return список всех кодовых имен утилит
   */
  @Override
  public Collection<String> getUtilCodes() {
    return jpUtils.keySet();
  }

  /**
   * Возвращает список всех кодовых имен общих утилит
   *
   * @return список всех кодовых имен утилит
   */
  @Override
  public Collection<String> getUniUtilCodes() {
    return jpUniUtils.keySet();
  }

  @Override
  public JPUtil getUtil(String utilCode) {
    UtilInfo info = jpUtils.get(utilCode);
    if (info == null) {
      info = jpUniUtils.get(utilCode);
    }
    return info != null ? info.util : null;
  }

  @Override
  public JPUtilMode getMode(String utilCode, String methodCode) {
    UtilInfo util = jpUtils.get(utilCode);
    return util != null ? util.modes.get(methodCode) : null;
  }

  /**
   * Возвращает список режимов утилит
   *
   * @return Список режимов утилит
   */
  private Stream<ModeInfo> utilsStream() {
    return Stream.concat(jpUtilsStream(), jpUniUtilsStream());
  }

  /**
   * Возвращает список режимов утилит
   *
   * @return Список режимов утилит
   */
  private Stream<ModeInfo> jpUtilsStream() {
    return jpUtils.values()
        .stream()
        .map(x -> x.modes.values())
        .flatMap(Collection::stream)
        .filter(x -> !SYSTEM_MODES.contains(x.getModeCode()));
  }

  /**
   * Возвращает список режимов общих утилит
   *
   * @return Список режимов общих утилит
   */
  private Stream<ModeInfo> jpUniUtilsStream() {
    return jpUniUtils.values()
        .stream()
        .map(x -> x.modes.values())
        .flatMap(Collection::stream)
        .filter(x -> !SYSTEM_MODES.contains(x.getModeCode()));
  }

  @Override
  public Flux<? extends JPUtilMode> getUtils() {
    return JPMono.fromCallable(this::utilsStream)
        .flatMapMany(Flux::fromStream);
  }

  @Override
  public Flux<? extends JPUtilMode> getUtils(AuthInfo auth) {
    return JPMono.fromCallable(this::utilsStream)
        .flatMapMany(Flux::fromStream)
        .filter(x -> checkSecurity(x, auth));
  }

  @Override
  public Flux<? extends JPUtilMode> getUtils(String className, AuthInfo auth) {
    JPClass jpClass = metaStorage.getJPClassByCode(className);
    if (jpClass == null) {
      return Flux.empty();
    }
    return getUtils(auth)
        .filter(
            x -> x.isUni() ||
                x.getJpClasses().contains(jpClass.getCode()) ||
                !Collections.disjoint(x.getJpClassTags(), jpClass.getTags())
        );
  }

  @Override
  public Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth) {
    return apply(utilCode, methodCode, auth)
        .flatMap(x -> apply(x, in, swe, auth));
  }

  /**
   * Проверка доступа
   *
   * @param mode Шаг утилиты
   * @param auth Доступ
   * @return Да/Нет
   */
  private boolean checkSecurity(ModeInfo mode, AuthInfo auth) {
    if (mode == null) {
      return true;
    }
    String[] methodRoles = mode.getAuthRoles();
    if (methodRoles != null && methodRoles.length > 0) {
      if (auth == null || Collections.disjoint(auth.getRoles(), Arrays.asList(methodRoles))) {
        return false;
      }
    }
    String jpPackage = mode.getJpPackage();
    if (jpPackage != null) {
      return securityManager.checkRead(jpPackage, auth.getRoles());
    }
    return true;
  }

  @Override
  public Mono<ModeInfo> apply(String utilCode, String methodCode, AuthInfo auth) {
    return JPMono.fromCallable(() -> jpUtils.get(utilCode))
        .switchIfEmpty(Mono.justOrEmpty(jpUniUtils.get(utilCode)))
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException(utilCode)))
        .filter(x -> x.util != null)
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException(utilCode)))
        .map(x -> {
              ModeInfo info = x.modes.get(methodCode);
              if (info == null) {
                throw new JPUtilModeNotFoundException(utilCode, methodCode);
              }
              return info;
            }
        )
        .filter(x -> checkSecurity(x, auth))
        .switchIfEmpty(Mono.error(new JPUtilRunRightException(utilCode)));
  }

  @Override
  public Mono<JPUtilOutParams> apply(final JPUtilMode mode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth) {
    return JPMono.fromCallable(() -> mode)
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException("")))
        .flatMap(x -> apply(x.getUtilCode(), x.getModeCode(), auth))
        .flatMap(x -> {
              JPUtil util = x.util;
              String utilCode = x.utilCode;
              String modeCode = x.modeCode;
              String methodName = x.methodName;
              Collection<String> actionLogIgnoreParams = x.actionLogIgnoreParams;
              Class<?>[] inClasses = x.allInClasses;
              String resultType = x.resultType;

              // По умолчанию метод check, если не переопределен отрабатывает корректно
              if (methodName == null && JPUtil.Mode.CHECK_MODE.equals(modeCode)) {
                return Mono.just(JPUtilCheckOutParams.newBuilder().build());
              }
              if (methodName == null && JPUtil.Mode.IN_PARAMS_DEF_VALUES.equals(modeCode)) {
                return Mono.just(JPUtilDefValuesOutParams.newBuilder().build());
              }
              if (methodName == null || inClasses == null || inClasses.length == 0 || resultType == null) {
                return Mono.error(new JPUtilModeNotFoundException(utilCode, modeCode));
              }
              String subject = auth.getUsername();
              try {
                if (!SYSTEM_MODES.contains(modeCode) && x.isActionLog()) { // не логируем типовые режимы утилит
                  String s = jpJsonMapper.toString(in, actionLogIgnoreParams);
                  appLogger.debug(Event.UTIL_RUN, subject, utilCode + "/mode/" + modeCode, s, auth);
                }

                Object[] inParams = new Object[inClasses.length];
                for (int i = 0; i < inClasses.length; i++) {
                  Class<?> cls = inClasses[i];
                  if (AuthInfo.class.isAssignableFrom(cls)) {
                    inParams[i] = auth;
                  } else if (ServerWebExchange.class.isAssignableFrom(cls)) {
                    inParams[i] = swe;
                  } else if (JPUtilInParams.class.isAssignableFrom(cls)) {
                    inParams[i] = in;
                  }
                }

                Method method = util.getClass().getMethod(methodName, inClasses);
                return (Mono<JPUtilOutParams>) method.invoke(util, inParams);
              } catch (InvocationTargetException e) {
                Exception targetException = (Exception) e.getTargetException();
                if (targetException instanceof JPAppRuntimeException) {
                  return Mono.error(JPRuntimeException.wrapException(targetException));
                } else if (targetException instanceof JPRuntimeException) {
                  LOG.error(targetException.getMessage(), targetException);
                  return Mono.error(JPRuntimeException.wrapException(targetException));
                } else {
                  LOG.error(targetException.getMessage(), targetException);
                  return Mono.error(new JPQueryException());
                }
              } catch (JPAppRuntimeException e) {
                return Mono.error(e);
              } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof JsonParseException) {
                  return Mono.error(new JPBadFormatException());
                } else {
                  LOG.error(e.getMessage(), e);
                  return Mono.error(new JPQueryException());
                }
              }
            }
        );
  }

  /**
   * Описание утилит
   */
  private static class UtilInfo {
    private final JPUtil util;
    private final Map<String, ModeInfo> modes = new HashMap<>();

    private UtilInfo(JPUtil util) {
      this.util = util;
    }
  }


  private static class JPUtilModeProperties implements JPUtilMode.Properties {
    private final Collection<String> linkModes;
    private final Collection<UUID> compConfCodes;

    private JPUtilModeProperties(Collection<String> linkModes, Collection<UUID> compConfCodes) {
      this.linkModes = linkModes != null ? linkModes : Collections.emptyList();
      this.compConfCodes = compConfCodes != null ? compConfCodes : Collections.emptyList();
    }

    private static JPUtilModeProperties of(JPUtilProperties properties) {
      if (properties == null) {
        return null;
      }
      String[] linkModes = properties.linkModes();
      String[] compConfCodes = properties.compConfCodes();
      return new JPUtilModeProperties(
          linkModes != null ? Arrays.asList(linkModes) : null,
          compConfCodes != null ? Arrays.stream(compConfCodes)
              .map(UUID::fromString)
              .toList() : null
      );
    }

    @Override
    public Collection<String> getLinkModes() {
      return linkModes;
    }

    @Override
    public Collection<UUID> getCompConfCodes() {
      return compConfCodes;
    }
  }

  /**
   * Описание шагов
   */
  private static class ModeInfo implements JPUtilMode {
    private final JPUtil util;
    private final String utilCode;
    private final String modeCode;
    private final String title;
    private final String qName;
    private final String methodName;
    private final Class<?>[] allInClasses;
    private final String jpPackage;
    private final String[] authRoles;
    private final String confirmMessage;
    private final boolean uni;
    private final Collection<String> jpClasses;
    private final Collection<String> jpClassTags;
    private final JPAppendType type;
    private final Collection<JPClassAttr> jpAttrs;
    private final Class<?> paramInClass;
    private final Collection<JPUtilParam> inParams;
    private final Collection<String> actionLogIgnoreParams;
    private final boolean inParamsDefValues;
    private final String resultType;
    private final Collection<JPUtilParam> outCustomParams;
    private final boolean actionLog;
    private final String infoMessage;
    private final boolean validate;
    private final Properties properties;

    private ModeInfo(JPUtil util, boolean uni, String[] jpClasses, String[] jpClassTags,
                     String jpPackage, String[] authRoles) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
      }
      Collection<String> jpClassTagList = new HashSet<>();
      if (jpClassTags != null) {
        Collections.addAll(jpClassTagList, jpClassTags);
      }
      this.modeCode = JPUtil.Mode.CHECK_MODE;
      this.util = util;
      this.utilCode = util.getUrl();
      this.methodName = null;
      this.allInClasses = null;
      this.resultType = null;
      this.confirmMessage = null;
      this.uni = uni;
      this.jpClasses = Collections.unmodifiableCollection(jpClassList);
      this.jpClassTags = Collections.unmodifiableCollection(jpClassTagList);
      this.type = JPAppendType.CUSTOM;
      this.jpAttrs = Collections.emptyList();
      this.title = null;
      this.qName = null;
      this.inParams = Collections.emptyList();
      this.actionLogIgnoreParams = Collections.emptyList();
      this.inParamsDefValues = false;
      this.outCustomParams = Collections.emptyList();
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = authRoles;
      this.paramInClass = null;
      this.actionLog = true;
      this.infoMessage = null;
      this.validate = false;
      this.properties = null;
    }

    private ModeInfo(JPUtil util, String methodName, Class<?>[] inClasses, String resultType,
                     boolean uni, String[] jpClasses, String[] jpClassTags,
                     String jpPackage, String[] authRoles, JPUtilModeLink anno) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
      }
      Collection<String> jpClassTagList = new HashSet<>();
      if (jpClassTags != null) {
        Collections.addAll(jpClassTagList, jpClassTags);
      }
      this.modeCode = anno.code();
      this.util = util;
      this.utilCode = util.getUrl();
      this.methodName = methodName;
      this.allInClasses = inClasses;
      this.resultType = resultType;
      this.confirmMessage = !anno.confirm().isEmpty() ? anno.confirm() : null;
      this.uni = uni;
      this.jpClasses = Collections.unmodifiableCollection(jpClassList);
      this.jpClassTags = Collections.unmodifiableCollection(jpClassTagList);
      this.type = anno.type();
      this.jpAttrs = Stream.of(anno.jpAttrs())
          .map(this::toJPClassAttr).toList();
      this.title = anno.title();
      this.qName = anno.qName();
      this.actionLog = anno.actionLog();
      this.inParams = Stream.of(anno.inParams())
          .map(this::toJPUtilParam).toList();
      this.actionLogIgnoreParams = this.inParams.stream()
          .filter(param -> !param.isActionLog())
          .map(JPParamBase::getCode)
          .toList();
      this.inParamsDefValues = anno.inParamsDefValues();
      this.outCustomParams = Stream.of(anno.outCustomParams())
          .map(this::toJPUtilParam).toList();
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = authRoles;
      this.infoMessage = StringUtils.hasText(anno.infoMessage()) ? anno.infoMessage() : null;
      this.validate = anno.validate();

      Class<?> paramInClass = null;
      if (allInClasses != null) {
        for (Class<?> cls : this.allInClasses) {
          if (JPUtilInParams.class.isAssignableFrom(cls)) {
            paramInClass = cls;
          }
        }
      }
      this.paramInClass = paramInClass;

      JPUtilProperties properties = anno.properties();
      this.properties = JPUtilModeProperties.of(properties);
    }

    private JPUtilParam toJPUtilParam(JPParam param) {
      String code = param.code();
      JPType type = param.type();
      String qName = param.qName();

      JPStringFormat stringFormat = param.stringFormat();
      String stringMask = param.stringMask();

      JPMoney money = param.money();

      return JPUtilParam.newBuilder()
          .code(code)
          .type(type)
          .stringFormat(stringFormat != JPStringFormat.NONE ? stringFormat : null)
          .stringMask(stringMask != null && !stringMask.isBlank() ? stringMask : null)
          .fileTypes(Stream.of(param.fileTypes())
              .collect(Collectors.toList())
          )
          .length(param.length())
          .mandatory(param.mandatory())
          .description(param.description())
          .qName(qName != null && !qName.isBlank() ? qName : null)
          .multiple(param.multiple())
          .refJpClass(param.refJpClass())
          .refJpAttr(param.refJpAttr())
          .refFilter(param.refFilter())
          // Настройка денежного типа
          .money(
              type != JPType.MONEY ? null : JPMoneyBean.of(money.currency())
          )
          .external(true)
          .enums(Stream.of(param.enums())
              .map(this::toJPEnum)
              .collect(Collectors.toList())
          )
          .clientSearch(param.clientSearch())
          .actionLog(param.actionLog())
          .readOnly(param.readOnly())
          .build();
    }

    private mp.jprime.common.JPEnum toJPEnum(JPEnum paramEnum) {
      return mp.jprime.common.JPEnum.of(paramEnum.value(), paramEnum.description(), paramEnum.qName());
    }

    private JPClassAttr toJPClassAttr(mp.jprime.common.annotations.JPClassAttr classAttr) {
      return JPClassAttrBean.newBuilder()
          .jpClass(classAttr.jpClass())
          .jpAttr(classAttr.jpAttr())
          .build();
    }

    @Override
    public String getUtilCode() {
      return utilCode;
    }

    @Override
    public String getModeCode() {
      return modeCode;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public String getQName() {
      return qName;
    }

    @Override
    public boolean isActionLog() {
      return actionLog;
    }

    @Override
    public String getConfirmMessage() {
      return confirmMessage;
    }

    @Override
    public boolean isUni() {
      return uni;
    }

    @Override
    public Collection<String> getJpClasses() {
      return jpClasses;
    }

    @Override
    public Collection<String> getJpClassTags() {
      return jpClassTags;
    }

    @Override
    public JPAppendType getType() {
      return type;
    }

    @Override
    public Collection<JPClassAttr> getJpAttrs() {
      return jpAttrs;
    }

    @Override
    public Class<?> getInClass() {
      return paramInClass;
    }

    @Override
    public Collection<JPUtilParam> getInParams() {
      return inParams;
    }

    @Override
    public boolean isInParamsDefValues() {
      return inParamsDefValues;
    }

    @Override
    public Collection<JPUtilParam> getOutCustomParams() {
      return outCustomParams;
    }

    @Override
    public String getResultType() {
      return resultType;
    }

    private String getJpPackage() {
      return jpPackage;
    }

    private String[] getAuthRoles() {
      return authRoles;
    }

    @Override
    public Properties getProperties() {
      return properties;
    }

    public String getInfoMessage() {
      return infoMessage;
    }

    public boolean isValidate() {
      return validate;
    }
  }
}
