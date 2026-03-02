package mp.jprime.imex.csvwriter.services;

import mp.jprime.concurrent.JPCompletableFuture;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.streams.JPPipedInputStream;
import mp.jprime.streams.JPPipedOutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

/**
 * Выгрузка строк в CSV
 */
public class JPStringCsvWriter extends JPCsvBaseWriter<String[]> {
  private static final Logger LOG = LoggerFactory.getLogger(JPStringCsvWriter.class);

  private JPStringCsvWriter(OutputStream os, JPCsvBaseWriterSettings settings) {
    super(os, settings);
  }

  public static InputStream writeCsv(Collection<String[]> values) {
    return writeCsv(values, null);
  }

  public static InputStream writeCsv(Collection<String[]> values, JPCsvBaseWriterSettings settings) {
    try {
      JPPipedOutputStream os = new JPPipedOutputStream();
      JPPipedInputStream is = new JPPipedInputStream(os);
      JPCompletableFuture.runAsync(() -> {
        try (JPStringCsvWriter writer = new JPStringCsvWriter(os, settings)) {
          writer.write(values);
        } catch (Exception e) {
          LOG.error("CSV write error", e);
          throw new JPRuntimeException(e);
        } finally {
          IOUtils.closeQuietly(os);
        }
      });
      return is;
    } catch (Exception e) {
      throw new JPRuntimeException(e);
    }
  }

  @Override
  protected Collection<String[]> toBatch(Collection<String[]> values) {
    return values;
  }

}
