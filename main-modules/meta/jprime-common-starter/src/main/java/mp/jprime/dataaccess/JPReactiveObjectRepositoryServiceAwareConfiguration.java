package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPReactiveObjectRepositoryService}
 */
@Configuration
public class JPReactiveObjectRepositoryServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPReactiveObjectRepositoryService service, Collection<JPReactiveObjectRepositoryServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPReactiveObjectRepositoryServiceAware aware : awares) {
      aware.setJpReactiveObjectRepositoryService(service);
    }
  }
}
