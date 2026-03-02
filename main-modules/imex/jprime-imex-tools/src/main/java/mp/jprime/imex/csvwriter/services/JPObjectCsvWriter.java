package mp.jprime.imex.csvwriter.services;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.meta.JPAttr;
import mp.jprime.parsers.ParserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Логика выгрузки {@link JPObject} в CSV
 */
public class JPObjectCsvWriter extends JPCsvBaseWriter<JPObject> {
  /**
   * Парсер типов
   */
  private final ParserService parser;
  /**
   * Упорядоченная коллекция кодов атрибутов
   */
  private final Collection<String> attrCodes;

  private JPObjectCsvWriter(Builder builder) {
    super(builder.os, builder.settings);

    Assert.notNull(builder.parser, "ParserService must be not null");
    Assert.notEmpty(builder.attrs, "JPAttrs must be not empty");

    this.parser = builder.parser;
    int attrsSize = builder.attrs.size();
    attrCodes = new ArrayList<>(attrsSize);
    Map<String, String> attrTitles = builder.attrTitles != null ? builder.attrTitles : Collections.emptyMap();
    String[] attrNames = new String[attrsSize];
    int counter = 0;
    for (JPAttr attr : builder.attrs) {
      Assert.notNull(attr, "JPAttrs must not contain null elements");

      attrCodes.add(attr.getCode());
      attrNames[counter++] = attrTitles.getOrDefault(attr.getCode(), attr.getName());
    }
    if (builder.needsHeaders) {
      csvWriter.writeNext(attrNames);
    }
  }

  @Override
  protected Collection<String[]> toBatch(Collection<JPObject> values) {
    if (CollectionUtils.isEmpty(values)) {
      return Collections.emptyList();
    }

    Collection<String[]> batch = new ArrayList<>(values.size());
    int size = attrCodes.size();
    for (JPObject value : values) {
      if (value == null) {
        continue;
      }

      String[] line = new String[size];
      int counter = 0;
      for (String attr : attrCodes) {
        line[counter++] = parser.toString(value.getAttrValue(attr));
      }
      batch.add(line);
    }

    return batch;
  }

  /**
   * Построитель {@link JPObjectCsvZipWriter}
   *
   * @param os     Выходной поток для записи файла
   * @param parser Парсер типов
   * @param attrs  <strong>Упорядоченная</strong> коллекция атрибутов
   */
  public static Builder newBuilder(OutputStream os, ParserService parser, Collection<JPAttr> attrs) {
    return new Builder(os, parser, attrs);
  }

  public static Builder newBuilder(OutputStream os, ParserService parser, Collection<JPAttr> attrs, JPCsvBaseWriterSettings settings) {
    return new Builder(os, parser, attrs, settings);
  }

  /**
   * Построитель {@link JPObjectCsvWriter}
   */
  public static final class Builder {
    private final OutputStream os;
    private JPCsvBaseWriterSettings settings;
    private boolean needsHeaders = true;
    private final ParserService parser;
    private final Collection<JPAttr> attrs;
    private Map<String, String> attrTitles;

    private Builder(OutputStream os, ParserService parser, Collection<JPAttr> attrs) {
      this.os = os;
      this.parser = parser;
      this.attrs = attrs;
    }

    private Builder(OutputStream os, ParserService parser, Collection<JPAttr> attrs, JPCsvBaseWriterSettings settings) {
      this.os = os;
      this.parser = parser;
      this.attrs = attrs;
      this.settings = settings;
    }

    /**
     * Признак необходимости заголовка
     */
    public Builder needsHeaders(boolean needsHeaders) {
      this.needsHeaders = needsHeaders;
      return this;
    }

    /**
     * Настройки
     */
    public Builder settings(JPCsvBaseWriterSettings settings) {
      this.settings = settings;
      return this;
    }

    /**
     * Заголовки атрибутов для первой строки заголовков
     */
    public Builder attrTitles(Map<String, String> attrTitles) {
      this.attrTitles = attrTitles;
      return this;
    }

    public JPObjectCsvWriter build() {
      return new JPObjectCsvWriter(this);
    }
  }
}
