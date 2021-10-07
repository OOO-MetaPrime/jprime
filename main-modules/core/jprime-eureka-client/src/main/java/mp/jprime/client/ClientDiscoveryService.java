package mp.jprime.client;

import org.springframework.cloud.client.ServiceInstance;

import java.util.Collection;
import java.util.function.Function;

/**
 * Логика получение данных зарегистрированных сервисов
 */
public interface ClientDiscoveryService {
  /**
   * Возвращает список сервисов по указанному условию
   *
   * @param func Условие
   * @return Список сервисов
   */
  Collection<ServiceInstance> getServices(Function<ServiceInstance, Boolean> func);

  /**
   * Возвращает случайный сервис по указанному условию
   *
   * @param func Условие
   * @return Экземпляр сервиса
   */
  ServiceInstance getService(Function<ServiceInstance, Boolean> func);

  /**
   * Возвращает список сервисов по указанному имени
   *
   * @param serviceName Имя сервиса
   * @return Список сервисов
   */
  Collection<ServiceInstance> getServices(String serviceName);

  /**
   * Возвращает случайный сервис по указанному имени
   *
   * @param serviceName Имя сервиса
   * @return Экземпляр сервиса
   */
  ServiceInstance getService(String serviceName);
}
