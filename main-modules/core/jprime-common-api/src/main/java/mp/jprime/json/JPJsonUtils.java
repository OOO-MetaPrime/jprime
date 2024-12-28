package mp.jprime.json;

import mp.jprime.json.services.JPJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Работа с JSON
 */
@Service
public final class JPJsonUtils {
  private static JPJsonMapper SERVICE;

  @Autowired
  private void setJPJsonMapper(JPJsonMapper jsonMapper) {
    SERVICE = jsonMapper;
  }

  private JPJsonUtils() {
  }

  /**
   * JPJsonMapper
   *
   * @return Возвращает JPJsonMapper
   */
  public static JPJsonMapper getJsonMapper() {
    return SERVICE;
  }
}
