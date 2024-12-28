package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Collections;

/**
 * Запрос массовой проверки доступа текущим пользователем к указанным объектам
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPObjectAccessBatchQuery {
  /**
   * Список объектов для проверки доступа
   */
  private Collection<JsonJPObjectAccessQuery> ids = Collections.emptyList();

  public Collection<JsonJPObjectAccessQuery> getIds() {
    return ids;
  }

  public void setIds(Collection<JsonJPObjectAccessQuery> ids) {
    this.ids = ids;
  }

}
