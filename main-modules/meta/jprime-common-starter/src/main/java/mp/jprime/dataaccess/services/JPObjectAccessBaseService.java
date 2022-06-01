package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPResourceAccessServiceAware;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Базовая логика проверки доступа к объекту
 */
public abstract class JPObjectAccessBaseService implements JPResourceAccessServiceAware {
  // Проверка доступа
  protected JPResourceAccessService accessService;
  // Хранилище настроек RBAC
  protected JPSecurityStorage securityManager;
  // Хранилище метаинформации
  protected JPMetaStorage metaStorage;
  // Сервис проверки данных указанному условию
  private JPDataCheckService dataCheckService;

  @Override
  public void setJpResourceAccessService(JPResourceAccessService accessService) {
    this.accessService = accessService;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  @Autowired
  private void setDataCheckService(JPDataCheckService dataCheckService) {
    this.dataCheckService = dataCheckService;
  }

  protected boolean isCreateCheck(String classCode, JPMutableData createData, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkCreate(classCode, auth);
    if (!access.isAccess()) {
      return false;
    }
    Filter accessFilter = access.getFilter();
    // проверки на значение
    if (createData != null && accessFilter != null && !checkData(accessFilter, createData, auth)) {
      return false;
    }
    return true;
  }

  protected boolean isReadCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkRead(classCode, auth);
    return access.isAccess();
  }

  protected boolean isUpdateCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkUpdate(classCode, auth);
    return access.isAccess();
  }

  protected boolean isDeleteCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkDelete(classCode, auth);
    return access.isAccess();
  }

  protected JPSelect toSelect(JPId id, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return JPSelect.from(id.getJpClass())
        .attr(jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) ? JPMeta.Attr.JPPACKAGE : jpClass.getPrimaryKeyAttr().getCode())
        .where(
            getFilter(
                Filter.attr(jpClass.getPrimaryKeyAttr()).eq(id.getId()),
                access
            )
        )
        .auth(auth)
        .build();
  }

  protected JPSelect toSelect(Collection<? extends Comparable> keys, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return JPSelect.from(jpClass)
        .attr(jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) ? JPMeta.Attr.JPPACKAGE : jpClass.getPrimaryKeyAttr().getCode())
        .where(
            getFilter(
                Filter.attr(jpClass.getPrimaryKeyAttr()).in(keys),
                access
            )
        )
        .auth(auth)
        .build();
  }

  protected Filter getFilter(Filter selectWhere, JPResourceAccess access) {
    Filter filter = access == null ? null : access.getFilter();
    if (filter == null) {
      filter = selectWhere;
    } else if (selectWhere != null) {
      filter = Filter.and(filter, selectWhere);
    }
    return filter;
  }

  protected boolean checkData(Filter filter, JPMutableData data, AuthInfo auth) {
    return dataCheckService.check(filter, data, auth);
  }
}
