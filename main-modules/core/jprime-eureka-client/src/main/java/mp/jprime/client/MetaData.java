package mp.jprime.client;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.client.ServiceInstance;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * MetaData сервиса
 */
public class MetaData {
  /**
   * Имя spring сервиса
   */
  private final static String SERVICE_NAME = "SERVICE_NAME";
  /**
   * Код сервиса
   */
  private final static String APPLICATION_CODE = "APPLICATION_CODE";
  /**
   * Название сервиса
   */
  private final static String APPLICATION_TITLE = "APPLICATION_TITLE";

  private final Map<String, String> metadata;

  private MetaData(Map<String, String> metadata) {
    this.metadata = metadata;
  }

  public static MetaData from(InstanceInfo info) {
    return new MetaData(info.getMetadata());
  }

  public static MetaData from(ServiceInstance instance) {
    return new MetaData(instance.getMetadata());
  }

  /**
   * Указываем имя сервиса (spring.application.name)
   *
   * @param value spring.application.name
   */
  public void setServiceName(String value) {
    metadata.put(SERVICE_NAME, value);
  }

  /**
   * Указываем код приложения (jprime.application.code)
   *
   * @param value jprime.application.code
   */
  public void setAppCode(String value) {
    metadata.put(APPLICATION_CODE, value);
  }

  /**
   * Указываем имя приложения (jprime.application.title)
   *
   * @param value jprime.application.title
   */
  public void setAppTitle(String value) {
    metadata.put(APPLICATION_TITLE, toBase64(value));
  }

  /**
   * Возвращает имя сервиса (spring.application.name)
   *
   * @return имя сервиса (spring.application.name)
   */
  public String getServiceName() {
    return getValue(SERVICE_NAME);
  }

  /**
   * Возвращает код приложения (jprime.application.code)
   *
   * @return код приложения (jprime.application.code)
   */
  public String getAppCode() {
    return getValue(APPLICATION_CODE);
  }

  /**
   * Возвращает имя приложения (jprime.application.title)
   *
   * @return имя приложения (jprime.application.title)
   */
  public String getAppTitle() {
    return fromBase64(getValue(APPLICATION_TITLE));
  }

  /**
   * Возвращает значение переменной
   *
   * @param code Код переменной
   * @return Значение указанной переменной
   */
  public String getValue(String code) {
    return metadata.get(code);
  }

  /**
   * Так как eureka не умеет работать с кириллицей, приходится кодировать в base64
   */
  private String toBase64(String value) {
    return value == null ? null : Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Так как eureka не умеет работать с кириллицей, приходится кодировать в base64
   */
  private String fromBase64(String base64) {
    return base64 == null ? null : new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
  }
}
