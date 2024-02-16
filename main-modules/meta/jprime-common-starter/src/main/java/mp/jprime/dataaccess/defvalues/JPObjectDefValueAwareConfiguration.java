package mp.jprime.dataaccess.defvalues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPObjectDefValueService}
 */
@Configuration
public class JPObjectDefValueAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPObjectDefValueService service, Collection<JPObjectDefValueServiceAware> awares) {
    for (JPObjectDefValueServiceAware aware : awares) {
      aware.setJPObjectDefValueService(service);
    }
  }
}
