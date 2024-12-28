package mp.jprime.dataaccess.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JPClassValidatorService}
 */
@Configuration
public class JPClassValidatorServiceAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JPClassValidatorService service, Collection<JPClassValidatorServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (JPClassValidatorServiceAware aware : awares) {
      aware.setJpClassValidatorService(service);
    }
  }
}
