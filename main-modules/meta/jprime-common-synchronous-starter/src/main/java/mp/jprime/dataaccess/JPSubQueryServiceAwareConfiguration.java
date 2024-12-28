package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPSubQueryService}
 */
@Configuration
public class JPSubQueryServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPSubQueryService service, Collection<JPSubQueryServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPSubQueryServiceAware aware : awares) {
      aware.setJpSubQueryService(service);
    }
  }
}
