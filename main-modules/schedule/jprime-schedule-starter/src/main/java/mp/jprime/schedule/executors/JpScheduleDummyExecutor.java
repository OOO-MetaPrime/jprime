package mp.jprime.schedule.executors;

import mp.jprime.common.JPParam;
import mp.jprime.lang.JPMap;
import mp.jprime.schedule.JpScheduleExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * Логика - пустышка
 */
@Service
public final class JpScheduleDummyExecutor implements JpScheduleExecutor {
  public static final String CODE = "dummy";

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public String getName() {
    return "Пустышка";
  }

  @Override
  public String getDescription() {
    return "Пишет в лог текущее время";
  }

  @Override
  public Collection<JPParam> getParams() {
    return Collections.emptyList();
  }

  @Override
  public void execute(JPMap paramValues) {
    LOG.error("dummy running: {}", LocalDateTime.now());
  }
}
