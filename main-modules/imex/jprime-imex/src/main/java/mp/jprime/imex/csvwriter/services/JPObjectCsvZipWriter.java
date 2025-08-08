package mp.jprime.imex.csvwriter.services;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPAttr;
import mp.jprime.parsers.ParserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Сервис выгрузки {@link JPObject} в архив CSV
 */
public class JPObjectCsvZipWriter extends JPCsvBaseWriter<JPObject> {
  /**
   * Парсер типов
   */
  private final ParserService parser;
  /**
   * Упорядоченная коллекция кодов атрибутов
   */
  private final Collection<String> attrCodes;
  /**
   * Выходной архивированный поток
   */
  private final ZipOutputStream os;
  /**
   * Имя файлов внутри архива
   */
  private final String filesName;
  /**
   * Массив названий атрибутов
   */
  private final String[] attrNames;
  /**
   * Признак необходимости заголовка
   */
  private final boolean needsHeaders;
  /**
   * Максимальное количество строк в файле внутри архива
   */
  private final int maxFileSize;
  /**
   * Счетчик файлов в архиве
   */
  private int batchCount = 1;
  /**
   * Количество строк записанных в текущий файл
   */
  private int currentFileSize = 0;

  private JPObjectCsvZipWriter(Builder builder) {
    super(builder.os, builder.settings);

    Assert.notNull(builder.parser, "ParserService must be not null");
    Assert.notEmpty(builder.attrs, "JPAttrs must be not empty");
    Assert.notNull(builder.filesName, "FilesName must be not null");

    this.os = builder.os;
    this.filesName = builder.filesName;
    this.parser = builder.parser;
    this.needsHeaders = builder.needsHeaders;
    this.maxFileSize = builder.maxFileSize == null ? 10000 : builder.maxFileSize;
    int attrsSize = builder.attrs.size();
    attrCodes = new ArrayList<>(attrsSize);
    attrNames = new String[attrsSize];
    int counter = 0;
    for (JPAttr attr : builder.attrs) {
      Assert.notNull(attr, "JPAttrs must not contain null elements");

      attrCodes.add(attr.getCode());
      attrNames[counter++] = attr.getName();
    }
    startNewFile();
  }

  /**
   * Преобразование коллекции входящих значений в массив строк и объявление начала нового файла в архиве
   */
  @Override
  protected Collection<String[]> toBatch(Collection<JPObject> values) {
    if (CollectionUtils.isEmpty(values)) {
      return Collections.emptyList();
    }

    List<String[]> batch = new ArrayList<>(values.size());
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

  @Override
  protected void writeAndFlush(Collection<String[]> batch) {
    List<String[]> listBatch = new ArrayList<>(batch);
    int batchSize = listBatch.size();
    currentFileSize += batchSize;
    while (currentFileSize > maxFileSize) {
      int diff = currentFileSize - maxFileSize;
      int lastLinesNumber = batchSize - diff;
      List<String[]> lastLines = listBatch.subList(0, lastLinesNumber);
      if (!lastLines.isEmpty()) {
        super.writeAndFlush(lastLines);
        listBatch = listBatch.subList(lastLinesNumber, batchSize);
      }
      startNewFile();
      currentFileSize = diff;
      batchSize = listBatch.size();
    }

    super.writeAndFlush(listBatch);
  }

  /**
   * Определяет в потоке новый файл и пишет в него заголовок, при необходимости
   */
  private void startNewFile() {
    try {
      os.putNextEntry(new ZipEntry(filesName + " (" + batchCount++ + ").csv"));
      if (needsHeaders) {
        csvWriter.writeNext(attrNames);
      }
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    }
  }

  /**
   * Построитель {@link JPObjectCsvZipWriter}
   *
   * @param os        Выходной поток для записи файла
   * @param parser    Парсер типов
   * @param attrs     <strong>Упорядоченная</strong> коллекция атрибутов
   * @param filesName Имя файлов внутри архива
   */
  public static Builder newBuilder(ZipOutputStream os, ParserService parser, Collection<JPAttr> attrs, String filesName) {
    return new Builder(os, parser, attrs, filesName);
  }

  /**
   * Построитель {@link JPObjectCsvZipWriter}
   */
  public static final class Builder {
    private final ZipOutputStream os;
    private JPCsvBaseWriterSettings settings;
    private boolean needsHeaders = true;
    private final ParserService parser;
    private final Collection<JPAttr> attrs;
    private final String filesName;
    private Integer maxFileSize;

    private Builder(ZipOutputStream os, ParserService parser, Collection<JPAttr> attrs, String filesName) {
      this.os = os;
      this.parser = parser;
      this.attrs = attrs;
      this.filesName = filesName;
    }

    /**
     * Настройки
     */
    public Builder settings(JPCsvBaseWriterSettings settings) {
      this.settings = settings;
      return this;
    }

    /**
     * Признак необходимости заголовка
     */
    public Builder needsHeaders(boolean needsHeaders) {
      this.needsHeaders = needsHeaders;
      return this;
    }

    /**
     * Максимальное количество строк в файле внутри архива
     */
    public Builder maxFileSize(Integer maxFileSize) {
      this.maxFileSize = maxFileSize;
      return this;
    }

    public JPObjectCsvZipWriter build() {
      return new JPObjectCsvZipWriter(this);
    }
  }
}
