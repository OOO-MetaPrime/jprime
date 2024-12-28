package mp.jprime.util.services;

import mp.jprime.util.JPApplicationShutdownManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Базовый сервис завершения работы приложения
 */
@Service
public class JPApplicationBaseShutdownManager implements JPApplicationShutdownManager {
  private ApplicationContext context;

  @Autowired
  private void setContext(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public void exit() {
    exit(0);
  }

  @Override
  public void exitWithError() {
    exit(1);
  }

  private void exit(int exitCode) {
    SpringApplication.exit(context, () -> exitCode);
    System.exit(exitCode);
  }
}
