package mp.jprime.security.abac.services;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.JPAbacDynamicLoader;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.PolicySet;
import mp.jprime.security.abac.PolicyTarget;
import mp.jprime.security.abac.annotations.services.JPAbacAnnoLoader;
import mp.jprime.security.abac.events.AbacChangeEvent;
import mp.jprime.security.abac.xmlloader.services.JPAbacXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Описание настроек ABAC
 */
@Service
@Lazy(value = false)
public final class JPAbacMemoryStorage implements JPAbacStorage {
  private final AtomicReference<Cache> cacheRef = new AtomicReference<>() {{
    set(new Cache());
  }};


  /**
   * Считываем настройки доступа
   */
  private JPAbacMemoryStorage(@Autowired JPAbacAnnoLoader annoLoader,
                              @Autowired JPAbacXmlLoader xmlLoader) {
    applyJPAbac(annoLoader.load());
    applyJPAbac(xmlLoader.load());
  }

  @Autowired(required = false)
  private void setDynamicLoaders(Collection<JPAbacDynamicLoader> dynamicLoaders) {
    if (dynamicLoaders == null) {
      return;
    }
    AtomicInteger i = new AtomicInteger(1);
    dynamicLoaders.forEach(x -> {
      int sourceNum = i.getAndIncrement();
      x.subscribe(list -> applyDynamicAbac(sourceNum, list));
    });
  }

  private void applyJPAbac(Collection<PolicySet> list) {
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

  private void applyDynamicAbac(Integer sourceNum, Collection<PolicySet> list) {
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
  public Collection<PolicySet> getSettings() {
    return cacheRef.get().umAllSetts.values();
  }

  @Override
  public Collection<Policy> getSettings(String jpClass, JPAction action) {
    ActionCache actionCache = cacheRef.get().classPolicies.get(jpClass);
    Collection<Policy> policies = actionCache == null ? null : actionCache.get(action);
    return policies == null ? Collections.emptyList() : policies;
  }

  @Override
  public Collection<String> getSettingsCodes() {
    return cacheRef.get().umAllSetts.keySet();
  }

  /**
   * Код события AbacChangeEvent
   *
   * @return AbacChangeEvent
   */
  public String getChangeEventCode() {
    return AbacChangeEvent.CODE;
  }

  private static class Cache {
    private static final Integer IMMUTABLE_SOURCE_CODE = 0;
    private final UUID uuid = UUID.randomUUID();
    // Код настроек - Настройки
    private final Map<String, PolicySet> allSetts = new ConcurrentHashMap<>();
    private final Map<String, PolicySet> umAllSetts = Collections.unmodifiableMap(allSetts);
    private final ActionCache commonPolicies = new ActionCache();
    private final Map<String, ActionCache> classPolicies = new ConcurrentHashMap<>();
    // Список по источникам
    private final Map<Integer, Collection<PolicySet>> sourceMaps = new ConcurrentHashMap<>();

    private Cache() {

    }


    private Cache(Cache oldCache, Integer sourceNum, Collection<PolicySet> list) {
      // Добавляем постоянные настройки
      Collection<PolicySet> immutableMaps = oldCache.sourceMaps.get(IMMUTABLE_SOURCE_CODE);
      if (immutableMaps != null) {
        for (PolicySet report : immutableMaps) {
          if (report.isImmutable()) {
            add(IMMUTABLE_SOURCE_CODE, report);
          }
        }
      }
      // Добавляем динамические настройки
      for (Map.Entry<Integer, Collection<PolicySet>> entry : oldCache.sourceMaps.entrySet()) {
        Integer key = entry.getKey();
        Collection<PolicySet> value = entry.getValue();
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


    private void add(Integer sourceCode, PolicySet sett) {
      if (sett == null) {
        return;
      }
      allSetts.put(sett.getCode(), sett);

      PolicyTarget target = sett.getTarget();
      if (target == null || target.getJpClassCodes().isEmpty()) {
        commonPolicies.addAll(sett.getPolicies());
      } else {
        target.getJpClassCodes().forEach(
            x -> classPolicies.computeIfAbsent(x, v -> new ActionCache()).addAll(sett.getPolicies())
        );
      }
      sourceMaps.computeIfAbsent(sourceCode, x -> ConcurrentHashMap.newKeySet()).add(sett);
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

  private static class ActionCache {
    private final Map<JPAction, Collection<Policy>> actionPolicies = new ConcurrentHashMap<>();
    private final Map<JPAction, Collection<Policy>> umActionPolicies = new ConcurrentHashMap<>();

    private ActionCache() {

    }

    private Collection<Policy> get(JPAction action) {
      return action == null ? null : umActionPolicies.get(action);
    }

    private void add(Policy policy) {
      policy.getActions().forEach(
          x -> actionPolicies.computeIfAbsent(x, v -> {
            Collection<Policy> result = new CopyOnWriteArrayList<>();
            umActionPolicies.put(x, Collections.unmodifiableCollection(result));
            return result;
          }).add(policy)
      );
    }

    private void addAll(Collection<Policy> policies) {
      policies.forEach(this::add);
    }
  }
}
