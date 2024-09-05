package mp.jprime.meta.json.converters;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.meta.*;
import mp.jprime.meta.beans.JPStringFormat;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовый json конвертер меты
 */
@Service
public class JPClassJsonBaseConverter implements JPClassJsonConverter {
  @Override
  public JsonJPClass toJson(JPClass jpClass) {
    if (jpClass == null) {
      return null;
    }

    return JsonJPClass.newBuilder()
        .code(jpClass.getCode())
        .guid(jpClass.getGuid())
        .qName(jpClass.getQName())
        .tags(jpClass.getTags())
        .name(jpClass.getName())
        .shortName(jpClass.getShortName())
        .description(jpClass.getDescription())
        .jpPackage(jpClass.getJpPackage())
        .immutable(jpClass.isImmutable())
        .attrs(
            jpClass
                .getAttrs()
                .stream()
                .map(this::toJson)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        )
        .build();
  }

  @Override
  public JsonJPAttr toJson(JPAttr jpAttr) {
    if (jpAttr == null) {
      return null;
    }

    JPType type = jpAttr.getValueType();
    if (type == null) {
      return null;
    }

    JPFile jpFile = jpAttr.getRefJpFile();
    JPSimpleFraction simpleFraction = jpAttr.getSimpleFraction();
    JPMoney money = jpAttr.getMoney();
    JPGeometry geometry = jpAttr.getGeometry();
    return JsonJPAttr.newBuilder()
        .code(jpAttr.getCode())
        .guid(jpAttr.getGuid())
        .qName(jpAttr.getQName())
        .name(jpAttr.getName())
        .shortName(jpAttr.getShortName())
        .description(jpAttr.getDescription())
        .jpPackage(jpAttr.getJpPackage())
        .identifier(jpAttr.isIdentifier())
        .mandatory(jpAttr.isMandatory())
        .type(type.getCode())
        .updatable(jpAttr.isUpdatable())
        .length(jpAttr.getLength())
        // Настройка ссылки класс+атрибут
        .refJpClass(jpAttr.getRefJpClass())
        .refJpAttr(jpAttr.getRefJpAttr())
        // Код атрибута-подписи
        .signAttr(jpAttr.getSignAttrCode())
        // Настройка файла
        .refJpFile(type == JPType.FILE ? toJson(jpFile) : null)
        // Настройка простой дроби
        .simpleFraction(type == JPType.SIMPLEFRACTION ? toJson(simpleFraction) : null)
        // Настройка денежного типа
        .money(type == JPType.MONEY ? toJson(money) : null)
        // Настройка геометрии
        .geometry(type == JPType.GEOMETRY ? toJson(geometry) : null)
        // Свойства псевдо-меты
        .schemaProps(toJsonJPProperty(jpAttr.getSchemaProps()))
        .build();
  }

  @Override
  public JsonJPFile toJson(JPFile jpFile) {
    return jpFile == null ? null :
        JsonJPFile.newBuilder()
            .titleAttr(jpFile.getFileTitleAttrCode())
            .extAttr(jpFile.getFileExtAttrCode())
            .sizeAttr(jpFile.getFileSizeAttrCode())
            .dateAttr(jpFile.getFileDateAttrCode())
            .infoAttr(jpFile.getFileInfoAttrCode())
            .fileStampAttr(jpFile.getFileStampAttrCode())
            .build();
  }

  @Override
  public JsonJPSimpleFraction toJson(JPSimpleFraction simpleFraction) {
    return simpleFraction == null ? null :
        JsonJPSimpleFraction.newBuilder()
            .integerAttr(simpleFraction.getIntegerAttrCode())
            .denominatorAttr(simpleFraction.getDenominatorAttrCode())
            .build();
  }

  @Override
  public JsonJPMoney toJson(JPMoney money) {
    return money == null ? null :
        JsonJPMoney.newBuilder()
            .currencyCode(money.getCurrencyCode())
            .build();
  }

  @Override
  public JsonJPGeometry toJson(JPGeometry geometry) {
    return geometry == null ? null : JsonJPGeometry.newBuilder()
        .srid(geometry.getSRID())
        .build();
  }

  @Override
  public JsonJPVirtualPath toJson(JPVirtualPath virtualPath) {
    if (virtualPath == null) {
      return null;
    } else {
      JPType virtualPathType = virtualPath.getType();
      return JsonJPVirtualPath.newBuilder()
          .virtualType(virtualPathType != null ? virtualPathType.getCode() : null)
          .targetAttrCode(virtualPath.getTargerAttrCode())
          .refAttrCode(virtualPath.getRefAttrCode())
          .build();
    }
  }

  @Override
  public Collection<JsonJPProperty> toJsonJPProperty(Collection<JPProperty> properties) {
    return properties == null || properties.isEmpty() ? null :
        properties.stream().map(this::toJsonJPProperty).collect(Collectors.toList());
  }

  private JsonJPProperty toJsonJPProperty(JPProperty property) {
    JPPropertyType type = property.getType();
    JPStringFormat stringFormat = property.getStringFormat();

    return JsonJPProperty.builder()
        .code(property.getCode())
        .type(type != null ? type.getCode() : null)
        .stringFormat(stringFormat != null ? stringFormat.getCode() : null)
        .stringMask(property.getStringMask())
        .length(property.getLength())
        .mandatory(property.isMandatory())
        .name(property.getName())
        .qName(property.getQName())
        .refJpClass(property.getRefJpClass())
        .refJpAttr(property.getRefJpAttr())
        .jpProps(toJsonJPProperty(property.getJpProps()))
        .build();
  }
}
