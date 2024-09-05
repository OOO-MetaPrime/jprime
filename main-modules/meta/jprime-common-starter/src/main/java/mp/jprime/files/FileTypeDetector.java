package mp.jprime.files;

import org.apache.tika.Tika;

/**
 * Определение тип файла
 */
public class FileTypeDetector {
  private static final Tika TIKA = new Tika();

  private FileTypeDetector() {

  }

  /**
   * Возвращает media type по имени файла
   *
   * @param name имя файла
   * @return media type
   */
  public static String mediaType(String name) {
    return TIKA.detect(name);
  }
}
