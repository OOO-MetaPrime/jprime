package mp.jprime.schedule.loaders.xml.services;

import mp.jprime.io.JpBaseLoaderResources;
import mp.jprime.schedule.loaders.xml.JpScheduleTaskXmlResources;
import org.springframework.stereotype.Service;

/**
 * Загрузка описания задач
 */
@Service
public final class JpScheduleTaskXmlCommonResources extends JpBaseLoaderResources implements JpScheduleTaskXmlResources {
  /**
   *  Папка с настройками
   */
  public static final String RESOURCES_FOLDER = "scheduletasks/settings/";

  @Override
  protected String getResourcesFolder() {
    return RESOURCES_FOLDER;
  }

  @Override
  protected String getResourcesExt() {
    return "xml";
  }
}