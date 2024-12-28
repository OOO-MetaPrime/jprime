package mp.jprime.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Описание миниатюры
 */
public interface JPFileThumbnail {
  /**
   * Признак, что миниатюра сформирована на основе оригинального файла
   *
   * @return Да/Нет
   */
  default boolean isOriginal() {
    return false;
  }

  /**
   * Название файла
   *
   * @return Название
   */
  String getFileName();

  /**
   * Данные миниатюры
   *
   * @return InputStream
   */
  InputStream getInputStream();

  static JPFileThumbnail of(boolean original, String fileName, InputStream inputStream) {
    return new JPFileThumbnail() {
      @Override
      public boolean isOriginal() {
        return original;
      }

      @Override
      public String getFileName() {
        return fileName;
      }

      @Override
      public InputStream getInputStream() {
        return inputStream;
      }
    };
  }

  static JPFileThumbnail ofByteArray(String fileName, byte[] body) {
    return new JPFileThumbnail() {
      @Override
      public String getFileName() {
        return fileName;
      }

      @Override
      public InputStream getInputStream() {
        return new ByteArrayInputStream(body);
      }
    };
  }
}
