package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Базовая логика проверки доступа к объекту
 */
public abstract class JPObjectAccessBaseService {

  @Service
  private static final class Links {
    private static JPResourceAccessService RESOURCE_ACCESS_SERVICE;
    private static JPSecurityStorage SECURITY_STORAGE;
    private static JPMetaStorage META_STORAGE;
    private static JPDataCheckService DATA_CHECK_SERVICE;

    private Links(@Autowired JPResourceAccessService accessService,
                  @Autowired JPSecurityStorage securityStorage,
                  @Autowired JPMetaStorage metaStorage,
                  @Autowired JPDataCheckService dataCheckService) {
      RESOURCE_ACCESS_SERVICE = accessService;
      SECURITY_STORAGE = securityStorage;
      META_STORAGE = metaStorage;
      DATA_CHECK_SERVICE = dataCheckService;
    }
  }

  protected JPResourceAccessService getAccessService() {
    return Links.RESOURCE_ACCESS_SERVICE;
  }

  protected JPSecurityStorage getSecurityStorage() {
    return Links.SECURITY_STORAGE;
  }

  protected JPMetaStorage getMetaStorage() {
    return Links.META_STORAGE;
  }

  protected JPDataCheckService getDataCheckService() {
    return Links.DATA_CHECK_SERVICE;
  }

  protected boolean isCreateCheck(String classCode, JPMap createData, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = getAccessService().checkCreate(classCode, auth);
    if (!access.isAccess()) {
      return false;
    }
    Filter accessFilter = access.getFilter();
    // проверки на значение
    if (createData != null && accessFilter != null && !checkData(accessFilter, createData, auth, false)) {
      return false;
    }
    return true;
  }

  protected boolean isReadCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = getAccessService().checkRead(classCode, auth);
    return access.isAccess();
  }

  protected boolean isUpdateCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = getAccessService().checkUpdate(classCode, auth);
    return access.isAccess();
  }

  protected boolean isDeleteCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = getAccessService().checkDelete(classCode, auth);
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

  protected boolean checkData(Filter filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    return getDataCheckService().check(filter, data, auth, notContainsDefaultValue);
  }
}
