package mp.jprime.meta.loaders.annotations.services;

import mp.jprime.meta.JPMeta;
import mp.jprime.meta.JPMetaConvertor;
import mp.jprime.meta.JPMetaLoader;
import mp.jprime.meta.annotations.*;
import mp.jprime.meta.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Загрузка метаинформации по аннотациям
 */
@Service
public class JPMetaAnnoLoader implements JPMetaLoader {
  private Collection<JPMeta> metas;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setMetas(Collection<JPMeta> metas) {
    this.metas = metas;
  }

  @Override
  public Collection<mp.jprime.meta.JPClass> load() {
    if (metas == null || metas.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<mp.jprime.meta.JPClass> result = new ArrayList<>();
    for (JPMeta meta : metas) {
      JPClass cls = meta.getClass().getAnnotation(mp.jprime.meta.annotations.JPClass.class);
      if (cls == null) {
        continue;
      }
      JPAttr[] attrs = cls.attrs();
      if (attrs.length == 0) {
        continue;
      }
      Collection<mp.jprime.meta.JPAttr> newAttrs = new ArrayList<>(attrs.length);
      for (JPAttr attr : attrs) {
        newAttrs.add(JPMetaConvertor.of(cls.code(), attr));
      }

      Collection<String> tags = Stream.of(cls.tags())
          .filter(Objects::nonNull)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.toSet());

      mp.jprime.meta.JPClass newCls = JPClassBean.newBuilder()
          .guid(cls.guid())
          .code(cls.code())
          .qName(cls.qName())
          .tags(tags)
          .jpPackage(cls.jpPackage())
          .inner(cls.inner())
          .actionLog(cls.actionLog())
          .name(cls.name())
          .shortName(cls.shortName())
          .description(cls.description())
          .attrs(newAttrs)
          .build();
      result.add(newCls);
    }
    return result;
  }
}
