package mp.jprime.streams;

import java.io.InputStream;
import java.io.SequenceInputStream;

/**
 * Стрим апендер
 */
public class UploadInputStream {
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
}
