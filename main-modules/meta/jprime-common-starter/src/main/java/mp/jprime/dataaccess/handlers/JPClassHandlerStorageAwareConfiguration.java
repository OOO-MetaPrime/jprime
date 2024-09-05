package mp.jprime.dataaccess.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPClassHandlerStorage}
 */
@Configuration
public class JPClassHandlerStorageAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPClassHandlerStorage service, Collection<JPClassHandlerStorageAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPClassHandlerStorageAware aware : awares) {
      aware.setJPClassHandlerStorage(service);
    }
  }
}
