package mp.jprime.schedule.loaders;

import mp.jprime.schedule.JpScheduleTaskCatalog;
import mp.jprime.schedule.JpScheduleTaskCatalogLoader;
import mp.jprime.schedule.beans.JpScheduleTaskCatalogBean;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Загрузчик каталогов общесистемных задач, выполняемых по расписанию
 */
@Service
public final class JpScheduleTaskCatalogCommonLoader implements JpScheduleTaskCatalogLoader {

  @Override
  public Collection<JpScheduleTaskCatalog> getCatalogs() {
    return List.of(
        JpScheduleTaskCatalogBean.of(JpScheduleTaskCatalog.JP, "Система"),
        JpScheduleTaskCatalogBean.of(JpScheduleTaskCatalog.SYSTEM, "Общесистемные задачи")
    );
  }
}
