package mp.jprime.streams;

import mp.jprime.exceptions.JPRuntimeException;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.SequenceInputStream;

/**
 * Стрим апендер
 */
public class UploadInputStream implements AutoCloseable {
  private final String name;
  private final String extension;
  private final String nameWithoutExtension;
  private InputStream is;

  public UploadInputStream(String name) {
    this.name = name;

    if (name != null) {
      int dotIndex = name.lastIndexOf('.');
      this.extension = (dotIndex == -1) ? StringUtils.EMPTY : name.substring(dotIndex + 1);
      this.nameWithoutExtension = (dotIndex == -1) ? name : name.substring(0, dotIndex);
    } else {
      this.extension = null;
      this.nameWithoutExtension = null;
    }
  }


  public UploadInputStream() {
    this.name = null;
    this.extension = null;
    this.nameWithoutExtension = null;
  }

  public UploadInputStream collectInputStream(InputStream is) {
    if (this.is == null) {
      this.is = is;
    } else {
      this.is = new SequenceInputStream(this.is, is);
    }
    return this;
  }

  public InputStream getInputStream() {
    return this.is != null ? this.is : new NullInputStream();
  }

  public String getName() {
    return name;
  }

  /**
   * Возвращает имя файла без расширения
   *
   * @return Имя файла без расширения
   */
  public String getNameWithoutExtension() {
    return nameWithoutExtension;
  }

  /**
   * Возвращает расширение файла
   *
   * @return Расширение файла
   */
  public String getFileExtension() {
    return extension;
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
