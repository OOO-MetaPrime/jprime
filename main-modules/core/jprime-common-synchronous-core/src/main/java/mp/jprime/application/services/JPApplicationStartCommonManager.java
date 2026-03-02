package mp.jprime.application.services;

import mp.jprime.application.*;
import mp.jprime.migration.JPMigrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Сервис запуска приложения
 */
@Service
public final class JPApplicationStartCommonManager implements JPApplicationStartManager, SmartInitializingSingleton {
  private static final Logger LOG = LoggerFactory.getLogger(JPApplicationStartCommonManager.class);

  private final Collection<JPMigrationService> migrations;
  private final Collection<JPApplicationMetaBootListener> metaBootListeners;
  private final Collection<JPApplicationBootListener> bootListeners;
  private final Collection<JPApplicationInitListener> initListeners;
  private final Collection<JPApplicationStartListener> startListeners;

  private JPApplicationStartCommonManager(@Autowired(required = false) Collection<JPMigrationService> migrations,
                                          @Autowired(required = false) Collection<JPApplicationMetaBootListener> metaBootListeners,
                                          @Autowired(required = false) Collection<JPApplicationBootListener> bootListeners,
                                          @Autowired(required = false) Collection<JPApplicationInitListener> initListeners,
                                          @Autowired(required = false) Collection<JPApplicationStartListener> startListeners) {
    this.migrations = migrations != null ? migrations : Collections.emptySet();
    this.metaBootListeners = metaBootListeners != null ? metaBootListeners : Collections.emptySet();
    this.bootListeners = bootListeners != null ? bootListeners : Collections.emptySet();
    this.initListeners = initListeners != null ? initListeners : Collections.emptySet();
    this.startListeners = startListeners != null ? startListeners : Collections.emptySet();
  }

  @Override
  public void afterSingletonsInstantiated() {
    LOG.debug("JPApplicationStartCommonManager.start");

    LOG.debug("JPMigrationServices.start");
    for (JPMigrationService migration : migrations) {
      migration.migrate();
    }
    LOG.debug("JPMigrationServices.end");

    LOG.debug("JPApplicationMetaBootListener.start");
    for (JPApplicationMetaBootListener listener : metaBootListeners) {
      listener.applicationBoot();
    }
    LOG.debug("JPApplicationMetaBootListener.end");

    LOG.debug("JPApplicationBootListener.start");
    for (JPApplicationBootListener listener : bootListeners) {
      listener.applicationBoot();
    }
    LOG.debug("JPApplicationBootListener.end");

    LOG.debug("JPApplicationInitListener.start");
    for (JPApplicationInitListener listener : initListeners) {
      listener.applicationInit();
    }
    LOG.debug("JPApplicationInitListener.end");

    LOG.debug("JPApplicationStartListeners.start");
    for (JPApplicationStartListener listener : startListeners) {
      listener.applicationStart();
    }
    LOG.debug("JPApplicationStartListeners.end");

    LOG.debug("JPApplicationStartCommonManager.end");
  }
}
