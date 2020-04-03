package mp.jprime.meta.services;

import mp.jprime.annotations.JPBeanInfo;
import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPLinkedData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.exceptions.JPRuntimeException;
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
  private Map<String, Collection<String>> defaultJpAttrCollections = new HashMap<>();

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

  /**
   * Создает объект
   *
   * @param jpClass Метаописание класса
   * @param data    Данные объекта
   * @return Новый объект
   */
  @Override
  public JPObject newInstance(JPClass jpClass, Map<String, Object> data) {
    JPObject jpObj = jpBeans.get(jpClass.getCode());
    JPData jpData = JPData.newBuilder().data(data).build();

    JPObject.Builder builder = JPObject.newBuilder(jpClass);
    if (jpObj != null) {
      builder.objectClass(jpObj.getClass());
    }
    return builder.jpData(jpData).build();
  }

  /**
   * Создает объект
   *
   * @param jpClass      Метаописание класса
   * @param jpData       Данные объекта
   * @param jpLinkedData Данные связанных объектов
   * @return Новый объект
   */
  @Override
  public JPObject newInstance(JPClass jpClass, JPData jpData, JPLinkedData jpLinkedData) {
    JPObject jpObj = jpBeans.get(jpClass.getCode());

    JPObject.Builder builder = JPObject.newBuilder(jpClass);
    if (jpObj != null) {
      builder.objectClass(jpObj.getClass());
    }
    return builder.jpData(jpData).jpLinkedData(jpLinkedData).build();
  }

  /**
   * Возвращает список атрибутов по-умолчанию
   *
   * @param jpClass Кодовое имя метаописания класса
   * @return Список атрибутов по-умолчанию
   */
  public Collection<String> getDefaultJpAttrs(String jpClass) {
    return jpClass != null ? defaultJpAttrCollections.get(jpClass) : null;
  }
}
