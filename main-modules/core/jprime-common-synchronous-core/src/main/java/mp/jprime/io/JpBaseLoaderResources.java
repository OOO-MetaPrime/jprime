package mp.jprime.io;

import mp.jprime.exceptions.JPRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Базовая логика получения ресурсов
 */
public abstract class JpBaseLoaderResources implements JpLoaderResources {
  private static final Logger LOG = LoggerFactory.getLogger(JpBaseLoaderResources.class);
  /**
   * Папка с настройками
   */
  public static final String RESOURCES_FOLDER = "compconf/settings/";

  private ApplicationContext applicationContext;

  @Autowired
  private void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  protected ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Папка с ресурсами
   *
   * @return Папка с ресурсами
   */
  protected abstract String getResourcesFolder();

  /**
   * Расширение файла с ресурсами
   *
   * @return Расширение файла
   */
  protected abstract String getResourcesExt();

  /**
   * Расширение файла с ресурсами
   *
   * @return Расширение файла
   */
  protected String getResourcesTemplate() {
    return "**/*";
  }

  /**
   * Место поиска ресурсов
   *
   * @return Место поиска ресурсов
   */
  protected String getPathPrefix() {
    return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
  }

  @Override
  public Collection<Resource> getResources() {
    try {
      Resource[] resources = null;
      try {
        resources = applicationContext.getResources(getPathPrefix() +
            getResourcesFolder() + getResourcesTemplate() + "." + getResourcesExt());
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