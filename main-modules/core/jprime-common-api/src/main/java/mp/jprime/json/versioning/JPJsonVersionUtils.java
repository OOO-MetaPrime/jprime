package mp.jprime.json.versioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Работа с версионированным JSON
 */
@Service
public final class JPJsonVersionUtils {
  private static JPJsonVersionService SERVICE;

  @Autowired
  private void setJPJsonVersionService(JPJsonVersionService jsonMapper) {
    SERVICE = jsonMapper;
  }

  private JPJsonVersionUtils() {
  }

  /**
   * JPJsonVersionService
   *
   * @return Возвращает JPJsonVersionService
   */
  public static JPJsonVersionService getService() {
    return SERVICE;
  }
}
