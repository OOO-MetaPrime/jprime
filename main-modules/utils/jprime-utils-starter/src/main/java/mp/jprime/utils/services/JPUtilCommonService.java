package mp.jprime.utils.services;

import com.fasterxml.jackson.core.JsonParseException;
import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPBadFormatException;
import mp.jprime.exceptions.JPQueryException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.log.AppLogger;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.utils.*;
import mp.jprime.utils.annotations.JPUtilProperties;
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

import java.util.*;
import java.util.stream.Stream;

/**
 * Сервис работы с утилитами
 */
@Service
public final class JPUtilCommonService implements JPUtilService {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilCommonService.class);

  private static final Collection<String> SYSTEM_MODE_LIST = Set.of(
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
  private JPUtilCommonService(@Autowired(required = false) Collection<JPUtilLoader> loaders) {
    if (loaders == null || loaders.isEmpty()) {
      return;
    }

    Collection<JPUtilSettings> utils = new ArrayList<>();
    for (JPUtilLoader loader : loaders) {
      Collection<JPUtilSettings> list = loader.getUtils();
      if (list == null) {
        continue;
      }
      utils.addAll(list);
    }

    if (utils.isEmpty()) {
      return;
    }

    for (JPUtilSettings util : utils) {
      try {
        JPUtilAction jpAction = util.getAction();
        String code = util.getCode();
        if (jpAction == null || code == null) {
          continue;
        }
        UtilInfo utilInfo = new UtilInfo(util);

        boolean isUni = util.isUni();
        String[] utilJpClasses = util.getJpClasses();
        String[] utilJpClassTags = util.getJpClassTags();
        String[] utilJpUtilTags = util.getJpUtilTags();

        for (JPUtilModeSettings mode : util.getModeList()) {
          String resultType = mode.getResultType();

          ModeInfo info = new ModeInfo(
              util,
              resultType,
              isUni,
              utilJpClasses,
              utilJpClassTags,
              utilJpUtilTags,
              mode
          );
          utilInfo.modes.putIfAbsent(mode.getCode(), info);
        }
        // Добавляем болванку шага check
        utilInfo.modes.putIfAbsent(
            JPUtil.Mode.CHECK_MODE,
            new ModeInfo(
                util,
                isUni,
                utilJpClasses,
                utilJpClassTags,
                utilJpUtilTags,
                util.getJpPackage(),
                util.getAuthRoles(),
                JPUtilCheckOutParams.CODE
            )
        );
        // Добавляем болванку шага inParamsDefValues
        utilInfo.modes.putIfAbsent(
            JPUtil.Mode.IN_PARAMS_DEF_VALUES,
            new ModeInfo(
                util,
                isUni,
                utilJpClasses,
                utilJpClassTags,
                utilJpUtilTags,
                util.getJpPackage(),
                util.getAuthRoles(),
                JPUtilDefValuesOutParams.CODE
            )
        );

        if (isUni) {
          jpUniUtils.put(code, utilInfo);
        } else {
          jpUtils.put(code, utilInfo);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
  }

  @Override
  public Collection<String> getUtilCodes() {
    return jpUtils.keySet();
  }

  @Override
  public Collection<String> getUniUtilCodes() {
    return jpUniUtils.keySet();
  }

  @Override
  public JPUtilSettings getUtil(String utilCode) {
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
        .filter(x -> !SYSTEM_MODE_LIST.contains(x.getModeCode()));
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
        .filter(x -> !SYSTEM_MODE_LIST.contains(x.getModeCode()));
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
        .filter(x -> checkAccess(x, auth));
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
  private boolean checkAccess(ModeInfo mode, AuthInfo auth) {
    if (mode == null) {
      return true;
    }
    String[] methodRoles = mode.getAuthRoles();
    if (methodRoles != null && methodRoles.length > 0) {
      if (auth == null || Collections.disjoint(auth.getRoles(), Arrays.asList(methodRoles))) {
        return false;
      }
    }
    boolean result = true;
    String jpPackage = mode.getJpPackage();
    if (jpPackage != null) {
      result = securityManager.checkRead(jpPackage, auth.getRoles());
    }
    return result && mode.checkAccess(auth);
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
        .filter(x -> checkAccess(x, auth))
        .switchIfEmpty(Mono.error(new JPUtilRunRightException(utilCode)));
  }

  @Override
  public Mono<JPUtilOutParams> apply(final JPUtilMode mode, JPUtilInParams in, ServerWebExchange swe, AuthInfo auth) {
    return JPMono.fromCallable(() -> mode)
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException("")))
        .flatMap(x -> apply(x.getUtilCode(), x.getModeCode(), auth))
        .flatMap(x -> {
              JPUtilSettings util = x.util;
              String utilCode = x.utilCode;
              String modeCode = x.modeCode;

              Collection<String> actionLogIgnoreParams = x.actionLogIgnoreParams;
              Class<?> paramInClass = x.paramInClass;
              String resultType = x.resultType;

              if (paramInClass == null || resultType == null) {
                return Mono.error(new JPUtilModeNotFoundException(utilCode, modeCode));
              }

              String subject = auth.getUsername();
              try {
                if (x.isActionLog() && !SYSTEM_MODE_LIST.contains(modeCode)) { // не логируем типовые режимы утилит
                  String s = jpJsonMapper.toString(in, actionLogIgnoreParams);
                  appLogger.debug(Event.UTIL_RUN, subject, utilCode + "/mode/" + modeCode, s, auth);
                }

                JPUtilAction jpAction = util.getAction();
                return jpAction.exec(modeCode, in, swe, auth);
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
    private final JPUtilSettings util;
    private final Map<String, ModeInfo> modes = new HashMap<>();

    private UtilInfo(JPUtilSettings util) {
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
    private final JPUtilSettings util;
    private final String utilCode;
    private final String modeCode;
    private final String title;
    private final String qName;
    private final String jpPackage;
    private final String[] authRoles;
    private final String confirmMessage;
    private final boolean uni;
    private final Collection<String> jpClasses;
    private final Collection<String> jpClassTags;
    private final Collection<String> jpUtilTags;
    private final JPAppendType type;
    private final Collection<JPClassAttr> jpAttrs;
    private final Class<?> paramInClass;
    private final Collection<mp.jprime.common.JPParam> inParams;
    private final Collection<String> actionLogIgnoreParams;
    private final boolean inParamsDefValues;
    private final boolean useDynamicParams;
    private final String resultType;
    private final Collection<mp.jprime.common.JPParam> outCustomParams;
    private final boolean actionLog;
    private final String infoMessage;
    private final boolean validate;
    private final Properties properties;

    private ModeInfo(JPUtilSettings util, boolean uni, String[] jpClasses, String[] jpClassTags,
                     String[] jpUtilTags, String jpPackage, String[] authRoles, String resultType) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
      }
      Collection<String> jpClassTagList = new HashSet<>();
      if (jpClassTags != null) {
        Collections.addAll(jpClassTagList, jpClassTags);
      }
      Collection<String> jpUtilTagList = new HashSet<>();
      if (jpUtilTags != null) {
        Collections.addAll(jpUtilTagList, jpUtilTags);
      }
      this.modeCode = JPUtil.Mode.CHECK_MODE;
      this.util = util;
      this.utilCode = util.getCode();
      this.resultType = resultType;
      this.confirmMessage = null;
      this.uni = uni;
      this.jpClasses = Collections.unmodifiableCollection(jpClassList);
      this.jpClassTags = Collections.unmodifiableCollection(jpClassTagList);
      this.jpUtilTags = Collections.unmodifiableCollection(jpUtilTagList);
      this.type = JPAppendType.CUSTOM;
      this.jpAttrs = Collections.emptyList();
      this.title = null;
      this.qName = null;
      this.inParams = Collections.emptyList();
      this.inParamsDefValues = false;
      this.useDynamicParams = false;
      this.actionLogIgnoreParams = Collections.emptyList();
      this.outCustomParams = Collections.emptyList();
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = authRoles;
      this.paramInClass = DefaultInParams.class;
      this.actionLog = true;
      this.infoMessage = null;
      this.validate = false;
      this.properties = null;
    }

    private ModeInfo(JPUtilSettings util, String resultType,
                     boolean uni, String[] jpClasses, String[] jpClassTags, String[] jpUtilTags,
                     JPUtilModeSettings mode) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
      }
      Collection<String> jpClassTagList = new HashSet<>();
      if (jpClassTags != null) {
        Collections.addAll(jpClassTagList, jpClassTags);
      }
      Collection<String> jpUtilTagList = new HashSet<>();
      if (jpUtilTags != null) {
        Collections.addAll(jpUtilTagList, jpUtilTags);
      }
      this.modeCode = mode.getCode();
      this.util = util;
      this.utilCode = util.getCode();
      this.resultType = resultType;

      String confirm = mode.getConfirm();
      this.confirmMessage = StringUtils.hasText(confirm) ? confirm : null;
      this.uni = uni;
      this.jpClasses = Collections.unmodifiableCollection(jpClassList);
      this.jpClassTags = Collections.unmodifiableCollection(jpClassTagList);
      this.jpUtilTags = Collections.unmodifiableCollection(jpUtilTagList);
      this.type = mode.getType();

      this.jpAttrs = mode.getJpAttrs();
      this.title = mode.getTitle();
      this.qName = mode.getQName();
      this.actionLog = mode.useActionLog();
      this.inParams = mode.getInParams();
      this.useDynamicParams = mode.useDynamicParams();
      this.inParamsDefValues = mode.useInParamsDefValues();
      this.actionLogIgnoreParams = this.inParams.stream()
          .filter(param -> !param.isActionLog())
          .map(mp.jprime.common.JPParam::getCode)
          .toList();

      this.outCustomParams = mode.getOutCustomParams();

      String jpPackage = mode.getJpPackage();
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = mode.getAuthRoles();

      String infoMessage = mode.getInfoMessage();
      this.infoMessage = StringUtils.hasText(infoMessage) ? infoMessage : null;
      this.validate = mode.useValidate();

      this.paramInClass = mode.getParamInClass();

      JPUtilProperties properties = mode.getProperties();
      this.properties = JPUtilModeProperties.of(properties);
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
    public boolean checkAccess(AuthInfo auth) {
      return util.getAction().checkAccess(auth);
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
    public Collection<String> getJpUtilTags() {
      return jpUtilTags;
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
    public Collection<mp.jprime.common.JPParam> getInParams() {
      return inParams;
    }

    @Override
    public boolean isUseDynamicParams() {
      return useDynamicParams;
    }

    @Override
    public boolean isInParamsDefValues() {
      return inParamsDefValues;
    }

    @Override
    public Collection<mp.jprime.common.JPParam> getOutCustomParams() {
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
