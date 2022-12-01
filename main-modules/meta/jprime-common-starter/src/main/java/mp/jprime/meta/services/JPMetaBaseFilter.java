package mp.jprime.meta.services;

import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaFilter;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.services.JPMapsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

/**
 * Фильтр меты
 *
 * `jprime.meta.api.filter.enabled` - признак публикации указанных данных
 * `jprime.meta.api.filter.jpClassCodes` - список кодовых имен классов через [,] для публикации
 * `jprime.meta.api.filter.jpStorageCodes` - список кодовых имен хранилищ через [,], классы которых публикуем
 */
@Service
public final class JPMetaBaseFilter implements JPMetaFilter {

  private Collection<String> jpClassCodes;
  private Collection<String> anonymousJpClassCodes;
  private Collection<String> jpStorageCodes;

  @Value("${jprime.meta.api.filter.enabled:false}")
  private boolean filterEnabled;

  @Value("${jprime.meta.api.filter.anonymous.enabled:false}")
  private boolean anonymousFilterEnabled;

  @Value("${jprime.meta.api.filter.jpClassCodes:#{null}}")
  private void setClassCodes(String[] jpClassCodes) {
    this.jpClassCodes = jpClassCodes != null ? Arrays.asList(jpClassCodes) : null;
  }

  @Value("${jprime.meta.api.filter.anonymous.jpClassCodes:#{null}}")
  private void setAnonymousClassCodes(String[] jpClassCodes) {
    this.anonymousJpClassCodes = jpClassCodes != null ? Arrays.asList(jpClassCodes) : null;
  }

  @Value("${jprime.meta.api.filter.jpStorageCodes:#{null}}")
  private void setStorageCodes(String[] jpStorageCodes) {
    this.jpStorageCodes = jpStorageCodes != null ? Arrays.asList(jpStorageCodes) : null;
  }

  /**
   * Описания хранилищ
   */
  private JPMapsStorage jpMapsStorage;

  @Autowired(required = false)
  private void setJpMapsStorage(JPMapsStorage jpMapsStorage) {
    this.jpMapsStorage = jpMapsStorage;
  }

  /**
   * Признак фильтрации
   *
   * @param jpClass Метаописание
   * @return Да/Нет
   */
  @Override
  public boolean filter(JPClass jpClass) {
    if (jpClass == null || jpClass.isInner()) {
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

  /**
   * Признак фильтрации
   *
   * @param jpClass Метаописание
   * @return Да/Нет
   */
  @Override
  public boolean anonymousFilter(JPClass jpClass) {
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
