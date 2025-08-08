package mp.jprime.metamaps.services;

import mp.jprime.meta.JPClass;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.JPMapsDynamicMultiLoader;
import mp.jprime.metamaps.JPMapsLoader;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Описания привязки метаинформации к хранилищу
 */
@Service
@Lazy(value = false)
public final class JPMapsMemoryStorage implements JPMapsStorage {
  /**
   * Описания хранилищ
   */
  private RepositoryGlobalStorage repoStorage;

  private final AtomicReference<Cache> cacheRef = new AtomicReference<>() {{
    set(new Cache());
  }};


  /**
   * Размещает метаописание в хранилище
   */
  private JPMapsMemoryStorage(@Autowired Collection<JPMapsLoader> loaders) {
    loaders.forEach(x-> applyJPClassMaps(x.load()));
  }

  @Autowired
  private void setRepoStorage(RepositoryGlobalStorage repoStorage) {
    this.repoStorage = repoStorage;
  }

  @Autowired(required = false)
  private void setDynamicLoaders(Collection<JPMapsDynamicMultiLoader> dynamicLoaders) {
    if (dynamicLoaders == null) {
      return;
    }
    AtomicInteger i = new AtomicInteger(1);
    dynamicLoaders.forEach(x -> x.load().forEach(loader -> {
      int sourceNum = i.getAndIncrement();
      loader.subscribe(list -> applyDynamicJPClassMaps(sourceNum, list));
    }));
  }

  /**
   * Загружает список maps
   *
   * @param list Список maps
   */
  private void applyJPClassMaps(Collection<JPClassMap> list) {
    if (list == null || list.isEmpty()) {
      return;
    }

    while (true) {
      Cache oldCache = cacheRef.get();

      Cache newCache = new Cache(oldCache, Cache.IMMUTABLE_SOURCE_CODE, list);
      // Подменяем кеш после инициализации
      if (cacheRef.compareAndSet(oldCache, newCache)) {
        break;
      }
    }
  }

  /**
   * Сохраняет указанный список maps
   *
   * @param list Список maps
   */
  private void applyDynamicJPClassMaps(Integer sourceNum, Collection<JPClassMap> list) {
    if (list == null || list.isEmpty()) {
      return;
    }
    while (true) {
      Cache oldCache = cacheRef.get();

      Cache newCache = new Cache(oldCache, sourceNum, list);
      // Подменяем кеш после инициализации
      if (cacheRef.compareAndSet(oldCache, newCache)) {
        break;
      }
    }
  }

  @Override
  public JPClassMap get(JPClass jpClass) {
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

  @Override
  public Collection<JPClassMap> getAll(JPClass jpClass) {
    return cacheRef.get().maps.get(jpClass.getCode()).values();
  }

  private static class Cache {
    private static final Integer IMMUTABLE_SOURCE_CODE = 0;

    private final UUID uuid = UUID.randomUUID();
    // Описания привязки метаинформации к хранилищу
    private final Map<String, Map<String, JPClassMap>> maps = new ConcurrentHashMap<>();
    // Список по источникам
    private final Map<Integer, Collection<JPClassMap>> sourceMaps = new ConcurrentHashMap<>();

    private Cache() {

    }

    private Cache(Cache oldCache, Integer sourceNum, Collection<JPClassMap> list) {
      // Добавляем постоянные настройки
      Collection<JPClassMap> immutableMaps = oldCache.sourceMaps.get(IMMUTABLE_SOURCE_CODE);
      if (immutableMaps != null) {
        for (JPClassMap map : immutableMaps) {
          if (map.isImmutable()) {
            add(IMMUTABLE_SOURCE_CODE, map);
          }
        }
      }
      // Добавляем динамические настройки
      for (Map.Entry<Integer, Collection<JPClassMap>> entry : oldCache.sourceMaps.entrySet()) {
        Integer key = entry.getKey();
        Collection<JPClassMap> value = entry.getValue();
        if (IMMUTABLE_SOURCE_CODE.equals(key) || value == null || value.isEmpty()) {
          continue;
        }
        // Сначала добавляем всю мету другого источника
        if (!key.equals(sourceNum)) {
          value.forEach(x -> add(key, x));
        }
      }
      // Потом мету текущего источника
      if (list != null) {
        list.forEach(x -> add(sourceNum, x));
      }
    }

    private void add(Integer sourceCode, JPClassMap map) {
      Map<String, JPClassMap> storages = maps.computeIfAbsent(map.getCode(), x -> new ConcurrentHashMap<>());
      if (storages.containsKey(map.getStorage())) {
        return;
      }
      storages.put(map.getStorage(), map);
      sourceMaps.computeIfAbsent(sourceCode, x -> ConcurrentHashMap.newKeySet()).add(map);
    }


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
