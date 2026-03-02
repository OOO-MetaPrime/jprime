package mp.jprime.utils.loaders.xml;

import mp.jprime.application.JPApplicationShutdownManager;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
import mp.jprime.dataaccess.transaction.ChainedTransactionManager;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.json.JPJsonUtils;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.*;
import mp.jprime.utils.exceptions.JPUtilModeNotFoundException;
import mp.jprime.utils.loaders.xml.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Типовая реализация утилиты на основе xml описания
 */
public final class JPUtilXmlAction extends JPUtiBaseAction {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilXmlAction.class);

  private final String utilCode;
  private final Collection<JPUtilXmlOperation.Executor> checkExecutors;
  private final Map<String, String> defValues;
  private final Map<String, Mode> modes;

  private final ChainedTransactionManager transactionManager;
  private final JPTemplateValueService templateValueService;

  private JPUtilXmlAction(String utilCode,
                          XmlJpCheck jpCheck,
                          XmlJpDefValues jpDefValues,
                          Map<XmlJpMode, Collection<String>> jpModeMap,
                          Map<String, JPUtilXmlOperation> operations,
                          ChainedTransactionManager transactionManager,
                          JPApplicationShutdownManager shutdownManager,
                          JPTemplateValueService templateValueService) {
    this.utilCode = utilCode;

    Collection<JPUtilXmlOperation.Executor> checkExecutors = new ArrayList<>();
    XmlJpOperation[] xmlCheckOperations = jpCheck != null ? jpCheck.getOperation() : null;
    if (xmlCheckOperations != null) {
      for (XmlJpOperation xml : xmlCheckOperations) {
        checkExecutors.add(toOperation(xml, operations, shutdownManager));
      }
    }
    this.checkExecutors = checkExecutors;

    Map<String, String> defValues = new HashMap<>();
    XmlJpDefValuesParam[] defValuesParams = jpDefValues != null ? jpDefValues.getParam() : null;
    if (defValuesParams != null) {
      for (XmlJpDefValuesParam param : defValuesParams) {
        defValues.put(param.getCode(), param.getValue());
      }
    }
    this.defValues = defValues;

    Map<String, Mode> modes = new HashMap<>();
    for (var entry : jpModeMap.entrySet()) {
      XmlJpMode jpMode = entry.getKey();
      Collection<String> params = entry.getValue();

      String modeCode = jpMode.getCode();
      XmlJpOperations modeOperations = jpMode.getOperations();

      Collection<JPUtilXmlOperation.Executor> modeExecutors = new ArrayList<>();
      XmlJpOperation[] modeOperationList = modeOperations != null ? modeOperations.getOperation() : null;
      if (modeOperationList != null) {
        for (XmlJpOperation xml : modeOperationList) {
          modeExecutors.add(toOperation(xml, operations, shutdownManager));
        }
      }

      String storages = modeOperations != null ? modeOperations.getStorages() : null;

      modes.put(modeCode, new Mode(
          params,
          modeOperations != null && modeOperations.isUseTransaction(),
          storages != null ? Arrays.stream(storages.split(",")).map(String::trim).toArray(String[]::new) : null,
          modeExecutors,
          jpMode.getJpResult()
      ));
    }
    this.modes = modes;

    this.transactionManager = transactionManager;
    this.templateValueService = templateValueService;
  }

  private JPUtilXmlOperation.Executor toOperation(XmlJpOperation xml,
                                                  Map<String, JPUtilXmlOperation> operations,
                                                  JPApplicationShutdownManager shutdownManager) {
    String type = xml.getType();
    JPUtilXmlOperation operation = operations.get(type);
    if (operation == null) {
      LOG.error("Not found JPUtilXmlOperation with code {}", type);
      shutdownManager.exitWithError();
    }
    return operation.newOperation(xml);
  }

  public static JPUtilXmlAction of(String utilCode,
                                   XmlJpCheck jpCheck,
                                   XmlJpDefValues jpDefValues,
                                   Map<XmlJpMode, Collection<String>> jpModeMap,
                                   Map<String, JPUtilXmlOperation> operations,
                                   ChainedTransactionManager transactionManager,
                                   JPApplicationShutdownManager shutdownManager,
                                   JPTemplateValueService templateValueService) {
    return new JPUtilXmlAction(
        utilCode, jpCheck, jpDefValues, jpModeMap,
        operations, transactionManager, shutdownManager, templateValueService
    );
  }

  @Override
  protected Mono<JPUtilCheckOutParams> check(JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    if (checkExecutors.isEmpty()) {
      return super.check(inParams, swe, auth);
    }
    return JPMono.fromCallable(() -> {
      Map<String, Object> params = JPJsonUtils.getJsonMapper().toMap(inParams);
      try {
        execute(checkExecutors, params, swe, auth);
      } catch (JPAppRuntimeException e) {
        return JPUtilCheckOutParams.newBuilder()
            .denied(true)
            .description(e.getMessage())
            .build();
      } catch (Exception e) {
        return JPUtilCheckOutParams.DENIED;
      }
      return JPUtilCheckOutParams.ALLOW;
    });
  }

  @Override
  protected Mono<JPUtilDefValuesOutParams> inParamsDefValues(JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
      Map<String, Object> values = new HashMap<>();
      for (var entry : defValues.entrySet()) {
        values.put(entry.getKey(), templateValueService.getValue(entry.getValue(), auth));
      }
      return JPUtilDefValuesOutParams.newBuilder()
          .result(values)
          .build();
    });
  }

  @Override
  protected <T> Mono<JPUtilOutParams<T>> execMode(String modeCode, JPUtilInParams inParams, ServerWebExchange swe, AuthInfo auth) {
    return JPMono.fromCallable(() -> {
      Mode mode = modes.get(modeCode);
      if (mode == null) {
        throw new JPUtilModeNotFoundException(utilCode, modeCode);
      }
      Map<String, Object> params = JPJsonUtils.getJsonMapper().toMap(inParams);

      if (mode.useTransaction()) {
        TransactionStatus ts = transactionManager.getTransactionStatus(mode.storages());
        try {
          execute(mode.executors(), params, swe, auth);
          transactionManager.commit(ts);
        } catch (Exception e) {
          transactionManager.rollback(ts);
          throw e;
        }
      } else {
        execute(mode.executors(), params, swe, auth);
      }

      return (JPUtilOutParams<T>) getOutParams(mode.result(), mode.params(), params);
    });
  }

  private JPUtilOutParams getOutParams(XmlJpModeResult result, Collection<String> params, Map<String, Object> values) {
    String resultType = result != null ? result.getType() : null;
    boolean changeData = result != null && result.isChangeData();
    String description = resultType != null ? JPStringUtils.replaceParamValues(result.getDescription(), params, values::get) : null;

    if (resultType == null || JPUtilVoidOutParams.CODE.equals(resultType)) {
      return JPUtilVoidOutParams.newBuilder()
          .changeData(changeData)
          .build();
    } else if (JPUtilMessageOutParams.CODE.equals(resultType)) {
      return JPUtilMessageOutParams.newBuilder()
          .changeData(changeData)
          .description(description)
          .build();
    } else {
      return JPUtilVoidOutParams.EMPTY;
    }
  }

  private void execute(Collection<JPUtilXmlOperation.Executor> executors,
                       Map<String, Object> params, ServerWebExchange swe, AuthInfo auth) {
    if (executors == null || executors.isEmpty()) {
      return;
    }
    Map<String, JPObject> cache = new HashMap<>();
    for (JPUtilXmlOperation.Executor executor : executors) {
      executor.execute(cache, params, swe, auth);
    }
  }

  private record Mode(Collection<String> params,
                      boolean useTransaction, String[] storages,
                      Collection<JPUtilXmlOperation.Executor> executors,
                      XmlJpModeResult result) {

  }
}
