package mp.jprime.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class ClientDiscoveryService {

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
  public Collection<ServiceInstance> getServices(Function<ServiceInstance, Boolean> func) {
    Collection<ServiceInstance> uris = new ArrayList<>();
    List<String> serviceIds = discoveryClient.getServices();
    for (String serviceId : serviceIds) {
      List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
      if (serviceInstances != null && !serviceInstances.isEmpty()) {
        ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));
        Boolean res = func.apply(serviceInstance);
        if (res != null && res) {
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
  public ServiceInstance getService(Function<ServiceInstance, Boolean> func) {
    List<String> serviceIds = discoveryClient.getServices();
    for (String serviceId : serviceIds) {
      List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
      if (serviceInstances != null && !serviceInstances.isEmpty()) {
        ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));

        Boolean res = func.apply(serviceInstance);
        if (res != null && res) {
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
  public Collection<ServiceInstance> getServices(String serviceName) {
    return discoveryClient.getInstances(serviceName);
  }

  /**
   * Возвращает случайный сервис по указанному имени
   *
   * @param serviceName Имя сервиса
   * @return Экземпляр сервиса
   */
  public ServiceInstance getService(String serviceName) {
    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
    if (serviceInstances != null && !serviceInstances.isEmpty()) {
      return serviceInstances.get(new Random().nextInt(serviceInstances.size()));
    }
    return null;
  }
}
