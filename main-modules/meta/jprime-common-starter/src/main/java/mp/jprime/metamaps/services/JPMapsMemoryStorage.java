package mp.jprime.metamaps.services;

import mp.jprime.meta.JPClass;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPMapsDynamicLoader;
import mp.jprime.metamaps.annotations.services.JPMapsAnnoLoader;
import mp.jprime.metamaps.xmlloader.services.JPMapsXmlLoader;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.services.RepositoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Описания привязки метаинформации к хранилищу
 */
@Service
@Lazy(value = false)
public final class JPMapsMemoryStorage implements JPMapsStorage {
  /**
   * Описания хранилищ
   */
  private RepositoryStorage repoStorage;

  private AtomicReference<Cache> cacheRef = new AtomicReference<Cache>() {
    {
      set(new Cache());
    }
  };


  /**
   * Размещает метаописание в хранилище
   */
  private JPMapsMemoryStorage(@Autowired JPMapsAnnoLoader annoLoader,
                              @Autowired JPMapsXmlLoader xmlLoader) {
    Collection<Flux<Collection<JPClassMap>>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPClassMaps);
  }

  @Autowired
  private void setRepoStorage(RepositoryStorage repoStorage) {
    this.repoStorage = repoStorage;
  }

  @Autowired(required = false)
  private void setDynamicLoader(Collection<JPMapsDynamicLoader> dynamicLoaders) {
    Flux
        .merge(
            dynamicLoaders.stream()
                .map(JPMapsDynamicLoader::load)
                .collect(Collectors.toList())
        )
        .filter(Objects::nonNull)
        .subscribe(this::applyDynamicJPClassMaps);
  }

  /**
   * Сохраняет указанную настройку
   *
   * @param maps настройки
   */
  private void applyJPClassMaps(Collection<JPClassMap> maps) {
    Cache cache = cacheRef.get();
    for (JPClassMap map : maps) {
      Map<String, JPClassMap> storages = cache.maps.computeIfAbsent(map.getCode(), x -> new HashMap<>());
      if (storages.containsKey(map.getStorage())) {
        continue;
      }
      storages.put(map.getStorage(), map);
    }
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

    while (true) {
      Cache newCache = new Cache();

      Cache oldCache = cacheRef.get();
      // Добавляем постоянные классы
      for (Map<String, JPClassMap> values : oldCache.maps.values()) {
        for (JPClassMap map : values.values()) {
          if (map.isImmutable()) {
            newCache.maps.computeIfAbsent(map.getCode(), x -> new HashMap<>()).put(map.getStorage(), map);
          }
        }
      }
      // Добавляем динамические классы
      for (JPClassMap map : list) {
        Map<String, JPClassMap> storages = newCache.maps.computeIfAbsent(map.getCode(), x -> new HashMap<>());
        if (storages.containsKey(map.getStorage())) {
          continue;
        }
        storages.put(map.getStorage(), map);
      }
      // Подменяем кеш после инициализации
      if (cacheRef.compareAndSet(oldCache, newCache)) {
        break;
      }
    }
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
    Map<String, JPClassMap> storages = cacheRef.get().maps.get(jpClass.getCode());
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

  /**
   * Возвращает все описания привязки метаинформации к хранилищу
   *
   * @param jpClass метакласс
   * @return описания привязки метаинформации к хранилищу
   */
  @Override
  public Collection<JPClassMap> getAll(@NonNull JPClass jpClass) {
    return cacheRef.get().maps.get(jpClass.getCode()).values();
  }

  private class Cache {
    private UUID uuid = UUID.randomUUID();
    /**
     * Описания привязки метаинформации к хранилищу
     */
    private Map<String, Map<String, JPClassMap>> maps = new ConcurrentHashMap<>();

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Cache cache = (Cache) o;
      return Objects.equals(uuid, cache.uuid);
    }

    @Override
    public int hashCode() {
      return Objects.hash(uuid);
    }
  }
}
