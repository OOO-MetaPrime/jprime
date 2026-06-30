package mp.jprime.io;

import mp.jprime.concurrent.JPCompletableFuture;
import mp.jprime.exceptions.JPRuntimeException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.Consumer;

/**
 * JPPipedInputStream
 */
public final class JpPipedInputStream {
  private static final Logger LOG = LoggerFactory.getLogger(JpPipedInputStream.class);

  public static InputStream toInputStream(Consumer<OutputStream> func) {
    try {
      PipedInputStream is = new PipedInputStream();
      PipedOutputStream os = new PipedOutputStream(is);

      JPCompletableFuture.runAsync(() -> {
            try {
              func.accept(os);
            } catch (Exception e) {
              LOG.error(e.getMessage(), e);
            } finally {
              IOUtils.closeQuietly(os);
            }
          }
      );

      return is;
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    }
  }
}
