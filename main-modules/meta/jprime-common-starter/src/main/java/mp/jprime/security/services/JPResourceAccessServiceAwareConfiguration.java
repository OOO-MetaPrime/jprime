package mp.jprime.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPResourceAccessService}
 */
@Configuration
public class JPResourceAccessServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPResourceAccessService service, Collection<JPResourceAccessServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPResourceAccessServiceAware aware : awares) {
      aware.setJpResourceAccessService(service);
    }
  }
}
