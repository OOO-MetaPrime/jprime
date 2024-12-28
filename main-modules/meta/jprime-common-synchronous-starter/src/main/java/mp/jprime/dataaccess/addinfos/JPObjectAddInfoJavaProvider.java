package mp.jprime.dataaccess.addinfos;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Логика получения дополнительных сведений об объекте через java
 */
@Service
public final class JPObjectAddInfoJavaProvider implements JPObjectAddInfoProvider, JPClassesLinkFilter<JPObjectAddInfo> {
  private Map<String, Collection<JPObjectAddInfo>> jpObjectAddInfos = new HashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setDefValues(Collection<JPObjectAddInfo> list) {
    if (list == null) {
      return;
    }
    Map<String, Collection<JPObjectAddInfo>> jpObjectAddInfos = new HashMap<>();
    for (JPObjectAddInfo addInfo : list) {
      try {
        JPClassesLink anno = addInfo.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          jpObjectAddInfos.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(addInfo);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    this.jpObjectAddInfos = jpObjectAddInfos;
  }

  /**
   * Список обработчиков
   *
   * @param jpClassCode Кодовое имя класса
   * @return Список обработчиков
   */
  private Collection<JPObjectAddInfo> getJPObjectAddInfo(String jpClassCode) {
    if (jpObjectAddInfos.containsKey(jpClassCode)) {
      return jpObjectAddInfos.get(jpClassCode);
    }
    return null;
  }


  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  @Override
  public Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params) {
    String jpClassCode = params != null ? params.getJpClassCode() : null;
    Collection<JPObjectAddInfo> vals = jpClassCode != null ? getJPObjectAddInfo(jpClassCode) : null;
    if (vals != null && !vals.isEmpty()) {
      Collection<AddInfo> result = new ArrayList<>();
      vals.forEach(x -> result.addAll(x.getAddInfo(params)));
      return result;
    }
    return Collections.emptyList();
  }
}
