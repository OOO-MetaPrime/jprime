package mp.jprime.dataaccess.checkers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPDataCheckService}
 */
@Configuration
public class JPDataCheckServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPDataCheckService service, Collection<JPDataCheckServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPDataCheckServiceAware aware : awares) {
      aware.setJpDataCheckService(service);
    }
  }
}
