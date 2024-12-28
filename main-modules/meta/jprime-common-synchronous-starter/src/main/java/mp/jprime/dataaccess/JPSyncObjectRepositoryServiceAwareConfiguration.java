package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPSyncObjectRepositoryService}
 */
@Configuration
public class JPSyncObjectRepositoryServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPSyncObjectRepositoryService service, Collection<JPSyncObjectRepositoryServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPSyncObjectRepositoryServiceAware aware : awares) {
      aware.setJpSyncObjectRepositoryService(service);
    }
  }
}
