package mp.jprime.utils.services;

import jakarta.annotation.Nonnull;
import mp.jprime.migration.JPMigrationService;
import mp.jprime.utils.JPApplicationStartListener;
import mp.jprime.utils.JPApplicationStartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис запуска приложения
 */
@Service
public class JPApplicationStartCommonManager implements JPApplicationStartManager, ApplicationListener<ContextRefreshedEvent> {
  private static final Logger LOG = LoggerFactory.getLogger(JPApplicationStartCommonManager.class);

  private Collection<JPMigrationService> migrations;
  private Collection<JPApplicationStartListener> listeners;

  @Autowired(required = false)
  private void setMigrations(Collection<JPMigrationService> migrations) {
    this.migrations = migrations;
  }

  @Autowired(required = false)
  private void setListeners(Collection<JPApplicationStartListener> listeners) {
    this.listeners = listeners;
  }

  /**
   * Необходимая опция, чтобы приложение не стартовало, пока не закончится вся логика
   *
   * @return Нет
   */
  @Override
  public boolean supportsAsyncExecution() {
    return false;
  }

  @Override
  public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
    LOG.debug("JPApplicationStartCommonManager.start");

    if (migrations != null) {
      LOG.debug("JPMigrationServices.start");
      for (JPMigrationService migration : migrations) {
        migration.migrate();
      }
      LOG.debug("JPMigrationServices.end");
    }

    if (listeners != null) {
      LOG.debug("JPApplicationStartListeners.start");
      for (JPApplicationStartListener listener : listeners) {
        listener.applicationStart();
      }
      LOG.debug("JPApplicationStartListeners.end");
    }

    LOG.debug("JPApplicationStartCommonManager.end");
  }
}
