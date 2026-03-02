package mp.jprime.schedule.loaders.xml;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Загрузка описания задач
 */
public interface JpScheduleTaskXmlResources {
  Collection<Resource> getResources();
}
