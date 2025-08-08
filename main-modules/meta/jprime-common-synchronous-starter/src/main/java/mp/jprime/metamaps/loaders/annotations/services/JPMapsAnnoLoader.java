package mp.jprime.metamaps.loaders.annotations.services;

import mp.jprime.meta.JPMeta;
import mp.jprime.metamaps.JPMapsLoader;
import mp.jprime.metamaps.JPMetaMapConvertor;
import mp.jprime.metamaps.annotations.JPAttrMap;
import mp.jprime.metamaps.annotations.JPClassMap;
import mp.jprime.metamaps.beans.JPAttrMapBean;
import mp.jprime.metamaps.beans.JPClassMapBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Загрузка мапинга по аннотациям
 */
@Service
public class JPMapsAnnoLoader implements JPMapsLoader {
  private Collection<JPMeta> metaList;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setMetaList(Collection<JPMeta> metaList) {
    this.metaList = metaList;
  }

  @Override
  public Collection<mp.jprime.metamaps.JPClassMap> load() {
    if (metaList == null || metaList.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<mp.jprime.metamaps.JPClassMap> result = new ArrayList<>();
    for (JPMeta meta : metaList) {
      JPClassMap cls = meta.getClass().getAnnotation(JPClassMap.class);
      if (cls == null) {
        continue;
      }

      JPAttrMap[] attrs = cls.attrs();
      Collection<mp.jprime.metamaps.JPAttrMap> newAttrs = new ArrayList<>(attrs.length);
      for (JPAttrMap attr : attrs) {
        newAttrs.add(JPMetaMapConvertor.of(attr));
      }
      mp.jprime.metamaps.JPClassMap newCls = JPClassMapBean.newBuilder()
          .code(cls.code())
          .storage(cls.storage())
          .map(cls.map())
          .schema(cls.schema())
          .attrs(newAttrs)
          .build();
      result.add(newCls);
    }
    return result;
  }
}