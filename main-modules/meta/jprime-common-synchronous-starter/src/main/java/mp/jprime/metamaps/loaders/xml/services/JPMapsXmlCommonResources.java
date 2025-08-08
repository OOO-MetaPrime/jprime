package mp.jprime.metamaps.loaders.xml.services;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.metamaps.loaders.xml.JPMapsXmlResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Загрузка описание привязки меты к хранилищам из xml
 */
@Service
public final class JPMapsXmlCommonResources implements JPMapsXmlResources {
  private static final Logger LOG = LoggerFactory.getLogger(JPMapsXmlCommonResources.class);
  /**
   * Папка с описанием привязки меты к хранилищам
   */
  public static final String RESOURCES_FOLDER = "metamaps/";

  private ApplicationContext applicationContext;

  @Autowired
  private void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Collection<Resource> getResources() {
    try {
      Resource[] resources = null;
      try {
        resources = applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
            JPMapsXmlCommonResources.RESOURCES_FOLDER + "*.xml");
      } catch (FileNotFoundException e) {
        LOG.debug(e.getMessage(), e);
      }
      if (resources == null || resources.length == 0) {
        return Collections.emptyList();
      }
      return Arrays.asList(resources);
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
