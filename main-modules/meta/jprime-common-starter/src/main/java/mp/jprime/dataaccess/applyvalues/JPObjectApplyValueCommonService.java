package mp.jprime.dataaccess.applyvalues;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Логика для дополнения значений
 */
@Service
public final class JPObjectApplyValueCommonService implements JPObjectApplyValueService, JPClassesLinkFilter<JPObjectApplyValue> {
  private Map<String, Collection<JPObjectApplyValue>> jpObjectApplyValues = new HashMap<>();

  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Хранилище настроек безопасности
  private JPSecurityStorage securityManager;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setApplyValues(Collection<JPObjectApplyValue> applyValues) {
    if (applyValues == null) {
      return;
    }
    Map<String, Collection<JPObjectApplyValue>> jpObjectApplyValues = new HashMap<>();
    for (JPObjectApplyValue applyValue : applyValues) {
      try {
        JPClassesLink anno = applyValue.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          jpObjectApplyValues.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(applyValue);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    // Размечаем * аннотацию
    Collection<String> keys = jpObjectApplyValues.keySet();
    for (String key : keys) {
      jpObjectApplyValues.put(key, filter(jpObjectApplyValues.get(key)));
    }
    this.jpObjectApplyValues = jpObjectApplyValues;
  }

  /**
   * Список обработчиков дополнения значений
   *
   * @param jpClassCode Кодовое имя класса
   * @return Список обработчиков дополнения значений
   */
  private Collection<JPObjectApplyValue> getApplyValues(String jpClassCode) {
    return jpObjectApplyValues.get(jpClassCode);
  }

  /**
   * Возвращает дополненные значения
   *
   * @param params Параметры для дополнения значений
   */
  @Override
  public JPData getApplyValues(JPObjectApplyValueParams params) {
    String jpClassCode = params.getJpClassCode();
    if (jpClassCode == null) {
      return JPData.empty();
    }
    JPClass jpClass = metaStorage.getJPClassByCode(jpClassCode);
    if (jpClass == null) {
      throw new JPClassNotFoundException(jpClassCode);
    }
    JPMutableData data = JPMutableData.empty();

    Collection<JPObjectApplyValue> vals = getApplyValues(jpClassCode);
    if (vals != null && !vals.isEmpty()) {
      for (JPObjectApplyValue val : vals) {
        JPMutableData values = val.getAppendValues(params);
        if (values == null) {
          continue;
        }
        data.putIfAbsent(values);
      }
    }
    AuthInfo authInfo = params.getAuth();
    if (!data.isEmpty() && params.getSource() == Source.USER && authInfo != null) {
      Collection<String> removes = new ArrayList<>();
      for (Map.Entry<String, Object> entry : data.entrySet()) {
        String key = entry.getKey();
        JPAttr attr = jpClass.getAttr(key);
        if (attr == null || !securityManager.checkRead(attr.getJpPackage(), authInfo.getRoles())) {
          removes.add(key);
        }
      }
      removes.forEach(data::remove);
    }
    return data.isEmpty() ? JPData.empty() : JPData.of(data);
  }
}
