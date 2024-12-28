package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPObjectAccessService}
 */
@Configuration
public class JPObjectAccessServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPObjectAccessService service, Collection<JPObjectAccessServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPObjectAccessServiceAware aware : awares) {
      aware.setJpObjectAccessService(service);
    }
  }
}
