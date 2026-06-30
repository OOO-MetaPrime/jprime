package mp.jprime.meta.loaders.xml.services;

import mp.jprime.io.JpBaseLoaderResources;
import mp.jprime.meta.loaders.xml.JPMetaXmlResources;
import org.springframework.stereotype.Service;

/**
 * Загрузка метаинформации из xml
 */
@Service
public final class JPMetaXmlCommonResources extends JpBaseLoaderResources implements JPMetaXmlResources {
  /**
   * Папка с метаописанием
   */
  public static final String RESOURCES_FOLDER = "meta/";

  @Override
  protected String getResourcesFolder() {
    return RESOURCES_FOLDER;
  }

  @Override
  protected String getResourcesExt() {
    return "xml";
  }
}
