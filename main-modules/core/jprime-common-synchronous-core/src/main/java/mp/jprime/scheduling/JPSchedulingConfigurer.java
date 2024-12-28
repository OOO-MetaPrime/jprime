package mp.jprime.scheduling;

import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * Конфигурация планировщика задач
 */
@Lazy(value = false)
@Configuration
@EnableConfigurationProperties(TaskSchedulingProperties.class)
public class JPSchedulingConfigurer implements SchedulingConfigurer {

  private final TaskSchedulingProperties properties;

  public JPSchedulingConfigurer(TaskSchedulingProperties properties) {
    this.properties = properties;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(jpTaskScheduler());
  }

  @Bean
  public Executor jpTaskScheduler() {
    ThreadPoolTaskScheduler t = new JPThreadPoolTaskScheduler();
    // min - 2; max - available processors
    t.setPoolSize(Math.max(Math.min(properties.getPool().getSize(), Runtime.getRuntime().availableProcessors()), 2));
    t.setThreadNamePrefix("jpTaskScheduler-");
    t.initialize();
    return t;
  }

  private static final class JPThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {
    @Override
    public Thread createThread(Runnable runnable) {
      Thread t = super.createThread(runnable);
      // fix to JAVA 11+
      t.setContextClassLoader(this.getClass().getClassLoader());
      return t;
    }
  }
}
