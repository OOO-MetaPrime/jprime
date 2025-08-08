package mp.jprime.meta.json.converters;

import mp.jprime.meta.json.beans.JsonJPGeometry;
import mp.jprime.meta.json.beans.JsonJPMoney;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.*;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.beans.*;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовый json конвертер меты
 */
@Service
public class JPClassJsonConverter {
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

  public JsonJPAttr toJson(JPAttr jpAttr) {
    if (jpAttr == null) {
      return null;
    }

    JPType type = jpAttr.getValueType();
    if (type == null) {
      return null;
    }
    JPStringFormat stringFormat = jpAttr.getStringFormat();
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
        .stringFormat(stringFormat != null ? stringFormat.getCode() : null)
        .stringMask(jpAttr.getStringMask())
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
        .money(type == JPType.MONEY ? JsonJPMoney.toJson(money) : null)
        // Настройка геометрии
        .geometry(type == JPType.GEOMETRY ? JsonJPGeometry.toJson(geometry) : null)
        .build();
  }

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

  public JsonJPSimpleFraction toJson(JPSimpleFraction simpleFraction) {
    return simpleFraction == null ? null :
        JsonJPSimpleFraction.newBuilder()
            .integerAttr(simpleFraction.getIntegerAttrCode())
            .denominatorAttr(simpleFraction.getDenominatorAttrCode())
            .build();
  }


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
}
