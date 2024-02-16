package mp.jprime.streams;

import java.io.IOException;
import java.io.PipedOutputStream;

/**
 * Реализация {@link PipedOutputStream} с возможностью получения инФормации о закрытии потока
 */
public class JPPipedOutputStream extends PipedOutputStream {

  private volatile boolean closed = false;

  public JPPipedOutputStream() {
    super();
  }

  @Override
  public void write(int b) throws IOException {
    if (!closed) {
      super.write(b);
    }
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (!closed) {
      super.write(b, off, len);
    }
  }

  @Override
  public void flush() throws IOException {
    if (!closed) {
      super.flush();
    }
  }

  @Override
  public void close() {
    closed = true;
    try {
      super.close();
    } catch (IOException ignored) {
    }
  }

  /**
   * Признак закрытия потока
   */
  public boolean isClosed() {
    return closed;
  }
}
