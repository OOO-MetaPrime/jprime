package mp.jprime.meta.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.JPObjectAccessServiceAware;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.services.JPMapsStorage;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Фильтр меты
 * `jprime.meta.api.filter.enabled` - признак публикации указанных данных
 * `jprime.meta.api.filter.jpClassCodes` - список кодовых имен классов через [,] для публикации
 * `jprime.meta.api.filter.jpStorageCodes` - список кодовых имен хранилищ через [,], классы которых публикуем
 */
@Service
public final class JPMetaCommonFilter implements JPMetaFilter, JPObjectAccessServiceAware {
  private Collection<String> jpClassCodes;
  private Collection<String> anonymousJpClassCodes;
  private Collection<String> jpStorageCodes;

  @Value("${jprime.meta.api.filter.enabled:false}")
  private boolean filterEnabled;

  @Value("${jprime.meta.api.filter.anonymous.enabled:false}")
  private boolean anonymousFilterEnabled;

  @Value("${jprime.meta.api.filter.jpClassCodes:#{null}}")
  private void setClassCodes(String[] jpClassCodes) {
    this.jpClassCodes = jpClassCodes != null ? Set.of(jpClassCodes) : null;
  }

  @Value("${jprime.meta.api.filter.anonymous.jpClassCodes:#{null}}")
  private void setAnonymousClassCodes(String[] jpClassCodes) {
    this.anonymousJpClassCodes = jpClassCodes != null ? Set.of(jpClassCodes) : null;
  }

  @Value("${jprime.meta.api.filter.jpStorageCodes:#{null}}")
  private void setStorageCodes(String[] jpStorageCodes) {
    this.jpStorageCodes = jpStorageCodes != null ? Set.of(jpStorageCodes) : null;
  }

  private JPMetaStorage jpMetaStorage;
  private JPMapsStorage jpMapsStorage;
  private JPObjectAccessService objectAccessService;

  @Autowired
  private void setJpMetaStorage(JPMetaStorage jpMetaStorage) {
    this.jpMetaStorage = jpMetaStorage;
  }

  @Autowired(required = false)
  private void setJpMapsStorage(JPMapsStorage jpMapsStorage) {
    this.jpMapsStorage = jpMapsStorage;
  }

  @Override
  public void setJpObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @Override
  public Collection<JPClass> getList() {
    return getListByAuth(null);
  }

  @Override
  public Collection<JPClass> getList(Collection<String> jpClassCodeList) {
    return getList(jpClassCodeList, null);
  }

  @Override
  public Collection<JPClass> getList(Collection<String> jpClassCodeList, AuthInfo auth) {
    if (jpClassCodeList == null || jpClassCodeList.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPClass> result = new ArrayList<>(jpClassCodeList.size());
    for (String jpClassCode : jpClassCodeList) {
      JPClass jpClass = get(jpClassCode, auth);
      if (jpClass != null) {
        result.add(jpClass);
      }
    }
    return result;
  }

  @Override
  public Collection<JPClass> getList(AuthInfo auth) {
    return getListByAuth(auth);
  }

  private Collection<JPClass> getListByAuth(AuthInfo auth) {
    Collection<JPClass> classes = jpMetaStorage.getJPClasses();
    if (classes == null || classes.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPClass> result = new ArrayList<>(classes.size());
    for (JPClass jpClass : classes) {
      if (filter(jpClass, auth)) {
        result.add(jpClass);
      }
    }
    return result;
  }

  @Override
  public JPClass get(String jpClassCode, AuthInfo auth) {
    JPClass jpClass = jpClassCode != null ? jpMetaStorage.getJPClassByCode(jpClassCode) : null;
    if (jpClass == null) {
      return null;
    }
    return filter(jpClass, auth) ? jpClass : null;
  }

  @Override
  public JPClass getAnonymous(String jpClassCode) {
    JPClass jpClass = jpClassCode != null ? jpMetaStorage.getJPClassByCode(jpClassCode) : null;
    if (jpClass == null) {
      return null;
    }
    return anonymousFilter(jpClass) ? jpClass : null;
  }

  private boolean filter(JPClass jpClass, AuthInfo auth) {
    if (jpClass == null || jpClass.isInner()) {
      return false;
    }
    if (auth != null && !objectAccessService.checkRead(jpClass.getCode(), auth)) {
      return false;
    }
    if (!filterEnabled) {
      return true;
    }
    if (jpClassCodes != null) {
      if (jpClassCodes.isEmpty()) {
        return false;
      }
      return jpClassCodes.contains(jpClass.getCode());
    }
    if (jpStorageCodes != null) {
      if (jpStorageCodes.isEmpty()) {
        return false;
      }
      JPClassMap jpClassMap = jpMapsStorage == null ? null : jpMapsStorage.get(jpClass);
      return jpClassMap != null && jpStorageCodes.contains(jpClassMap.getStorage());
    }
    return true;
  }

  private boolean anonymousFilter(JPClass jpClass) {
    if (jpClass == null || jpClass.isInner()) {
      return false;
    }
    if (!anonymousFilterEnabled) {
      return false;
    }
    if (anonymousJpClassCodes != null) {
      if (anonymousJpClassCodes.isEmpty()) {
        return false;
      }
      return anonymousJpClassCodes.contains(jpClass.getCode());
    }
    return false;
  }
}
