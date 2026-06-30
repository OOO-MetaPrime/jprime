package mp.jprime.metamaps.loaders.xml.services;

import mp.jprime.io.JpBaseLoaderResources;
import mp.jprime.metamaps.loaders.xml.JPMapsXmlResources;
import org.springframework.stereotype.Service;

/**
 * Загрузка описание привязки меты к хранилищам из xml
 */
@Service
public final class JPMapsXmlCommonResources extends JpBaseLoaderResources implements JPMapsXmlResources {
  /**
   * Папка с описанием привязки меты к хранилищам
   */
  public static final String RESOURCES_FOLDER = "metamaps/";

  @Override
  protected String getResourcesFolder() {
    return RESOURCES_FOLDER;
  }

  @Override
  protected String getResourcesExt() {
    return "xml";
  }
}
