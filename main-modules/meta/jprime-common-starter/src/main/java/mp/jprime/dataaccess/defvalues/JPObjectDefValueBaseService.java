package mp.jprime.dataaccess.defvalues;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPCreateRightException;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Логика вычисления значений по умолчанию
 */
@Service
public class JPObjectDefValueBaseService implements JPObjectDefValueService, JPClassesLinkFilter<JPObjectDefValue> {
  private Map<String, Collection<JPObjectDefValue>> jpObjectDefValues = new HashMap<>();
  private Collection<JPObjectDefValue> uniDefValues = new ArrayList<>();

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
  private void setDefValues(Collection<JPObjectDefValue> defValues) {
    if (defValues == null) {
      return;
    }
    Map<String, Collection<JPObjectDefValue>> jpObjectDefValues = new HashMap<>();
    Collection<JPObjectDefValue> uniDefValues = new ArrayList<>();
    for (JPObjectDefValue defValue : defValues) {
      try {
        JPClassesLink anno = defValue.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        if (anno.uni()) {
          uniDefValues.add(defValue);
        } else {
          for (String jpClassCode : anno.jpClasses()) {
            if (jpClassCode == null || jpClassCode.isEmpty()) {
              continue;
            }
            jpObjectDefValues.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(defValue);
          }
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    uniDefValues = filter(uniDefValues);

    // Размечаем * аннотацию
    Collection<String> keys = jpObjectDefValues.keySet();
    for (String key : keys) {
      Collection<JPObjectDefValue> values = jpObjectDefValues.get(key);
      values.addAll(uniDefValues);
      jpObjectDefValues.put(key, filter(values));
    }

    this.uniDefValues = uniDefValues;
    this.jpObjectDefValues = jpObjectDefValues;
  }

  /**
   * Список обработчиков значений по умолчанию
   *
   * @param jpClassCode Кодовое имя класса
   * @return Список обработчиков значений по умолчанию
   */
  private Collection<JPObjectDefValue> getDefValues(String jpClassCode) {
    Collection<JPObjectDefValue> result = jpObjectDefValues.get(jpClassCode);
    if (result != null && !result.isEmpty()) {
      return result;
    } else if (!uniDefValues.isEmpty()) {
      result = new ArrayList<>(uniDefValues);
      jpObjectDefValues.put(jpClassCode, result);
      return result;
    } else {
      return null;
    }
  }

  /**
   * Возвращает значения по умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по умолчанию
   * @param params      Параметры для вычисления значений по умолчанию
   */
  @Override
  public JPMutableData getDefValues(String jpClassCode, JPObjectDefValueParams params) {
    JPClass jpClass = getJPClassWithChecking(jpClassCode, params);
    AuthInfo authInfo = params.getAuth();
    JPMutableData data = JPMutableData.empty();

    // Проставляем значение для связи
    JPAttr refAttr = params.getRefAttrCode() != null ? jpClass.getAttr(params.getRefAttrCode()) : null;
    if (params.getRootId() != null &&
        refAttr != null &&
        refAttr.getRefJpClassCode() != null &&
        refAttr.getRefJpClassCode().equals(params.getRootJpClassCode())) {
      data.put(params.getRefAttrCode(), params.getRootId());
    }
    Collection<JPObjectDefValue> vals = getDefValues(jpClassCode);
    if (vals != null && !vals.isEmpty()) {
      vals.forEach(x -> x.appendValues(data, params));
    }
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
    return data;
  }

  protected JPClass getJPClassWithChecking(String jpClassCode, JPObjectDefValueParams params) {
    JPClass jpClass = metaStorage.getJPClassByCode(jpClassCode);
    if (jpClass == null) {
      throw new JPClassNotFoundException(jpClassCode);
    }
    AuthInfo authInfo = params.getAuth();
    if (params.getSource() == Source.USER
        && authInfo != null
        && !securityManager.checkCreate(jpClass.getJpPackage(), authInfo.getRoles())) {
      throw new JPCreateRightException(jpClassCode);
    }
    return jpClass;
  }
}
