package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Базовая логика проверки доступа к объекту
 */
public abstract class JPObjectAccessBaseService {
  // Проверка доступа
  protected JPResourceAccessService accessService;
  // Хранилище настроек RBAC
  protected JPSecurityStorage securityManager;
  // Хранилище метаинформации
  protected JPMetaStorage metaStorage;

  @Autowired
  private void setAccessService(JPResourceAccessService accessService) {
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

  protected boolean isCreateCheck(String classCode, AuthInfo auth) {
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkCreate(classCode, auth);
    return access == null || access.isAccess();
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

  protected Filter getFilter(Filter selectWhere, JPResourceAccess access) {
    Filter filter = access == null ? null : access.getFilter();
    if (filter == null) {
      filter = selectWhere;
    } else if (selectWhere != null) {
      filter = Filter.and(filter, selectWhere);
    }
    return filter;
  }
}
