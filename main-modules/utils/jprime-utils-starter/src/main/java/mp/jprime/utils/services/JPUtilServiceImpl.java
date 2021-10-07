package mp.jprime.utils.services;

import com.fasterxml.jackson.core.JsonParseException;
import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;
import mp.jprime.common.JPClassAttrBean;
import mp.jprime.common.annotations.JPEnum;
import mp.jprime.common.annotations.JPParam;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPBadFormatException;
import mp.jprime.exceptions.JPQueryException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.log.AppLogger;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import mp.jprime.utils.*;
import mp.jprime.utils.annotations.JPUtilModeLink;
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
public final class JPUtilServiceImpl implements JPUtilService {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilServiceImpl.class);
  /**
   * Режим проверки доступности по умолчанию
   */
  public static final String CHECK_MODE = "check";

  private Map<String, UtilInfo> jpUtils = new HashMap<>();
  private Map<String, UtilInfo> jpUniUtils = new HashMap<>();
  /**
   * Системный журнал
   */
  private AppLogger appLogger;
  /**
   * Хранилище настроек RBAC
   */
  private JPSecurityStorage securityManager;

  private JPJsonMapper jpJsonMapper;

  @Autowired
  private void setJpJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  /**
   * Считываем аннотации
   */
  private JPUtilServiceImpl(@Autowired(required = false) Collection<JPUtil> utils) {
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
              !StringUtils.isEmpty(anno.jpPackage()) ? anno.jpPackage() : util.getJpPackage(),
              anno.authRoles().length > 0 ? anno.authRoles() : util.getAuthRoles(),
              anno
          );
          utilInfo.modes.put(anno.code(), info);
        }
        // Добавляем болванку шага check
        utilInfo.modes.putIfAbsent(CHECK_MODE, new ModeInfo(util, isUni, utilJpClasses, util.getJpPackage(), util.getAuthRoles()));

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

  @Autowired
  private void setAppLogger(AppLogger appLogger) {
    this.appLogger = appLogger;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
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

  /**
   * Возвращает утилиту по ее коду
   *
   * @param utilCode Код утилиты
   * @return Утилита
   */
  @Override
  public JPUtil getUtil(String utilCode) {
    UtilInfo info = jpUtils.get(utilCode);
    if (info == null) {
      info = jpUniUtils.get(utilCode);
    }
    return info != null ? info.util : null;
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
        .filter(x -> !CHECK_MODE.equals(x.getModeCode()));
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
        .filter(x -> !CHECK_MODE.equals(x.getModeCode()));
  }

  /**
   * Возвращает все утилиты
   *
   * @return Утилиты
   */
  @Override
  public Flux<JPUtilMode> getUtils() {
    return Flux.fromStream(utilsStream());
  }

  /**
   * Возвращает все утилиты, доступные данной авторизации
   *
   * @param authInfo Данные авторизации
   * @return Утилиты
   */
  @Override
  public Flux<JPUtilMode> getUtils(AuthInfo authInfo) {
    return Flux.fromStream(
        utilsStream()
            .filter(x -> checkSecurity(x, authInfo))
    );
  }

  /**
   * Возвращает все утилиты для указанного класса, доступные данной авторизации
   *
   * @param className Код метакласса
   * @param authInfo  Данные авторизации
   * @return Утилиты
   */
  @Override
  public Flux<JPUtilMode> getUtils(String className, AuthInfo authInfo) {
    return getUtils(authInfo)
        .filter(x -> x.isUni() || x.getJpClasses().contains(className));
  }

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param in         Входящие параметры
   * @param swe        ServerWebExchange
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  @Override
  public Mono<JPUtilOutParams> apply(String utilCode, String methodCode, JPUtilInParams in, ServerWebExchange swe, AuthInfo authInfo) {
    return apply(utilCode, methodCode, authInfo)
        .flatMap(x -> apply(x, in, swe, authInfo));
  }

  /**
   * Проверка доступа
   *
   * @param mode     Шаг утилиты
   * @param authInfo Доступ
   * @return Да/Нет
   */
  private boolean checkSecurity(ModeInfo mode, AuthInfo authInfo) {
    if (mode == null) {
      return true;
    }
    String[] methodRoles = mode.getAuthRoles();
    if (methodRoles != null && methodRoles.length > 0) {
      if (authInfo == null || Collections.disjoint(authInfo.getRoles(), Arrays.asList(methodRoles))) {
        return false;
      }
    }
    String jpPackage = mode.getJpPackage();
    if (jpPackage != null) {
      return securityManager.checkRead(jpPackage, authInfo.getRoles());
    }
    return true;
  }

  /**
   * Выполняет метод утилиты
   *
   * @param utilCode   Код утилиты
   * @param methodCode Код метода
   * @param authInfo   Данные авторизации
   * @return Исходящие параметры
   */
  @Override
  public Mono<ModeInfo> apply(String utilCode, String methodCode, AuthInfo authInfo) {
    return Mono.justOrEmpty(jpUtils.get(utilCode))
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
        .filter(x -> checkSecurity(x, authInfo))
        .switchIfEmpty(Mono.error(new JPUtilRunRightException(utilCode)));
  }

  /**
   * Выполняет метод утилиты
   *
   * @param mode     Шаг утилиты
   * @param in       Входящие параметры
   * @param swe      ServerWebExchange
   * @param authInfo Данные авторизации
   * @return Исходящие параметры
   */
  @Override
  public Mono<JPUtilOutParams> apply(final JPUtilMode mode, JPUtilInParams in, ServerWebExchange swe, AuthInfo authInfo) {
    return Mono.just(mode)
        .switchIfEmpty(Mono.error(new JPUtilNotFoundException("")))
        .flatMap(x -> apply(x.getUtilCode(), x.getModeCode(), authInfo))
        .flatMap(x -> {
              JPUtil util = x.util;
              String utilCode = x.utilCode;
              String modeCode = x.modeCode;
              String methodName = x.methodName;
              Class[] inClasses = x.allInClasses;
              String resultType = x.resultType;

              // По умолчанию метод check, если не переопределен отрабатывает корректно
              if (methodName == null && CHECK_MODE.equals(modeCode)) {
                return Mono.just(JPUtilMessageOutParams.newBuilder().build());
              }
              if (methodName == null || inClasses == null || inClasses.length == 0 || resultType == null) {
                return Mono.error(new JPUtilModeNotFoundException(utilCode, modeCode));
              }
              String subject = authInfo.getUsername();
              try {
                String s = jpJsonMapper.getObjectMapper().writeValueAsString(in);
                if (!CHECK_MODE.equals(modeCode) && x.isActionLog()) { // не логируем чеки
                  appLogger.debug(Event.UTIL_RUN, subject, utilCode + "/mode/" + modeCode, s, authInfo);
                }

                Object[] inParams = new Object[inClasses.length];
                for (int i = 0; i < inClasses.length; i++) {
                  Class cls = inClasses[i];
                  if (AuthInfo.class.isAssignableFrom(cls)) {
                    inParams[i] = authInfo;
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
  private class UtilInfo {
    private final JPUtil util;
    private final Map<String, ModeInfo> modes = new HashMap<>();

    private UtilInfo(JPUtil util) {
      this.util = util;
    }
  }

  /**
   * Описание шагов
   */
  private class ModeInfo implements JPUtilMode {
    private final JPUtil util;
    private final String utilCode;
    private final String modeCode;
    private final String title;
    private final String qName;
    private final String methodName;
    private final Class[] allInClasses;
    private final String jpPackage;
    private final String[] authRoles;
    private final String confirmMessage;
    private final boolean uni;
    private final Collection<String> jpClasses;
    private final JPAppendType type;
    private final Collection<JPClassAttr> jpAttrs;
    private final Class paramInClass;
    private final Collection<JPUtilParam> inParams;
    private final String resultType;
    private final Collection<JPUtilParam> outCustomParams;
    private final boolean actionLog;

    private ModeInfo(JPUtil util, boolean uni, String[] jpClasses, String jpPackage, String[] authRoles) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
      }
      this.modeCode = CHECK_MODE;
      this.util = util;
      this.utilCode = util.getUrl();
      this.methodName = null;
      this.allInClasses = null;
      this.resultType = null;
      this.confirmMessage = null;
      this.uni = uni;
      this.jpClasses = Collections.unmodifiableCollection(jpClassList);
      this.type = JPAppendType.CUSTOM;
      this.jpAttrs = Collections.emptyList();
      this.title = null;
      this.qName = null;
      this.inParams = Collections.emptyList();
      this.outCustomParams = Collections.emptyList();
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = authRoles;
      this.paramInClass = null;
      this.actionLog = true;
    }

    private ModeInfo(JPUtil util, String methodName, Class[] inClasses, String resultType,
                     boolean uni, String[] jpClasses, String jpPackage, String[] authRoles, JPUtilModeLink anno) {
      Collection<String> jpClassList = new HashSet<>();
      if (jpClasses != null) {
        Collections.addAll(jpClassList, jpClasses);
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
      this.type = anno.type();
      this.jpAttrs = Collections.unmodifiableCollection(
          Stream.of(anno.jpAttrs())
              .map(this::toJPClassAttr)
              .collect(Collectors.toList())
      );
      this.title = anno.title();
      this.qName = anno.qName();
      this.actionLog = anno.actionLog();
      this.inParams = Collections.unmodifiableCollection(
          Stream.of(anno.inParams())
              .map(this::toJPUtilParam)
              .collect(Collectors.toList())
      );
      this.outCustomParams = Collections.unmodifiableCollection(
          Stream.of(anno.outCustomParams())
              .map(this::toJPUtilParam)
              .collect(Collectors.toList())
      );
      this.jpPackage = StringUtils.hasText(jpPackage) ? jpPackage : null;
      this.authRoles = authRoles;

      Class paramInClass = null;
      if (allInClasses != null) {
        for (Class cls : this.allInClasses) {
          if (JPUtilInParams.class.isAssignableFrom(cls)) {
            paramInClass = cls;
          }
        }
      }
      this.paramInClass = paramInClass;
    }

    private JPUtilParam toJPUtilParam(JPParam param) {
      return JPUtilParam.newBuilder()
          .code(param.code())
          .type(param.type())
          .length(param.length())
          .mandatory(param.mandatory())
          .description(param.description())
          .qName(param.qName())
          .multiple(param.multiple())
          .refJpClass(param.refJpClass())
          .refJpAttr(param.refJpAttr())
          .refFilter(param.refFilter())
          .external(true)
          .enums(Stream.of(param.enums())
              .map(this::toJPUtilParam)
              .collect(Collectors.toList())
          )
          .build();
    }

    private JPUtilEnum toJPUtilParam(JPEnum paramEnum) {
      return JPUtilEnum.newBuilder()
          .description(paramEnum.description())
          .qName(paramEnum.qName())
          .value(paramEnum.value())
          .build();
    }

    private JPClassAttr toJPClassAttr(mp.jprime.common.annotations.JPClassAttr classAttr) {
      return JPClassAttrBean.newBuilder()
          .jpClass(classAttr.jpClass())
          .jpAttr(classAttr.jpAttr())
          .build();
    }

    /**
     * Кодовое имя утилиты
     *
     * @return Кодовое имя утилиты
     */
    @Override
    public String getUtilCode() {
      return utilCode;
    }

    /**
     * Кодовое имя шага
     *
     * @return Кодовое имя шага
     */
    @Override
    public String getModeCode() {
      return modeCode;
    }

    /**
     * Возвращает название шага утилиты
     *
     * @return название шага утилиты
     */
    @Override
    public String getTitle() {
      return title;
    }

    /**
     * Возвращает QName шага утилиты
     *
     * @return QName шага утилиты
     */
    @Override
    public String getQName() {
      return qName;
    }

    /**
     * Возвращает  Признак логирования действий
     *
     * @return Да/Нет
     */
    @Override
    public boolean isActionLog() {
      return actionLog;
    }

    /**
     * Сообщение для подтверждения
     *
     * @return Сообщение для подтверждения
     */
    @Override
    public String getConfirmMessage() {
      return confirmMessage;
    }

    /**
     * Признак универсального (для всех классов) шага
     *
     * @return Да/Нет
     */
    @Override
    public boolean isUni() {
      return uni;
    }

    /**
     * Классы, обрабатываемые этой утилитой
     *
     * @return Классы, обрабатываемые этой утилитой
     */
    @Override
    public Collection<String> getJpClasses() {
      return jpClasses;
    }

    /**
     * Возвращает тип утилиты
     *
     * @return Тип утилиты
     */
    @Override
    public JPAppendType getType() {
      return type;
    }

    /**
     * Атрибуты, обрабатываемые этой утилитой
     *
     * @return Атрибуты, обрабатываемые этой утилитой
     */
    @Override
    public Collection<JPClassAttr> getJpAttrs() {
      return jpAttrs;
    }

    /**
     * Класс входящих параметров
     *
     * @return Входящие параметры
     */
    @Override
    public Class getInClass() {
      return paramInClass;
    }

    /**
     * Описание входных параметров
     *
     * @return Описание входных параметров
     */
    @Override
    public Collection<JPUtilParam> getInParams() {
      return inParams;
    }

    /**
     * Описание итоговых параметров
     *
     * @return Описание итоговых параметров
     */
    @Override
    public Collection<JPUtilParam> getOutCustomParams() {
      return outCustomParams;
    }

    /**
     * Тип итогового результата
     *
     * @return Тип итогового результата
     */
    @Override
    public String getResultType() {
      return resultType;
    }

    /**
     * Настройки доступа
     *
     * @return Настройки доступа
     */
    private String getJpPackage() {
      return jpPackage;
    }

    /**
     * Роли, имеющиеся доступ к этой утилите
     *
     * @return Роли, имеющиеся доступ к этой утилите
     */
    private String[] getAuthRoles() {
      return authRoles;
    }
  }
}
