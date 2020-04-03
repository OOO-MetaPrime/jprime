package mp.jprime.meta.annotations.services;

import mp.jprime.meta.JPMeta;
import mp.jprime.meta.JPMetaLoader;
import mp.jprime.meta.annotations.JPClass;
import mp.jprime.meta.annotations.JPAttr;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.meta.beans.JPClassBean;
import mp.jprime.meta.beans.JPImmutableClassBean;
import mp.jprime.meta.beans.JPType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Загрузка метаинформации по аннотациям
 */
@Service
public class JPMetaAnnoLoader implements JPMetaLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPMetaAnnoLoader.class);

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
  public Flux<mp.jprime.meta.JPClass> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<mp.jprime.meta.JPClass> sink) {
    if (metas == null || metas.isEmpty()) {
      return;
    }
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
        newAttrs.add(JPAttrBean.newBuilder()
            .guid(attr.guid())
            .type(attr.type().getCode())
            .length(attr.length())
            .identifier(attr.identifier())
            .mandatory(attr.mandatory())
            .qName(attr.qName())
            .jpPackage(attr.jpPackage())
            .name(attr.name())
            .shortName(attr.shortName())
            .description(attr.description())
            .code(attr.code())
            .jpClassCode(cls.guid())
            .refJpClassCode(attr.refJpClass())
            .refJpAttrCode(attr.refJpAttr())
            .virtualReference(attr.virtualReference())
            .virtualType(!JPType.NONE.getCode().equals(attr.virtualType().getCode()) ? attr.virtualType().getCode() : null)
            .build());
      }
      mp.jprime.meta.JPClass newCls = JPImmutableClassBean.newBuilder()
          .jpClass(JPClassBean.newBuilder()
              .guid(cls.guid())
              .code(cls.code())
              .qName(cls.qName())
              .pluralCode(cls.pluralCode())
              .jpPackage(cls.jpPackage())
              .inner(cls.inner())
              .name(cls.name())
              .shortName(cls.shortName())
              .description(cls.description())
              .attrs(newAttrs)
              .build()
          )
          .build();
      sink.next(newCls);
    }
  }
}
