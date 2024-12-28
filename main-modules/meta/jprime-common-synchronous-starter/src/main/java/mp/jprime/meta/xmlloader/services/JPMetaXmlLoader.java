package mp.jprime.meta.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.beans.JPPropertyType;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public Collection<JPClass> load() {
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
            String code = attr.getCode();
            String name = attr.getName();
            JPType type = JPType.getType(attr.getType());

            String descr = attr.getDescription();
            XmlJpFile jpFile = attr.getRefJpFile();
            XmlJpSimpleFraction simpleFraction = attr.getSimpleFraction();
            XmlJpMoney money = attr.getMoney();
            XmlJpGeometry geometry = attr.getGeometry();

            if (type == null) {
              continue;
            }

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
                .code(code)
                .jpClassCode(cls.getCode())
                // Настройка ссылки класс+атрибут
                .refJpClass(attr.getRefJpClass())
                .refJpAttr(attr.getRefJpAttr())
                // Настройка виртуальной ссылки
                .virtualReference(
                    JPVirtualPathBean.newInstance(
                        attr.getVirtualReference(), attr.getVirtualType()
                    )
                )
                // Настройка файла
                .refJpFile(
                    type != JPType.FILE || jpFile == null ||
                        jpFile.getStorageCode() == null || jpFile.getStorageFilePath() == null ||
                        jpFile.getStorageCode().isEmpty() || jpFile.getStorageFilePath().isEmpty() ? null :
                        JPFileBean.newBuilder(code)
                            .storageCode(jpFile.getStorageCode())
                            .storageFilePath(jpFile.getStorageFilePath())
                            .storageCodeAttrCode(jpFile.getStorageCodeAttrCode())
                            .storageFilePathAttrCode(jpFile.getStorageFilePathAttrCode())
                            .fileTitleAttrCode(jpFile.getFileTitleAttrCode())
                            .fileExtAttrCode(jpFile.getFileExtAttrCode())
                            .fileSizeAttrCode(jpFile.getFileSizeAttrCode())
                            .fileDateAttrCode(jpFile.getFileDateAttrCode())
                            .fileInfoAttrCode(jpFile.getFileInfoAttrCode())
                            .fileStampAttrCode(jpFile.getFileStampAttrCode())
                            .build()
                )
                // Настройка простой дроби
                .simpleFraction(
                    type != JPType.SIMPLEFRACTION || simpleFraction == null ? null :
                        JPSimpleFractionBean.newBuilder(code)
                            .integerAttrCode(simpleFraction.getIntegerAttrCode())
                            .denominatorAttrCode(simpleFraction.getDenominatorAttrCode())
                            .build()
                )
                // Настройка денежного типа
                .money(
                    type != JPType.MONEY || money == null ? null : JPMoneyBean.of(money.getCurrencyCode())
                )
                // Настройка пространственных данных
                .geometry(
                    type != JPType.GEOMETRY || geometry == null ? null :
                        JPGeometryBean.newBuilder()
                            .srid(geometry.getSRID())
                            .build()
                )
                // Свойства псевдо-меты
                .schemaProps(
                    toJPProperty(attr.getSchemaProps())
                )
                .signAttrCode(attr.getSignAttrCode())
                .build());
          }
          String name = cls.getName();
          String shortName = cls.getShortName();
          String descr = cls.getDescription();
          Collection<String> tags = Collections.emptyList();
          if (cls.getTags() != null && cls.getTags().getTags() != null) {
            tags = Stream.of(cls.getTags().getTags())
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
          }

          JPClass newCls = JPClassBean.newBuilder()
              .guid(cls.getGuid())
              .code(cls.getCode())
              .qName(cls.getqName())
              .jpPackage(cls.getJpPackage())
              .inner(cls.isInner())
              .actionLog(cls.getActionLog() == null || cls.getActionLog())
              .name(name != null ? name : descr)
              .shortName(shortName)
              .description(descr)
              .shortName(cls.getShortName())
              .description(cls.getDescription())
              .attrs(newAttrs)
              .tags(tags)
              .build();

          result.add(newCls);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException("Unable to load xml meta resource: " + Objects.toString(res.getFilename(), "unknown"), e);
      }
    }
    return result;
  }

  private Collection<JPProperty> toJPProperty(XmlJpProps schemaProps) {
    if (schemaProps != null && schemaProps.getJpProperties() != null) {
      return Arrays.stream(schemaProps.getJpProperties())
          .map(this::toJPProperty)
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }

  private JPProperty toJPProperty(XmlJpProperty property) {
    return JPPropertyBean.builder()
        .code(property.getCode())
        .type(JPPropertyType.getType(property.getType()))
        .length(property.getLength())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .qName(property.getqName())
        .refJpClass(property.getRefJpClass())
        .refJpAttr(property.getRefJpAttr())
        .jpProps(toJPProperty(property.getSchemaProps()))
        .build();
  }
}