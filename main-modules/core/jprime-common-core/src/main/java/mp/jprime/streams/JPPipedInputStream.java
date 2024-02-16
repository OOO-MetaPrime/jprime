package mp.jprime.streams;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Реализация {@link PipedInputStream}  с закрытием связанного выходного потока при закрытии входного потока
 */
public class JPPipedInputStream extends PipedInputStream {
  private final PipedOutputStream os;

  public JPPipedInputStream(PipedOutputStream os) throws IOException {
    super(os);
    this.os = os;
  }

  /**
   * Закрывает выходной поток и входной поток
   */
  @Override
  public void close() {
    try {
      os.close();
    } catch (IOException ignored) {
    }
    try {
      super.close();
    } catch (IOException ignored) {
    }
  }

}
