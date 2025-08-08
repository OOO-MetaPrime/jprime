package mp.jprime.meta.services;

import mp.jprime.annotations.JPBeanInfo;
import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectBase;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Логика работы с JPBean
 */
@Service
public class JPBeanMemoryService implements JPBeanService, JPClassesLinkFilter<JPObject> {
  private Map<String, JPObject> jpBeans = new HashMap<>();
  private final Map<String, Collection<String>> defaultJpAttrCollections = new HashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setBeans(Collection<JPObject> beans) {
    if (beans == null) {
      return;
    }
    Map<String, Collection<JPObject>> jpAllBeans = new HashMap<>();
    for (JPObject bean : beans) {
      try {
        JPClassesLink linkAnno = bean.getClass().getAnnotation(JPClassesLink.class);
        JPBeanInfo beanAnno = bean.getClass().getAnnotation(JPBeanInfo.class);
        if (linkAnno == null) {
          continue;
        }
        for (String jpClassCode : linkAnno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          jpAllBeans.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(bean);
          if (beanAnno != null) {
            defaultJpAttrCollections.put(jpClassCode,
                Collections.unmodifiableList(Arrays.stream(beanAnno.defaultJpAttrCollection())
                    .filter(Objects::nonNull)
                    .filter(x -> !x.isEmpty())
                    .collect(Collectors.toList()))
            );
          }
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    Map<String, JPObject> jpBeans = new HashMap<>();
    for (String key : jpAllBeans.keySet()) {
      jpBeans.put(key, filter(jpAllBeans.get(key)).iterator().next());
    }
    this.jpBeans = jpBeans;
  }

  @Override
  public JPObject newInstance(JPClass jpClass, Map<String, Object> data) {
    return newInstance(jpClass, JPData.newBuilder().data(data).build(), null);
  }

  @Override
  public JPObject newInstance(String jpClassCode, String primaryKeyAttrCode, JPData jpData) {
    JPObject jpObj = jpBeans.get(jpClassCode);

    if (jpObj == null) {
      return JPObjectBase.newBaseInstance(jpClassCode, primaryKeyAttrCode, jpData);
    } else {
      return jpObj.newInstance(jpClassCode, primaryKeyAttrCode, jpData);
    }
  }

  @Override
  public JPObject newInstance(JPClass jpClass, JPData jpData, JPLinkedData jpLinkedData) {
    JPAttr primaryKeyAttr = jpClass.getPrimaryKeyAttr();
    String jpClassCode = jpClass.getCode();
    String primaryKeyAttrCode = primaryKeyAttr != null ? primaryKeyAttr.getCode() : null;
    JPObject jpObj = jpBeans.get(jpClass.getCode());
    if (jpObj == null) {
      return JPObjectBase.newBaseInstance(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
    } else {
      return jpObj.newInstance(jpClassCode, primaryKeyAttrCode, jpData, jpLinkedData);
    }
  }

  /**
   * Возвращает список атрибутов по умолчанию
   *
   * @param jpClass Кодовое имя метаописания класса
   * @return Список атрибутов по умолчанию
   */
  public Collection<String> getDefaultJpAttrs(String jpClass) {
    return jpClass != null ? defaultJpAttrCollections.get(jpClass) : null;
  }
}
