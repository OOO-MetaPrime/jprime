package mp.jprime.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Сервис для инжекта реализации {@link ParserService}
 */
@Configuration
public class ParserServiceAwareConfiguration {
  @Autowired(required = false)
  public void setToAwares(ParserService service,  Collection<ParserServiceAware> awares) {
    if (awares == null) {
      return;
    }
    for (ParserServiceAware aware : awares) {
      aware.setParserService(service);
    }
  }
}
