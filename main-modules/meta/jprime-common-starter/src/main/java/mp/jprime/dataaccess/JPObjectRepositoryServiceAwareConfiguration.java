package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPObjectRepositoryService}
 */
@Configuration
public class JPObjectRepositoryServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPObjectRepositoryService service, Collection<JPObjectRepositoryServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPObjectRepositoryServiceAware aware : awares) {
      aware.setJpObjectRepositoryService(service);
    }
  }
}
