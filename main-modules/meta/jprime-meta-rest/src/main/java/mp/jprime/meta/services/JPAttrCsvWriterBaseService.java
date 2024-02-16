package mp.jprime.meta.services;

import mp.jprime.concurrent.JPForkJoinPoolService;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvwriter.services.JPCsvBaseWriter;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.meta.JPAttrCsvWriterService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPClassJsonConverter;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Базовый сервис создания {@link JPAttrCsvWriter}
 */
@Service
public class JPAttrCsvWriterBaseService implements JPAttrCsvWriterService, ParserServiceAware {
  private static final Logger LOG = LoggerFactory.getLogger(JPAttrCsvWriterBaseService.class);
  /**
   * Конвертер JpClass
   */
  private JPClassJsonConverter converter;
  /**
   * Базовый класс JSON-обработчиков
   */
  private JPJsonMapper mapper;
  /**
   * Парсер типов
   */
  private ParserService parser;

  @Override
  public void setParserService(ParserService parserService) {
    this.parser = parserService;
  }

  @Autowired
  private void setConverter(JPClassJsonConverter converter) {
    this.converter = converter;
  }

  @Autowired
  private void setMapper(JPJsonMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public InputStream of(JPClass jpClass, String lineEnd) {
    return getInputStream(JPCsvBaseWriter.JPCsvBaseWriterSettings.newBuilder().lineEnd(lineEnd).build(), jpClass);
  }

  @Override
  public InputStream of(JPClass jpClass) {
    return getInputStream(null, jpClass);
  }

  private InputStream getInputStream(JPCsvBaseWriter.JPCsvBaseWriterSettings settings, JPClass jpClass) {
    if (jpClass == null) {
      return InputStream.nullInputStream();
    }
    try {
      PipedInputStream is = new PipedInputStream();
      PipedOutputStream os = new PipedOutputStream(is);

      CompletableFuture.runAsync(() -> {
            try (JPAttrCsvWriter writer = JPAttrCsvWriter.of(
                os,
                settings,
                true,
                converter,
                mapper,
                parser
            )) {
              writer.write(jpClass.getAttrs());
            } catch (Exception e) {
              LOG.error("Exception in JPAttrCsvWriterBaseService with classCode \"{}\"", jpClass.getCode(), e);
            } finally {
              IOUtils.closeQuietly(os);
            }
          },
          JPForkJoinPoolService.pool());

      return is;
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    }
  }
}
