package mp.jprime.meta.loaders.xml.services;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.loaders.xml.JPMetaXmlResources;
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
 * Загрузка метаинформации из xml
 */
@Service
public final class JPMetaXmlCommonResources implements JPMetaXmlResources {
  private static final Logger LOG = LoggerFactory.getLogger(JPMetaXmlCommonResources.class);
  /**
   * Папка с метаописанием
   */
  public static final String RESOURCES_FOLDER = "meta/";

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
            JPMetaXmlCommonResources.RESOURCES_FOLDER + "*.xml");
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
