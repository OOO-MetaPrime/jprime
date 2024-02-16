package mp.jprime.meta.annotations.services;

import mp.jprime.beans.PropertyType;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.JPMetaLoader;
import mp.jprime.meta.JPProperty;
import mp.jprime.meta.annotations.*;
import mp.jprime.meta.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Flux<Collection<mp.jprime.meta.JPClass>> load() {
    return Mono.fromCallable(this::annoLoad).flux();
  }

  private Collection<mp.jprime.meta.JPClass> annoLoad() {
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
        String code = attr.code();
        JPType type = attr.type();
        JPFile jpFile = attr.refJpFile();
        JPSimpleFraction simpleFraction = attr.simpleFraction();
        JPMoney money = attr.money();
        JPGeometry geometry = attr.geometry();

        Map<String, JPPropertySchema> schemas = getSchemas(attr.schemaProps());
        newAttrs.add(JPAttrBean.newBuilder()
            .guid(attr.guid())
            .type(type)
            .length(attr.length())
            .identifier(attr.identifier())
            .mandatory(attr.mandatory())
            .qName(attr.qName())
            .jpPackage(attr.jpPackage())
            .name(attr.name())
            .shortName(attr.shortName())
            .description(attr.description())
            .code(code)
            .jpClassCode(cls.code())
            // Настройка ссылки класс+атрибут
            .refJpClassCode(attr.refJpClass())
            .refJpAttrCode(attr.refJpAttr())
            // Настройка виртуальной ссылки
            .virtualReference(
                JPVirtualPathBean.newInstance(
                    attr.virtualReference(),
                    !JPType.NONE.getCode().equals(attr.virtualType().getCode()) ? attr.virtualType() : null
                )
            )
            // Настройка файла
            .refJpFile(
                type != JPType.FILE || jpFile.storageCode().isEmpty() || jpFile.storageFilePath().isEmpty() ? null :
                    JPFileBean.newBuilder(code)
                        .storageCode(jpFile.storageCode())
                        .storageFilePath(jpFile.storageFilePath())
                        .storageCodeAttrCode(jpFile.storageCodeAttrCode())
                        .storageFilePathAttrCode(jpFile.storageFilePathAttrCode())
                        .fileTitleAttrCode(jpFile.fileTitleAttrCode())
                        .fileExtAttrCode(jpFile.fileExtAttrCode())
                        .fileSizeAttrCode(jpFile.fileSizeAttrCode())
                        .fileDateAttrCode(jpFile.fileDateAttrCode())
                        .fileInfoAttrCode(jpFile.fileInfoAttrCode())
                        .fileStampAttrCode(jpFile.fileStampAttrCode())
                        .build()
            )
            // Настройка простой дроби
            .simpleFraction(
                type != JPType.SIMPLEFRACTION ? null :
                    JPSimpleFractionBean.newBuilder(code)
                        .integerAttrCode(simpleFraction.integerAttrCode())
                        .denominatorAttrCode(simpleFraction.denominatorAttrCode())
                        .build()
            )
            // Настройка денежного типа
            .money(
                type != JPType.MONEY ? null :
                    JPMoneyBean.newBuilder(code)
                        .currencyCode(money.currency())
                        .build()
            )
            // Настройка пространственных данных
            .geometry(
                type != JPType.GEOMETRY ? null :
                    JPGeometryBean.newBuilder()
                        .srid(geometry.SRID())
                        .build()
            )
            // Свойства псевдо-меты
            .schemaProps(
                toJPProperty(attr.jpProps(), schemas)
            )
            .signAttrCode(attr.signAttrCode())
            .build()
        );
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

  private Collection<JPProperty> toJPProperty(mp.jprime.meta.annotations.JPProperty[] schemaProps,
                                              Map<String, JPPropertySchema> schemas) {
    if (schemaProps == null || schemaProps.length == 0) {
      return null;
    }
    return Arrays.stream(schemaProps).map(p -> toJPProperty(p, schemas)).collect(Collectors.toList());
  }

  private JPProperty toJPProperty(mp.jprime.meta.annotations.JPProperty property,
                                  Map<String, JPPropertySchema> schemas) {
    JPPropertyBean.Builder jpProp = JPPropertyBean.builder()
        .code(property.code())
        .type(property.type())
        .length(property.length())
        .multiple(property.multiple())
        .mandatory(property.mandatory())
        .name(property.name())
        .shortName(property.shortName())
        .description(property.description())
        .qName(property.qName())
        .refJpClassCode(property.refJpClass())
        .refJpAttrCode(property.refJpAttr());
    if (PropertyType.ELEMENT.equals(property.type())) {
      JPPropertySchema schema = schemas.get(property.schemaCode());
      if (schema == null) {
        throw new JPRuntimeException("Invalid property schema code: '" + property.schemaCode() + '\'' +
            " in the JPProperty{code='" + property.code() + "', qName='" + property.qName() + "'}");
      }
      jpProp.schemaProps(toJPProperty(schema.jpProps(), schemas));
    }
    return jpProp.build();
  }

  private Map<String, JPPropertySchema> getSchemas(JPPropertySchema[] schemas) {
    return Arrays.stream(schemas)
        .collect(Collectors.toMap(JPPropertySchema::code, s -> s));
  }
}
