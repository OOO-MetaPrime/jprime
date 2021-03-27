package mp.jprime.metamaps.services;

import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.meta.JPClass;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPMapsDynamicLoader;
import mp.jprime.metamaps.annotations.services.JPMapsAnnoLoader;
import mp.jprime.metamaps.xmlloader.services.JPMapsXmlLoader;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Описания привязки метаинформации к хранилищу
 */
@Service
public final class JPMapsMemoryStorage implements JPMapsStorage {
  /**
   * Описания привязки метаинформации к хранилищу
   */
  private Map<String, Map<String, JPClassMap>> maps = new ConcurrentHashMap<>();
  /**
   * Динамическая загрузка меты
   */
  private JPMapsDynamicLoader dynamicLoader;
  /**
   * Описания хранилищ
   */
  private RepositoryStorage repoStorage;

  /**
   * Размещает метаописание в хранилище
   */
  private JPMapsMemoryStorage(@Autowired JPMapsAnnoLoader annoLoader,
                              @Autowired JPMapsXmlLoader xmlLoader) {
    Collection<Flux<JPClassMap>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPClassMap);
  }

  @Autowired
  private void setRepoStorage(RepositoryStorage repoStorage) {
    this.repoStorage = repoStorage;
  }

  @Autowired(required = false)
  private void setDynamicLoader(JPMapsDynamicLoader dynamicLoader) {
    this.dynamicLoader = dynamicLoader;
  }

  /**
   * Сохраняет указанную настройку
   *
   * @param map настройку
   */
  private void applyJPClassMap(JPClassMap map) {
    Map<String, JPClassMap> storages = maps.computeIfAbsent(map.getCode(), x -> new HashMap<>());
    if (storages.containsKey(map.getStorage())) {
      return;
    }
    storages.put(map.getStorage(), map);
  }

  /**
   * Сохраняет указанный список классов
   *
   * @param list Список классов
   */
  private void applyDynamicJPClassMaps(Collection<JPClassMap> list) {
    if (list == null || list.isEmpty()) {
      return;
    }

    Map<String, Map<String, JPClassMap>> maps = new ConcurrentHashMap<>();
    // Добавляем постоянные классы
    for (Map<String, JPClassMap> values : this.maps.values()) {
      for (JPClassMap map : values.values()) {
        if (map.isImmutable()) {
          maps.computeIfAbsent(map.getCode(), x -> new HashMap<>()).put(map.getStorage(), map);
        }
      }
    }
    // Добавляем динамические классы
    for (JPClassMap map : list) {
      Map<String, JPClassMap> storages = maps.computeIfAbsent(map.getCode(), x -> new HashMap<>());
      if (storages.containsKey(map.getStorage())) {
        continue;
      }
      storages.put(map.getStorage(), map);
    }
    this.maps = maps;
  }

  /**
   * Возвращает описание привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описание привязки метаинформации к хранилищу
   */
  @Override
  public Mono<JPClassMap> mono(@NonNull JPClass jpClass) {
    return Mono.justOrEmpty(get(jpClass));
  }

  /**
   * Возвращает описание привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описание привязки метаинформации к хранилищу
   */
  @Override
  public JPClassMap get(@NonNull JPClass jpClass) {
    Map<String, JPClassMap> storages = maps.get(jpClass.getCode());
    if (storages == null) {
      return null;
    }
    for (JPClassMap map : storages.values()) {
      JPStorage storage = repoStorage.getStorage(map.getStorage());
      if (storage != null) {
        return map;
      }
    }
    return null;
  }

  @EventListener(condition = "#event.eventCode.equals(T(mp.jprime.meta.events.JPMetaChangeEvent).CODE)")
  public void handleJPMetaChangeEvent(JPSystemApplicationEvent event) {
    dynamicLoad();
  }

  @EventListener
  public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
    dynamicLoad();
  }

  private void dynamicLoad() {
    if (dynamicLoader != null) {
      dynamicLoader.load()
          .collectList()
          .subscribe(this::applyDynamicJPClassMaps);
    }
  }
}
