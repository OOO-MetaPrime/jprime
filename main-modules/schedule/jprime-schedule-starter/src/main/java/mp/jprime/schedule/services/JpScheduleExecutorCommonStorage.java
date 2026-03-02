package mp.jprime.schedule.services;

import mp.jprime.application.JPApplicationShutdownManager;
import mp.jprime.schedule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище настроек логики выполнения
 */
@Service
public final class JpScheduleExecutorCommonStorage implements JpScheduleExecutorStorage {
  private static final Logger LOG = LoggerFactory.getLogger(JpScheduleExecutorCommonStorage.class);

  private final Map<String, JpScheduleExecutor> executors;

  private JpScheduleExecutorCommonStorage(@Autowired(required = false) Collection<JpScheduleExecutor> executors,
                                          @Autowired JPApplicationShutdownManager shutdownManager) {

    Map<String, JpScheduleExecutor> executorMap = new HashMap<>();
    if (executors != null) {
      for (JpScheduleExecutor executor : executors) {
        String code = executor.getCode();
        if (executorMap.containsKey(code)) {
          LOG.error("Error duplication JPScheduleExecutor with code {}", code);
          shutdownManager.exitWithError();
        }
        executorMap.put(code, executor);
      }
    }
    this.executors = executorMap;
  }

  @Override
  public Collection<JpScheduleExecutor> getExecutors() {
    return executors.values();
  }

  @Override
  public JpScheduleExecutor getExecutor(String code) {
    return executors.get(code);
  }
}
