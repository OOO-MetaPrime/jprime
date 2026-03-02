package mp.jprime.utils.beans;

import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPClassAttr;
import mp.jprime.common.JPParam;
import mp.jprime.utils.JPUtilModeSettings;
import mp.jprime.utils.annotations.JPUtilProperties;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация настроек шага утилиты
 */
public final class JPUtilModeSettingsBean implements JPUtilModeSettings {
  private final String code;
  private final String title;
  private final String qName;
  private final Class<?> paramInClass;
  private final String jpPackage;
  private final String[] authRoles;
  private final boolean useActionLog;
  private final JPAppendType type;
  private final Collection<JPClassAttr> jpAttrs;
  private final String confirm;
  private final String infoMessage;
  private final Collection<JPParam> inParams;
  private final boolean useDynamicParams;
  private final boolean useInParamsDefValues;
  private final JPUtilProperties properties;
  private final String resultType;
  private final Collection<JPParam> outCustomParams;
  private final boolean useValidate;

  private JPUtilModeSettingsBean(String code, String title, String qName,
                                 Class<?> paramInClass,
                                 String jpPackage, String[] authRoles,
                                 boolean useActionLog, JPAppendType type, Collection<JPClassAttr> jpAttrs,
                                 String confirm, String infoMessage,
                                 Collection<JPParam> inParams,
                                 boolean useDynamicParams,
                                 boolean useInParamsDefValues,
                                 JPUtilProperties properties, String resultType,
                                 Collection<JPParam> outCustomParams,
                                 boolean useValidate) {
    this.code = code;
    this.title = title;
    this.qName = qName;
    this.paramInClass = paramInClass;
    this.jpPackage = jpPackage;
    this.authRoles = authRoles;
    this.useActionLog = useActionLog;
    this.type = type;
    this.jpAttrs = jpAttrs != null ? Collections.unmodifiableCollection(jpAttrs) : Collections.emptyList();
    this.confirm = confirm;
    this.infoMessage = infoMessage;
    this.inParams = inParams != null ? Collections.unmodifiableCollection(inParams) : Collections.emptyList();
    this.useDynamicParams = useDynamicParams;
    this.useInParamsDefValues = useInParamsDefValues;
    this.properties = properties;
    this.resultType = resultType;
    this.outCustomParams = outCustomParams != null ? Collections.unmodifiableCollection(outCustomParams) : Collections.emptyList();
    this.useValidate = useValidate;
  }

  public static JPUtilModeSettings of(String code, String title, String qName,
                                      Class<?> paramInClass,
                                      String jpPackage, String[] authRoles,
                                      boolean useActionLog, JPAppendType type, Collection<JPClassAttr> jpAttrs,
                                      String confirm, String infoMessage,
                                      Collection<JPParam> inParams,
                                      boolean useDynamicParams,
                                      boolean useInParamsDefValues,
                                      JPUtilProperties properties, String resultType,
                                      Collection<JPParam> outCustomParams,
                                      boolean useValidate) {
    return new JPUtilModeSettingsBean(code, title, qName, paramInClass,
        jpPackage, authRoles, useActionLog, type, jpAttrs, confirm, infoMessage,
        inParams, useDynamicParams, useInParamsDefValues, properties, resultType, outCustomParams, useValidate);
  }


  @Override
  public String getCode() {
    return code;
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
  public Class<?> getParamInClass() {
    return paramInClass;
  }

  @Override
  public String getJpPackage() {
    return jpPackage;
  }

  @Override
  public String[] getAuthRoles() {
    return authRoles;
  }

  @Override
  public boolean useActionLog() {
    return useActionLog;
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
  public String getConfirm() {
    return confirm;
  }

  @Override
  public String getInfoMessage() {
    return infoMessage;
  }

  @Override
  public Collection<JPParam> getInParams() {
    return inParams;
  }

  @Override
  public JPUtilProperties getProperties() {
    return properties;
  }

  @Override
  public String getResultType() {
    return resultType;
  }

  @Override
  public Collection<JPParam> getOutCustomParams() {
    return outCustomParams;
  }

  @Override
  public boolean useInParamsDefValues() {
    return useInParamsDefValues;
  }

  @Override
  public boolean useDynamicParams() {
    return useDynamicParams;
  }

  @Override
  public boolean useValidate() {
    return useValidate;
  }
}
