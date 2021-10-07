package mp.jprime.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * Параметры приложения
 */
@Service
@Lazy(value = false)
public class AppProperty implements JPAppProperty {
  private static String SERVICE_NAME;
  private static String APP_CODE;
  private static String SYSTEM_CODE;
  private static String APP_TITLE;
  private static InetAddress INET_ADDRESS = null;

  public AppProperty(@Autowired Environment environment) {
    SERVICE_NAME = environment.getProperty("spring.application.name", "");
    APP_CODE = environment.getProperty("jprime.application.code", SERVICE_NAME);
    SYSTEM_CODE = APP_CODE;
    APP_TITLE = environment.getProperty("jprime.application.title", "");
    try {
      INET_ADDRESS = InetAddress.getLocalHost();
    } catch (Exception e) {
      INET_ADDRESS = null;
    }
  }

  /**
   * Имя текущего spring сервиса
   *
   * @return Имя текущего spring сервиса
   */
  public static String getServiceName() {
    return SERVICE_NAME;
  }

  /**
   * Код текущего jprime сервиса
   *
   * @return Код текущего jprime сервиса
   */
  public static String getApplicationCode() {
    return APP_CODE;
  }

  /**
   * Название текущего jprime сервиса
   *
   * @return Название текущего jprime сервиса
   */
  public static String getApplicationTitle() {
    return APP_TITLE;
  }

  /**
   * IP адрес текущего сервиса
   *
   * @return IP адрес текущего сервиса
   */
  public static String getServiceIp() {
    return INET_ADDRESS != null ? INET_ADDRESS.getHostAddress() : null;
  }

  /**
   * Имя текущего spring сервиса
   *
   * @return Имя текущего spring сервиса
   */
  @Override
  public String serviceName() {
    return SERVICE_NAME;
  }

  /**
   * Код текущего jprime сервиса
   *
   * @return Код текущего jprime сервиса
   */
  @Override
  public String applicationCode() {
    return APP_CODE;
  }

  /**
   * Код подсистемы (по умолчанию - код текущего сервиса)
   *
   * @return Код подсистемы (по умолчанию - код текущего сервиса)
   */
  @Override
  public String systemCode() {
    return SYSTEM_CODE;
  }

  /**
   * Название текущего jprime сервиса
   *
   * @return Название текущего jprime сервиса
   */
  @Override
  public String applicationTitle() {
    return APP_TITLE;
  }
}
