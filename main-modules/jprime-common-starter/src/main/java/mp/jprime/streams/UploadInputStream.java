package mp.jprime.streams;

import mp.jprime.exceptions.JPRuntimeException;

import java.io.InputStream;
import java.io.SequenceInputStream;

/**
 * Стрим апендер
 */
public class UploadInputStream implements AutoCloseable {
  private final String name;
  private InputStream is;

  public UploadInputStream(String name) {
    this.name = name;
  }


  public UploadInputStream() {
    this.name = null;
  }

  public void collectInputStream(InputStream is) {
    if (this.is == null) {
      this.is = is;
    } else {
      this.is = new SequenceInputStream(this.is, is);
    }
  }

  public InputStream getInputStream() {
    return this.is;
  }

  public String getName() {
    return name;
  }

  @Override
  public void close() {
    try {
      if (is != null) {
        is.close();
      }
    } catch (Exception e) {
      throw new JPRuntimeException(e.getMessage(), e);
    }
  }
}
