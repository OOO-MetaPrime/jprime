package mp.jprime.files;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

/**
 * Определение тип файла
 */
@Service
public class FileTypeDetector {
  private Tika tika = new Tika();

  /**
   * Возвращает media type по имени файла
   *
   * @param name имя файла
   * @return media type
   */
  public String mediaType(String name) {
    return tika.detect(name);
  }
}
