package mp.jprime.meta.services;

import mp.jprime.imex.csvwriter.services.JPCsvBaseWriter;
import mp.jprime.io.JpPipedInputStream;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.meta.JPAttrCsvWriterService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.json.converters.JPClassJsonConverter;
import mp.jprime.parsers.ParserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Базовый сервис создания {@link JPAttrCsvWriter}
 */
@Service
public final class JPAttrCsvWriterCommonService implements JPAttrCsvWriterService {
  private final JPClassJsonConverter converter;
  private final JPJsonMapper mapper;
  private final ParserService parser;

  private JPAttrCsvWriterCommonService(@Autowired JPClassJsonConverter converter,
                                       @Autowired JPJsonMapper mapper,
                                       @Autowired ParserService parser) {
    this.converter = converter;
    this.mapper = mapper;
    this.parser = parser;
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
    return JpPipedInputStream.toInputStream(os -> {
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
