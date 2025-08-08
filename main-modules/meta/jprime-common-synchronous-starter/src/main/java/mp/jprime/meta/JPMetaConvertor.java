package mp.jprime.meta;

import mp.jprime.meta.annotations.JPGeometry;
import mp.jprime.meta.annotations.JPMoney;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.beans.*;

/**
 * Конверторы мета-бинов
 */
public final class JPMetaConvertor {
  /**
   * Создание
   *
   * @param attr mp.jprime.metamaps.annotations.JPAttr
   * @return JPAttr
   */
  public static JPAttr of(String jpClassCode, mp.jprime.meta.annotations.JPAttr attr) {
    String code = attr.code();
    JPType type = attr.type();

    JPStringFormat stringFormat = attr.stringFormat();
    String stringMask = attr.stringMask();
    mp.jprime.meta.annotations.JPFile jpFile = attr.refJpFile();
    mp.jprime.meta.annotations.JPSimpleFraction simpleFraction = attr.simpleFraction();
    JPMoney money = attr.money();
    JPGeometry geometry = attr.geometry();

    return JPAttrBean.newBuilder()
        .guid(attr.guid())
        .type(type)
        .stringFormat(stringFormat != JPStringFormat.NONE ? stringFormat : null)
        .stringMask(stringMask != null && !stringMask.isBlank() ? stringMask : null)
        .length(attr.length())
        .identifier(attr.identifier())
        .mandatory(attr.mandatory())
        .qName(attr.qName())
        .jpPackage(attr.jpPackage())
        .name(attr.name())
        .shortName(attr.shortName())
        .description(attr.description())
        .code(code)
        .jpClassCode(jpClassCode)
        // Настройка ссылки класс+атрибут
        .refJpClass(attr.refJpClass())
        .refJpAttr(attr.refJpAttr())
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
            type != JPType.MONEY ? null : JPMoneyBean.of(money.currency())
        )
        // Настройка пространственных данных
        .geometry(
            type != JPType.GEOMETRY ? null :
                JPGeometryBean.newBuilder()
                    .srid(geometry.SRID())
                    .build()
        )
        .signAttrCode(attr.signAttrCode())
        .build();
  }
}
