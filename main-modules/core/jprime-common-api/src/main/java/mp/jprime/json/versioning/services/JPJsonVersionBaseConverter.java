package mp.jprime.json.versioning.services;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.json.versioning.JPJsonVersionConverter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Базовая поддержка JPJsonVersionConverter с добавлением jsonMapper
 *
 * @param <T> Тип бина
 */
public abstract class JPJsonVersionBaseConverter<T> implements JPJsonVersionConverter<T> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  protected JPJsonMapper getJsonMapper() {
    return jsonMapper;
  }
}
