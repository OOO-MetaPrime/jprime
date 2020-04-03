package mp.jprime.metamaps.annotations.services;

import mp.jprime.meta.JPMeta;
import mp.jprime.metamaps.JPMapsLoader;
import mp.jprime.metamaps.annotations.JPAttrMap;
import mp.jprime.metamaps.annotations.JPClassMap;
import mp.jprime.metamaps.beans.JPAttrMapBean;
import mp.jprime.metamaps.beans.JPClassMapBean;
import mp.jprime.metamaps.beans.JPImmutableClassMapBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Загрузка мапинга по аннотациям
 */
@Service
public class JPMapsAnnoLoader implements JPMapsLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPMapsAnnoLoader.class);

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
  public Flux<mp.jprime.metamaps.JPClassMap> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<mp.jprime.metamaps.JPClassMap> sink) {
    if (metas == null || metas.isEmpty()) {
      return;
    }
    for (JPMeta meta : metas) {
      JPClassMap cls = meta.getClass().getAnnotation(JPClassMap.class);
      if (cls == null) {
        continue;
      }
      JPAttrMap[] attrs = cls.attrs();
      if (attrs.length == 0) {
        continue;
      }


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
      mp.jprime.metamaps.JPClassMap newCls = JPImmutableClassMapBean.newBuilder()
          .jpClassMap(JPClassMapBean.newBuilder()
              .code(cls.code())
              .storage(cls.storage())
              .map(cls.map())
              .attrs(newAttrs)
              .build()
          )
          .build();
      sink.next(newCls);
    }
  }
}