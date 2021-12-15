package mp.jprime.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Логика получение данных зарегистрированных сервисов
 */
@Service
public class EurekaClientDiscoveryService implements ClientDiscoveryService {

  /**
   * Клиент опроса service-discovery
   */
  private DiscoveryClient discoveryClient;

  @Autowired
  private void setDiscoveryClient(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  /**
   * Возвращает список сервисов по указанному условию
   *
   * @param func Условие
   * @return Список сервисов
   */
  @Override
  public Collection<ServiceInstance> getServices(Predicate<ServiceInstance> func) {
    Collection<ServiceInstance> uris = new ArrayList<>();
    List<String> serviceIds = discoveryClient.getServices();
    for (String serviceId : serviceIds) {
      List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
      if (serviceInstances != null && !serviceInstances.isEmpty()) {
        ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));
        if (func.test(serviceInstance)) {
          uris.add(serviceInstance);
        }
      }
    }
    return uris;
  }

  /**
   * Возвращает случайный сервис по указанному условию
   *
   * @param func Условие
   * @return Экземпляр сервиса
   */
  @Override
  public ServiceInstance getService(Predicate<ServiceInstance> func) {
    List<String> serviceIds = discoveryClient.getServices();
    for (String serviceId : serviceIds) {
      List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
      if (serviceInstances != null && !serviceInstances.isEmpty()) {
        ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));

        if (func.test(serviceInstance)) {
          return serviceInstance;
        }
      }
    }
    return null;
  }

  /**
   * Возвращает список сервисов по указанному имени
   *
   * @param serviceName Имя сервиса
   * @return Список сервисов
   */
  @Override
  public Collection<ServiceInstance> getServices(String serviceName) {
    return discoveryClient.getInstances(serviceName);
  }

  /**
   * Возвращает случайный сервис по указанному имени
   *
   * @param serviceName Имя сервиса
   * @return Экземпляр сервиса
   */
  @Override
  public ServiceInstance getService(String serviceName) {
    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
    if (serviceInstances != null && !serviceInstances.isEmpty()) {
      return serviceInstances.get(new Random().nextInt(serviceInstances.size()));
    }
    return null;
  }
}
