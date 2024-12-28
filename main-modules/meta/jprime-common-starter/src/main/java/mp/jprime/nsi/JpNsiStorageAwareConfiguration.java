package mp.jprime.nsi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link JpNsiStorage}
 */
@Configuration
public class JpNsiStorageAwareConfiguration {
  @Autowired(required = false)
  public void setAwares(JpNsiStorage service, Collection<JpNsiStorageAware> awares) {
    if (awares == null) {
      return;
    }
    for (JpNsiStorageAware aware : awares) {
      aware.setJpNsiStorage(service);
    }
  }
}
