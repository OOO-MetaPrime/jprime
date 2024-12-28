package mp.jprime.metamaps.annotations.services;

import mp.jprime.meta.JPMeta;
import mp.jprime.metamaps.JPMapsLoader;
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
  private Collection<JPMeta> metas;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setMetas(Collection<JPMeta> metas) {
    this.metas = metas;
  }

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Collection<mp.jprime.metamaps.JPClassMap> load() {
    if (metas == null || metas.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<mp.jprime.metamaps.JPClassMap> result = new ArrayList<>();
    for (JPMeta meta : metas) {
      JPClassMap cls = meta.getClass().getAnnotation(JPClassMap.class);
      if (cls == null) {
        continue;
      }

      JPAttrMap[] attrs = cls.attrs();
      Collection<mp.jprime.metamaps.JPAttrMap> newAttrs = new ArrayList<>(attrs.length);
      for (JPAttrMap attr : attrs) {
        newAttrs.add(JPAttrMapBean.newBuilder()
            .code(attr.code())
            .map(attr.map())
            .fuzzyMap(attr.fuzzyMap())
            .cs(attr.cs().getCode())
            .readOnly(attr.readOnly())
            .build());
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