package mp.jprime.meta.services;

import mp.jprime.imex.csvwriter.services.JPCsvBaseWriter;
import mp.jprime.io.JPPipedInputStream;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.meta.JPAttrCsvWriterService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.json.converters.JPClassJsonConverter;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Базовый сервис создания {@link JPAttrCsvWriter}
 */
@Service
public final class JPAttrCsvWriterCommonService implements JPAttrCsvWriterService, ParserServiceAware {
  private JPClassJsonConverter converter;
  private JPJsonMapper mapper;
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
    return JPPipedInputStream.toInputStream(os -> {
      try (JPAttrCsvWriter writer = JPAttrCsvWriter.of(
          os,
          settings,
          true,
          converter,
          mapper,
          parser
      )) {
        writer.write(jpClass.getAttrs());
      } finally {
        IOUtils.closeQuietly(os);
      }
    });
  }
}
