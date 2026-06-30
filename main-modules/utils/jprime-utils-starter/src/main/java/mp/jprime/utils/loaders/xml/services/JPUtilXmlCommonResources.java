package mp.jprime.utils.loaders.xml.services;

import mp.jprime.io.JpBaseLoaderResources;
import mp.jprime.utils.loaders.xml.JPUtilXmlResources;
import org.springframework.stereotype.Service;

/**
 * Загрузка описания утилит
 */
@Service
public final class JPUtilXmlCommonResources extends JpBaseLoaderResources implements JPUtilXmlResources {
  /**
   * Папка с настройками
   */
  public static final String RESOURCES_FOLDER = "utils/settings/";

  @Override
  protected String getResourcesFolder() {
    return RESOURCES_FOLDER;
  }

  @Override
  protected String getResourcesExt() {
    return "xml";
  }
}