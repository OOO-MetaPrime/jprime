package mp.jprime.schedule.services;

import mp.jprime.application.JPApplicationStartListener;
import mp.jprime.log.AppLogger;
import mp.jprime.schedule.*;
import mp.jprime.schedule.exceptions.JpScheduleTaskIntermittentNotAllowException;
import mp.jprime.schedule.log.Event;
import mp.jprime.security.AuthInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@EnableScheduling
@Service
public final class JpScheduleCommonService implements JpScheduleService, JPApplicationStartListener {
  private static final Logger LOG = LoggerFactory.getLogger(JpScheduleCommonService.class);
  private static final String CRON_DISABLED = "-";

  private final AppLogger appLogger;
  private final TaskScheduler taskScheduler;
  private final JpScheduleExecutorStorage executorStorage;
  private final Map<String, JpScheduleTaskCatalog> catalogs;
  private final Collection<JpScheduleTaskLoader> taskLoaders;

  private final Map<UUID, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();
  private final Map<UUID, JpScheduleTask> tasks = new ConcurrentHashMap<>();

  private JpScheduleCommonService(@Autowired AppLogger appLogger,
                                  @Autowired TaskScheduler taskScheduler,
                                  @Autowired JpScheduleExecutorStorage executorStorage,
                                  @Autowired(required = false) Collection<JpScheduleTaskCatalogLoader> catalogLoaders,
                                  @Autowired(required = false) Collection<JpScheduleTaskLoader> taskLoaders) {
    this.appLogger = appLogger;
    this.taskScheduler = taskScheduler;
    this.executorStorage = executorStorage;

    Map<String, JpScheduleTaskCatalog> catalogMap = new HashMap<>();
    if (catalogLoaders != null) {
      for (JpScheduleTaskCatalogLoader catalogLoader : catalogLoaders) {
        catalogLoader.getCatalogs().forEach(x -> catalogMap.put(x.getCatalog(), x));
      }
    }
    this.catalogs = catalogMap;

    this.taskLoaders = taskLoaders != null ? taskLoaders : Collections.emptyList();
  }

  @Override
  public void applicationStart() {
    for (JpScheduleTaskLoader loader : taskLoaders) {
      for (JpScheduleTask task : loader.getTasks()) {
        schedule(task);
      }
    }
  }

  @Override
  public Collection<JpScheduleTaskCatalog> getCatalogs() {
    return catalogs.values();
  }

  @Override
  public Collection<JpScheduleExecutor> getExecutors() {
    return executorStorage.getExecutors();
  }

  @Override
  public JpScheduleExecutor getExecutor(String code) {
    return executorStorage.getExecutor(code);
  }

  @Override
  public Collection<JpScheduleTask> getTasks() {
    return tasks.values();
  }

  @Override
  public void schedule(JpScheduleTask task) {
    UUID code = task != null ? task.getCode() : null;
    if (code == null) {
      return;
    }
    JpScheduleExecutor executor = getExecutor(task.getExecutorCode());
    if (executor == null) {
      return;
    }
    schedule(task, () -> executor.execute(task.getParamValues()), true);
  }

  @Override
  public void schedule(UUID code, String name, String description, String catalogCode, JpScheduleCron cron, Runnable execute, boolean actionLog) {
    if (code == null) {
      return;
    }
    schedule(JpScheduleTask.of(code, name, description, catalogCode, cron), execute, actionLog);
  }

  @Override
  public boolean isRunning(UUID code) {
    return futures.containsKey(code);
  }

  @Override
  public void remove(UUID code) {
    stop(code);
    tasks.remove(code);
  }

  @Override
  public void stop(UUID code) {
    ScheduledFuture<?> future = code != null ? futures.get(code) : null;
    if (future != null) {
      future.cancel(false);
      futures.remove(code);
    }
  }

  @Override
  public JpScheduleTask start(UUID code, AuthInfo auth) {
    JpScheduleTask task = tasks.get(code);
    JpScheduleTaskCatalog catalog = task != null ? catalogs.get(task.getCatalogCode()) : null;
    if (catalog == null || !catalog.isTempIntermittent()) {
      throw new JpScheduleTaskIntermittentNotAllowException();
    }
    schedule(task);

    return task;
  }

  @Override
  public JpScheduleTask stop(UUID code, AuthInfo auth) {
    JpScheduleTask task = tasks.get(code);
    JpScheduleTaskCatalog catalog = task != null ? catalogs.get(task.getCatalogCode()) : null;
    if (catalog == null || !catalog.isTempIntermittent()) {
      throw new JpScheduleTaskIntermittentNotAllowException();
    }
    stop(code);

    return task;
  }

  private void schedule(JpScheduleTask task, Runnable execute, boolean actionLog) {
    UUID code = task.getCode();
    JpScheduleCron cron = task.getCron();
    String expression = cron != null ? cron.getExpression() : null;
    if (code == null || expression == null || execute == null) {
      return;
    }
    if (CRON_DISABLED.equals(expression)) {
      return;
    }
    stop(code);

    String sCode = code.toString();
    String description = getDescription(task);

    ScheduledFuture<?> future = taskScheduler.schedule(() -> {
      if (actionLog) {
        appLogger.debug(Event.TASK_STARTED, sCode, description + " has started");
      }
      try {
        execute.run();

        if (actionLog) {
          appLogger.debug(Event.TASK_FINISHED, sCode, description + " has finished");
        }
      } catch (Exception e) {
        appLogger.error(Event.TASK_FINISHED_WITH_ERROR, sCode, description + " has finished with error");
        LOG.error(e.getMessage(), e);
      }
    }, new CronTrigger(expression));

    futures.put(code, future);
    tasks.put(code, task);
  }

  private String getDescription(JpScheduleTask task) {
    UUID code = task.getCode();
    String name = task.getName();
    return "JpTask \"" + name + "\" (" + code + ")";
  }
}
