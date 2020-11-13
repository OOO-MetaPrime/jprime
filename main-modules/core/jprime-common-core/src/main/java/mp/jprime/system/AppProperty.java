package mp.jprime.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * Параметры приложения
 */
@Service
public class AppProperty {
  private static String serviceName;
  private static InetAddress ip = null;

  public AppProperty(@Autowired ApplicationContext applicationContext) {
    serviceName = applicationContext.getEnvironment().getProperty("spring.application.name");
    try {
      ip = InetAddress.getLocalHost();
    } catch (Exception e) {
      ip = null;
    }
  }

  /**
   * Имя текущего сервиса
   *
   * @return Имя текущего сервиса
   */
  public static String getServiceName() {
    return serviceName;
  }

  /**
   * IP адрес текущего сервиса
   *
   * @return IP адрес текущего сервиса
   */
  public static String getServiceIp() {
    return ip != null ? ip.getHostAddress() : null;
  }
}
