package mp.jprime.utils.attrvaluechange;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.dataaccess.transaction.ChainedTransactionManager;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.JPUtil;
import mp.jprime.utils.JPUtilMessageOutParams;
import mp.jprime.utils.annotations.JPUtilLink;
import mp.jprime.utils.annotations.JPUtilModeLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

import static mp.jprime.security.Role.ADMIN;

/**
 * Утилита массового изменения атрибутов
 */
@JPUtilLink(
    uni = true,
    authRoles = ADMIN,
    code = "attrvaluechangeutil",
    qName = "attrvaluechangeutil.title",
    title = "Утилита массового изменения атрибутов"
)
public class AttrValueChangeUtil implements JPUtil {

  private static final Logger LOG = LoggerFactory.getLogger(AttrValueChangeUtil.class);

  private static final String UTIL_Q_NAME = "attrvaluechangeutil.done";
  private static final String UTIL_FAILED_CODE = "attrvaluechangeutil.failed";
  private static final String UTIL_FAILED_LOG_MESSAGE = "'{}': failed changing attributes with exception: {}";
  private static final String JP_CLASS_NOT_FOUND_LOG_MESSAGE = "'{}': jpClass with objectClassCode={} not found";
  private static final String EMPTY_ATTRIBUTES_LOG_MESSAGE = "{}: attributes were not passed";
  private static final String INVALID_ATTRS_LOG_MESSAGE = "'{}': entity with objectClassCode='{}' doesn't have following attributes: '{}'";
  private static final String UTIL_FAILED_USER_MESSAGE = "Массовое внесение изменений не выполнено";
  private static final String UTIL_SUCCESS_USER_MESSAGE = "Массовое внесение изменений успешно выполнено";

  private JPObjectRepositoryService repository;
  private ChainedTransactionManager transactionManager;
  private JPMetaStorage jpMetaStorage;

  @Autowired
  private void setRepository(JPObjectRepositoryService repository) {
    this.repository = repository;
  }

  @Autowired
  private void setTransactionManager(ChainedTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Autowired
  public void setJpMetaStorage(JPMetaStorage jpMetaStorage) {
    this.jpMetaStorage = jpMetaStorage;
  }

  @JPUtilModeLink(
      code = "change",
      qName = "attrvaluechangeutil.change",
      title = "Массовое изменение атрибутов",
      outClass = JPUtilMessageOutParams.class
  )
  public Mono<JPUtilMessageOutParams> change(AttrValueChangeIn in, AuthInfo auth) {
    if (checkAttrs(in)) {
      return update(in, auth);
    } else {
      return Mono.error(new JPAppRuntimeException(UTIL_FAILED_CODE, UTIL_FAILED_USER_MESSAGE));
    }
  }

  private boolean checkAttrs(AttrValueChangeIn in) {
    if (in.getAttrs() == null || in.getAttrs().isEmpty()) {
      LOG.error(EMPTY_ATTRIBUTES_LOG_MESSAGE, UTIL_FAILED_CODE);
      return false;
    }
    JPClass jpAppClass = jpMetaStorage.getJPClassByCode(in.getObjectClassCode());
    if (jpAppClass != null) {
      Collection<String> invalidAttrCodes = in.getAttrs().stream()
          .filter(codeValue -> jpAppClass.getAttr(codeValue.getAttrCode()) == null)
          .map(JsonAttrCodeValue::getAttrCode)
          .collect(Collectors.toList());
      if (!invalidAttrCodes.isEmpty()) {
        LOG.error(INVALID_ATTRS_LOG_MESSAGE,
            UTIL_FAILED_CODE, in.getObjectClassCode(), invalidAttrCodes);
      }
      return invalidAttrCodes.isEmpty();
    } else {
      LOG.error(JP_CLASS_NOT_FOUND_LOG_MESSAGE, UTIL_FAILED_CODE, in.getObjectClassCode());
      return false;
    }
  }

  private Mono<JPUtilMessageOutParams> update(AttrValueChangeIn in, AuthInfo auth) {
    TransactionStatus txStatus = transactionManager.getTransactionStatus();
    try {
      in.getObjectIds().forEach(id -> {
        JPUpdate.Builder update = JPUpdate.update(JPId.get(in.getObjectClassCode(), id))
            .auth(auth);
        setValues(update, in.getAttrs());
        repository.update(
            update.build()
        );
      });

      transactionManager.commit(txStatus);
      return Mono.fromCallable(() ->
          JPUtilMessageOutParams.newBuilder()
              .changeData(true)
              .description(UTIL_SUCCESS_USER_MESSAGE)
              .qName(UTIL_Q_NAME)
              .build());
    } catch (Exception e) {
      transactionManager.rollback(txStatus);
      LOG.error(UTIL_FAILED_LOG_MESSAGE, UTIL_FAILED_CODE, e.getMessage(), e);
      return Mono.error(new JPAppRuntimeException(UTIL_FAILED_CODE, UTIL_FAILED_USER_MESSAGE));
    }
  }

  private void setValues(JPUpdate.Builder update, Collection<JsonAttrCodeValue> attrs) {
    attrs.forEach(codeValue -> update.set(codeValue.getAttrCode(), codeValue.getAttrValue()));
  }
}
