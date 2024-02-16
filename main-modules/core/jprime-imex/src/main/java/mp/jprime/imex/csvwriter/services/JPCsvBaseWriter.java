package mp.jprime.imex.csvwriter.services;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvwriter.JPCsvWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * Базовый сервис выгрузки данных в CSV
 */
public abstract class JPCsvBaseWriter<T> implements JPCsvWriter<T> {
  protected final CSVWriter csvWriter;

  /**
   * Конструктор {@link JPCsvBaseWriter}
   *
   * @param os       Выходной поток для записи файла
   * @param settings Настройки
   */
  protected JPCsvBaseWriter(OutputStream os, JPCsvBaseWriterSettings settings) {
    Assert.notNull(os, "OutputStream must be not null");

    OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    if (settings == null) {
      this.csvWriter = new CSVWriter(writer);
    } else {
      this.csvWriter = new CSVWriter(
          writer,
          settings.separator == null ? ICSVWriter.DEFAULT_SEPARATOR : settings.separator,
          settings.quoteChar == null ? ICSVWriter.DEFAULT_QUOTE_CHARACTER : settings.quoteChar,
          settings.escapeChar == null ? ICSVWriter.DEFAULT_ESCAPE_CHARACTER : settings.escapeChar,
          settings.lineEnd == null ? ICSVWriter.DEFAULT_LINE_END : settings.lineEnd
      );
    }
  }

  @Override
  public void write(Collection<T> values) {
    if (CollectionUtils.isEmpty(values)) {
      return;
    }

    Collection<String[]> batch = toBatch(values);

    if (CollectionUtils.isEmpty(batch)) {
      return;
    }

    writeAndFlush(batch);
  }

  /**
   * Записывает и очищает буфер
   *
   * @param batch Данные для записи
   */
  protected void writeAndFlush(Collection<String[]> batch) {
    csvWriter.writeAll(batch);
    try {
      csvWriter.flush();
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    }
  }

  /**
   * Преобразование коллекции входящих значений в коллекцию строк
   *
   * @param values Входящие значения
   * @return Коллекция строк
   */
  protected abstract Collection<String[]> toBatch(Collection<T> values);

  /**
   * Очистка буфера и закрытие потока записи
   */
  @Override
  public void close() {

    try {
      csvWriter.flush();
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    } finally {
      IOUtils.closeQuietly(csvWriter);
    }
  }

  /**
   * Настройки базового сервиса выгрузки данных в CSV
   */
  public static class JPCsvBaseWriterSettings {
    /**
     * Разделитель
     */
    private final Character separator;
    /**
     * Символ кавычек
     */
    private final Character quoteChar;
    /**
     * Символ для экранирования кавычек
     */
    private final Character escapeChar;
    /**
     * Окончание строки
     */
    private final String lineEnd;

    private JPCsvBaseWriterSettings(Builder builder) {
      separator = builder.separator;
      quoteChar = builder.quoteChar;
      escapeChar = builder.escapeChar;
      lineEnd = builder.lineEnd;
    }

    /**
     * Построитель {@link JPCsvBaseWriterSettings}
     */
    public static Builder newBuilder() {
      return new Builder();
    }

    /**
     * Построитель {@link JPCsvBaseWriterSettings}
     */
    public static final class Builder {
      private Character separator;
      private Character quoteChar;
      private Character escapeChar;
      private String lineEnd;

      private Builder() {
      }

      /**
       * Разделитель
       *
       * @param separator Разделитель
       * @return {@link Builder}
       */
      public Builder separator(Character separator) {
        this.separator = separator;
        return this;
      }

      /**
       * Символ кавычек
       *
       * @param quoteChar Символ кавычек
       * @return {@link Builder}
       */
      public Builder quoteChar(Character quoteChar) {
        this.quoteChar = quoteChar;
        return this;
      }

      /**
       * Символ для экранирования кавычек
       *
       * @param escapeChar Символ для экранирования кавычек
       * @return {@link Builder}
       */
      public Builder escapeChar(Character escapeChar) {
        this.escapeChar = escapeChar;
        return this;
      }

      /**
       * Окончание строки
       *
       * @param lineEnd Окончание строки
       * @return {@link Builder}
       */
      public Builder lineEnd(String lineEnd) {
        this.lineEnd = lineEnd;
        return this;
      }

      /**
       * Построить {@link JPCsvBaseWriterSettings}
       *
       * @return {@link JPCsvBaseWriterSettings}
       */
      public JPCsvBaseWriterSettings build() {
        return new JPCsvBaseWriterSettings(this);
      }
    }
  }
}
