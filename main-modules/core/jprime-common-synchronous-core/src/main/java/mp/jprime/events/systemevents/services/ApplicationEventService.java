package mp.jprime.events.systemevents.services;

import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.events.systemevents.JPSystemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy(value = false)
@ConditionalOnProperty(value = "jprime.events.systemevents.app.enabled", havingValue = "true")
public class ApplicationEventService implements SystemEventPublisher {
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publishEvent(JPSystemEvent event) {
    eventPublisher.publishEvent(JPSystemApplicationEvent.from(event));
  }
}
