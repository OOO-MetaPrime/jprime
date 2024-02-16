package mp.jprime.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPReactiveObjectAccessService}
 */
@Configuration
public class JPReactiveObjectAccessServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPReactiveObjectAccessService service, Collection<JPReactiveObjectAccessServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPReactiveObjectAccessServiceAware aware : awares) {
      aware.setJpReactiveObjectAccessService(service);
    }
  }
}
