package mp.jprime.meta.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.beans.PropertyType;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaLoader;
import mp.jprime.meta.JPProperty;
import mp.jprime.meta.beans.*;
import mp.jprime.meta.xmlloader.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Загрузка метаинформации из xml
 */
@Service
public class JPMetaXmlLoader implements JPMetaLoader {
  private final Collection<JPMetaXmlResources> resources;

  public JPMetaXmlLoader(@Autowired(required = false) Collection<JPMetaXmlResources> resources) {
    this.resources = resources;
  }

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Flux<Collection<JPClass>> load() {
    return Mono.fromCallable(this::xmlLoad).flux();
  }

  private Collection<JPClass> xmlLoad() {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPClass> result = new ArrayList<>();
    for (JPMetaXmlResources resource : resources) {
      result.addAll(xmlLoad(resource.getResources()));
    }
    return result;
  }

  private Collection<JPClass> xmlLoad(Collection<Resource> resources) {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      Collection<JPClass> result = new ArrayList<>();

      for (Resource res : resources) {
        try (InputStream is = res.getInputStream()) {
          XmlJpClasses xmlJpClasses = new XmlMapper().readValue(is, XmlJpClasses.class);
          if (xmlJpClasses == null || xmlJpClasses.getJpClasses() == null) {
            continue;
          }
          for (XmlJpClass cls : xmlJpClasses.getJpClasses()) {
            XmlJpAttrs bAttrs = cls.getJpAttrs();
            XmlJpAttr[] attrs = bAttrs != null ? bAttrs.getJpAttrs() : null;
            if (attrs == null) {
              continue;
            }
            Collection<JPAttr> newAttrs = new ArrayList<>(attrs.length);
            for (XmlJpAttr attr : attrs) {
              String name = attr.getName();
              String descr = attr.getDescription();
              XmlJpFile jpFile = attr.getRefJpFile();

              newAttrs.add(JPAttrBean.newBuilder()
                  .guid(attr.getGuid())
                  .type(attr.getType())
                  .length(attr.getLength())
                  .identifier(attr.isIdentifier())
                  .mandatory(attr.isMandatory())
                  .qName(attr.getqName())
                  .jpPackage(attr.getJpPackage())
                  .name(name != null ? name : descr)
                  .shortName(attr.getShortName())
                  .description(descr)
                  .code(attr.getCode())
                  .jpClassCode(cls.getGuid())
                  .refJpClassCode(attr.getRefJpClass())
                  .refJpAttrCode(attr.getRefJpAttr())
                  .virtualReference(
                      JPVirtualPathBean.newInstance(
                          attr.getVirtualReference(), attr.getVirtualType()
                      )
                  )
                  .refJpFile(
                      jpFile == null || jpFile.getStorageCode() == null || jpFile.getStorageFilePath() == null ||
                          jpFile.getStorageCode().isEmpty() || jpFile.getStorageFilePath().isEmpty() ? null :
                          JPFileBean.newBuilder()
                              .storageCode(jpFile.getStorageCode())
                              .storageFilePath(jpFile.getStorageFilePath())
                              .storageCodeAttrCode(jpFile.getStorageCodeAttrCode())
                              .storageFilePathAttrCode(jpFile.getStorageFilePathAttrCode())
                              .fileTitleAttrCode(jpFile.getFileTitleAttrCode())
                              .fileExtAttrCode(jpFile.getFileExtAttrCode())
                              .fileSizeAttrCode(jpFile.getFileSizeAttrCode())
                              .fileDateAttrCode(jpFile.getFileDateAttrCode())
                              .build()
                  )
                  .schemaProps(toJPProperty(attr.getSchemaProps()))
                  .build());
            }
            String name = cls.getName();
            String shortName = cls.getShortName();
            String descr = cls.getDescription();

            JPClass newCls = JPClassBean.newBuilder()
                .guid(cls.getGuid())
                .code(cls.getCode())
                .qName(cls.getqName())
                .pluralCode(cls.getPluralCode())
                .jpPackage(cls.getJpPackage())
                .inner(cls.isInner())
                .actionLog(cls.getActionLog() == null || cls.getActionLog())
                .name(name != null ? name : descr)
                .shortName(shortName)
                .description(descr)
                .shortName(cls.getShortName())
                .description(cls.getDescription())
                .attrs(newAttrs)
                .build();

            result.add(newCls);
          }
        }
      }
      return result;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private Collection<JPProperty> toJPProperty(XmlJpProps schemaProps) {
    if (schemaProps != null && schemaProps.getJpProperties() != null) {
      return Arrays.stream(schemaProps.getJpProperties()).map(this::toJPProperty).collect(Collectors.toList());
    } else {
      return null;
    }
  }

  private JPProperty toJPProperty(XmlJpProperty property) {
    return JPPropertyBean.builder()
        .code(property.getCode())
        .type(PropertyType.getType(property.getType()))
        .length(property.getLength())
        .multiple(property.isMultiple())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .shortName(property.getShortName())
        .description(property.getDescription())
        .qName(property.getqName())
        .refJpClassCode(property.getRefJpClassCode())
        .refJpAttrCode(property.getRefJpAttrCode())
        .schemaProps(toJPProperty(property.getSchemaProps()))
        .build();
  }
}