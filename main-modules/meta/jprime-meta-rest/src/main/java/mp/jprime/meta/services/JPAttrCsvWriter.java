package mp.jprime.meta.services;

import mp.jprime.meta.json.beans.JsonJPGeometry;
import mp.jprime.meta.json.beans.JsonJPMoney;
import mp.jprime.imex.csvwriter.services.JPCsvBaseWriter;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.meta.*;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.json.converters.JPClassJsonConverter;
import mp.jprime.parsers.ParserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Сервис выгрузки {@link JPAttr} в CSV
 */
public class JPAttrCsvWriter extends JPCsvBaseWriter<JPAttr> {
  private static final String[] HEADERS = {
      "Кодовое имя",
      "Название",
      "Тип",
      "Настройки доступа",
      "Код класса",
      "Идентификатор/гуид",
      "Признак идентификатора",
      "Признак обязательности",
      "Признак обновляемости значения",
      "Длина",
      "Короткое название",
      "Описание",
      "Уникальный qName",
      "Код класса, на который ссылается",
      "Код атрибута, на который ссылается",
      "Описание файла",
      "Описание простой дроби",
      "Описание денежного типа",
      "Описание пространственных данных",
      "Описание виртуальной ссылки",
      "Тип значения",
      "Код атрибута, содержащего подпись",
  };

  private final JPClassJsonConverter classConverter;
  private final JPJsonMapper mapper;
  private final ParserService parser;

  private JPAttrCsvWriter(OutputStream os, JPCsvBaseWriterSettings settings, boolean needsHeaders, JPClassJsonConverter classConverter,
                          JPJsonMapper mapper, ParserService parser) {
    super(os, settings);
    Assert.notNull(classConverter, "JpJsonJpClassConverter must be not null");
    Assert.notNull(mapper, "JPJsonMapper must be not null");
    Assert.notNull(parser, "ParserService must be not null");

    this.parser = parser;
    this.classConverter = classConverter;
    this.mapper = mapper;
    if (needsHeaders) {
      csvWriter.writeNext(HEADERS);
    }
  }

  /**
   * Создает экземпляр {@link JPAttrCsvWriter}
   *
   * @param os             Выходной поток для записи файла
   * @param settings       Настройки
   * @param needsHeaders   Признак необходимости указания заголовков
   * @param classConverter Json конвертер меты
   * @param mapper         Базовый класс JSON-обработчиков
   * @param parser         Парсер
   * @return {@link JPAttrCsvWriter}
   */
  public static JPAttrCsvWriter of(OutputStream os, JPCsvBaseWriterSettings settings, boolean needsHeaders,
                                   JPClassJsonConverter classConverter, JPJsonMapper mapper, ParserService parser) {
    return new JPAttrCsvWriter(os, settings, needsHeaders, classConverter, mapper, parser);
  }

  /**
   * Создает экземпляр {@link JPAttrCsvWriter} используя стандартные настройки
   *
   * @param os             Выходной поток для записи файла
   * @param needsHeaders   Признак необходимости указания заголовков
   * @param classConverter Json конвертер меты
   * @param mapper         Базовый класс JSON-обработчиков
   * @param parser         Парсер
   * @return {@link JPAttrCsvWriter}
   */
  public static JPAttrCsvWriter of(OutputStream os, boolean needsHeaders, JPClassJsonConverter classConverter,
                                   JPJsonMapper mapper, ParserService parser) {
    return new JPAttrCsvWriter(os, null, needsHeaders, classConverter, mapper, parser);
  }

  @Override
  protected Collection<String[]> toBatch(Collection<JPAttr> values) {
    if (CollectionUtils.isEmpty(values)) {
      return Collections.emptyList();
    }

    Collection<String[]> batch = new ArrayList<>(values.size());
    for (JPAttr value : values) {
      if (value == null) {
        continue;
      }

      String[] line = new String[23];
      line[0] = value.getCode();
      line[1] = value.getName();
      line[2] = jpTypeToString(value.getType());
      line[3] = value.getJpPackage();
      line[4] = value.getJpClassCode();
      line[5] = value.getGuid();
      line[6] = parser.toString(value.isIdentifier());
      line[7] = parser.toString(value.isMandatory());
      line[8] = parser.toString(value.isUpdatable());
      Integer length = value.getLength();
      line[9] = length == null ? null : parser.toString(length);
      line[10] = value.getShortName();
      line[11] = value.getDescription();
      line[12] = value.getQName();
      line[13] = value.getRefJpClass();
      line[14] = value.getRefJpAttr();
      line[15] = refJpFileToString(value.getRefJpFile());
      line[16] = simpleFractionToString(value.getSimpleFraction());
      line[17] = moneyToString(value.getMoney());
      line[18] = geometryToString(value.getGeometry());
      line[19] = virtualReferenceToString(value.getVirtualReference());
      line[20] = jpTypeToString(value.getValueType());
      line[21] = value.getSignAttrCode();
      batch.add(line);
    }

    return batch;
  }

  private String refJpFileToString(JPFile refJpFile) {
    if (refJpFile == null) {
      return null;
    }

    return mapper.toString(classConverter.toJson(refJpFile));
  }

  private String simpleFractionToString(JPSimpleFraction simpleFraction) {
    if (simpleFraction == null) {
      return null;
    }

    return mapper.toString(classConverter.toJson(simpleFraction));
  }

  private String moneyToString(JPMoney money) {
    if (money == null) {
      return null;
    }

    return mapper.toString(JsonJPMoney.toJson(money));
  }

  private String geometryToString(JPGeometry geometry) {
    if (geometry == null) {
      return null;
    }

    return mapper.toString(JsonJPGeometry.toJson(geometry));
  }

  private String virtualReferenceToString(JPVirtualPath reference) {
    if (reference == null) {
      return null;
    }

    return mapper.toString(classConverter.toJson(reference));
  }

  private String jpTypeToString(JPType type) {
    if (type == null) {
      return null;
    }

    return type.getCode();
  }
}
