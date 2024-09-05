package mp.jprime.dataaccess.applyvalues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPObjectApplyValueService}
 */
@Configuration
public class JPObjectApplyValueServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPObjectApplyValueService service, Collection<JPObjectApplyValueServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPObjectApplyValueServiceAware aware : awares) {
      aware.setJPObjectApplyValueService(service);
    }
  }
}
