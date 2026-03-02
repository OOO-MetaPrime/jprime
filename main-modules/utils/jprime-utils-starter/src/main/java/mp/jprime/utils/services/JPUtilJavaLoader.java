package mp.jprime.utils.services;

import com.fasterxml.jackson.core.JsonParseException;
import mp.jprime.common.annotations.JPClassAttr;
import mp.jprime.common.annotations.JPEnum;
import mp.jprime.common.annotations.JPParam;
import mp.jprime.common.beans.JPClassAttrBean;
import mp.jprime.common.beans.JPCommonParam;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPBadFormatException;
import mp.jprime.exceptions.JPQueryException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.JPIntegerFormat;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.annotations.JPMoney;
import mp.jprime.meta.beans.JPMoneyBean;
import mp.jprime.meta.beans.JPType;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.*;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import mp.jprime.utils.annotations.JPUtilResultType;
import mp.jprime.utils.beans.JPUtilModeSettingsBean;
import mp.jprime.utils.beans.JPUtilSettingsBean;
import mp.jprime.utils.exceptions.JPUtilModeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Основная загрузка утилит, описанных через java
 */
@Service
public class JPUtilJavaLoader implements JPUtilLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilJavaLoader.class);

  private Collection<JPUtilSettings> utils;

  @Autowired(required = false)
  private void setUtils(Collection<JPUtil> utils) {
    if (utils != null) {
      Collection<JPUtilSettings> result = new ArrayList<>(utils.size());
      for (JPUtil util : utils) {
        JPUtilLink utilAnno = util.getClass().getAnnotation(JPUtilLink.class);
        if (utilAnno == null) {
          continue;
        }
        Map<String, String> methodNameByCode = new HashMap<>();
        Map<String, Class<?>[]> parameterTypeByCode = new HashMap<>();
        Map<String, Class<?>> paramInClassByCode = new HashMap<>();

        Collection<JPUtilModeSettings> modeList = new ArrayList<>();
        for (Method method : util.getClass().getMethods()) {
          JPUtilModeLink modeAnno = method.getAnnotation(JPUtilModeLink.class);
          if (modeAnno == null) {
            continue;
          }

          String modeCode = modeAnno.code();
          String methodName = method.getName();

          if (methodNameByCode.containsKey(modeCode)) {
            continue;
          }

          Class<?> returnType = method.getReturnType();
          if (!Mono.class.isAssignableFrom(returnType)) {
            continue;
          }

          Class<?> outClass = modeAnno.outClass();
          if (!JPUtilOutParams.class.isAssignableFrom(outClass)) {
            continue;
          }

          JPUtilResultType a = outClass.getAnnotation(JPUtilResultType.class);
          String resultType = a != null ? a.code() : "";

          Class<?> paramInClass = null;
          Class<?>[] modeParamTypes = method.getParameterTypes();
          for (Class<?> cls : modeParamTypes) {
            if (JPUtilInParams.class.isAssignableFrom(cls)) {
              paramInClass = cls;
            }
          }

          methodNameByCode.put(modeCode, methodName);
          parameterTypeByCode.put(modeCode,  method.getParameterTypes());
          paramInClassByCode.put(modeCode, paramInClass);

          modeList.add(
              JPUtilModeSettingsBean.of(
                  modeCode,
                  modeAnno.title(),
                  modeAnno.qName(),
                  paramInClass,
                  StringUtils.hasText(modeAnno.jpPackage()) ? modeAnno.jpPackage() : util.getJpPackage(),
                  modeAnno.authRoles().length > 0 ? modeAnno.authRoles() : util.getAuthRoles(),
                  modeAnno.actionLog(),
                  modeAnno.type(),
                  Stream.of(modeAnno.jpAttrs())
                      .map(this::toJPClassAttr)
                      .toList(),
                  modeAnno.confirm(),
                  modeAnno.infoMessage(),
                  Stream.of(modeAnno.inParams())
                      .map(this::toJPUtilParam)
                      .toList(),
                  modeAnno.useDynamicParams(),
                  modeAnno.inParamsDefValues(),
                  modeAnno.properties(),
                  resultType,
                  Stream.of(modeAnno.outCustomParams())
                      .map(this::toJPUtilParam)
                      .toList(),
                  modeAnno.validate()
              )
          );
        }

        result.add(
            JPUtilSettingsBean.of(
                util.getCode(), util.getTitle(), util.getQName(),
                util.getJpPackage(), util.getAuthRoles(),
                util.isUni(), util.getJpClasses(),
                util.getJpClassTags(), util.getJpUtilTags(),
                toAction(util, methodNameByCode, parameterTypeByCode, paramInClassByCode), modeList
            )
        );
      }
      this.utils = result;
    }
  }

  private JPUtilAction toAction(JPUtil util,
                                Map<String, String> methodNameByCode,
                                Map<String, Class<?>[]> parameterTypeByCode,
                                Map<String, Class<?>> paramInClassByCode) {
    return new JPUtilAction() {
      @Override
      public boolean checkAccess(AuthInfo auth) {
        return util.checkAccess(auth);
      }

      @Override
      public <T> Mono<JPUtilOutParams<T>> exec(String mode, JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
        return invoke(mode, inParams, swe, auth);
      }

      private <T> Mono<JPUtilOutParams<T>> invoke(String mode,
                                                  JPUtilInParams inParams,
                                                  ServerWebExchange swe,
                                                  AuthInfo auth) {
        String methodName = methodNameByCode.get(mode);
        // По умолчанию метод check, если не переопределен отрабатывает корректно
        if (methodName == null && JPUtil.Mode.CHECK_MODE.equals(mode)) {
          return (Mono) Mono.just(JPUtilCheckOutParams.ALLOW);
        }
        if (methodName == null && JPUtil.Mode.IN_PARAMS_DEF_VALUES.equals(mode)) {
          return (Mono) Mono.just(JPUtilDefValuesOutParams.newBuilder().build());
        }

        Class<?>[] modeParamTypes = parameterTypeByCode.get(mode);
        Class<?> paramInClass = paramInClassByCode.get(mode);
        if (methodName == null || modeParamTypes == null || modeParamTypes.length == 0 || paramInClass == null) {
          return Mono.error(new JPUtilModeNotFoundException(util.getCode(), mode));
        }
        try {
          Object[] inValues = new Object[modeParamTypes.length];
          for (int i = 0; i < modeParamTypes.length; i++) {
            Class<?> cls = modeParamTypes[i];
            if (AuthInfo.class.isAssignableFrom(cls)) {
              inValues[i] = auth;
            } else if (ServerWebExchange.class.isAssignableFrom(cls)) {
              inValues[i] = swe;
            } else if (JPUtilInParams.class.isAssignableFrom(cls)) {
              inValues[i] = inParams;
            }
          }

          Method method = util.getClass().getMethod(methodName, modeParamTypes);
          return (Mono) method.invoke(util, inValues);
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
    };
  }

  @Override
  public Collection<JPUtilSettings> getUtils() {
    return utils != null ? utils : Collections.emptyList();
  }

  private mp.jprime.common.JPClassAttr toJPClassAttr(JPClassAttr classAttr) {
    return JPClassAttrBean.newBuilder()
        .jpClass(classAttr.jpClass())
        .jpAttr(classAttr.jpAttr())
        .build();
  }

  private mp.jprime.common.JPParam toJPUtilParam(JPParam param) {
    String code = param.code();
    JPType type = param.type();
    String qName = param.qName();

    JPStringFormat stringFormat = param.stringFormat();
    String stringMask = param.stringMask();
    JPIntegerFormat integerFormat = param.integerFormat();

    JPMoney money = param.money();

    return JPCommonParam.newBuilder()
        .code(code)
        .type(type)
        .stringFormat(stringFormat != JPStringFormat.NONE ? stringFormat : null)
        .stringMask(stringMask != null && !stringMask.isBlank() ? stringMask : null)
        .integerFormat(integerFormat != JPIntegerFormat.NONE ? integerFormat : null)
        .multiline(param.multiline())
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
}
