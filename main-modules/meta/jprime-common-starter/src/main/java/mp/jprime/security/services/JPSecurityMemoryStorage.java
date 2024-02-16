package mp.jprime.security.services;

import mp.jprime.security.JPSecurityDynamicLoader;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.annotations.services.JPSecurityAnnoLoader;
import mp.jprime.security.events.SecurityChangeEvent;
import mp.jprime.security.xmlloader.services.JPSecurityXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Описание настроек RBAC
 */
@Service
@Lazy(value = false)
public final class JPSecurityMemoryStorage implements JPSecurityStorage {
  private final AtomicReference<Cache> cacheRef = new AtomicReference<>() {{
    set(new Cache());
  }};

  /**
   * Считываем настройки доступа
   */
  private JPSecurityMemoryStorage(@Autowired JPSecurityAnnoLoader annoLoader,
                                  @Autowired JPSecurityXmlLoader xmlLoader) {
    Collection<Flux<Collection<JPSecurityPackage>>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyRbac);
  }

  @Autowired(required = false)
  private void setDynamicLoader(Collection<JPSecurityDynamicLoader> dynamicLoaders) {
    if (dynamicLoaders == null) {
      return;
    }
    AtomicInteger i = new AtomicInteger(1);
    dynamicLoaders.forEach(x -> {
      int sourceNum = i.getAndIncrement();
      x.load().subscribe(list -> applyDynamicRbac(sourceNum, list));
    });
  }

  private void applyRbac(Collection<JPSecurityPackage> list) {
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

  private void applyDynamicRbac(Integer sourceNum, Collection<JPSecurityPackage> list) {
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

  /**
   * Возвращает загруженные настройки доступа
   *
   * @return Настройки доступа
   */
  @Override
  public Collection<JPSecurityPackage> getPackages() {
    return cacheRef.get().setts.values();
  }

  /**
   * Возвращает настройку доступа по коду
   *
   * @param code Код
   * @return Настройка доступа
   */
  @Override
  public JPSecurityPackage getJPPackageByCode(String code) {
    return code != null ? cacheRef.get().setts.get(code) : null;
  }

  /**
   * Проверка всех пакетов на доступ на чтение
   *
   * @param roles Роли
   * @return Список кодов пакета
   */
  @Override
  public Collection<String> checkRead(Collection<String> roles) {
    Collection<String> codes = new ArrayList<>();
    for (JPSecurityPackage sett : cacheRef.get().setts.values()) {
      if (sett.checkRead(roles)) {
        codes.add(sett.getCode());
      }
    }
    return codes;
  }

  /**
   * Проверка доступа на чтение
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(String packageCode, Collection<String> roles) {
    if (packageCode == null) {
      return true;
    }
    JPSecurityPackage sett = getJPPackageByCode(packageCode);
    return sett != null && sett.checkRead(roles);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkDelete(String packageCode, Collection<String> roles) {
    if (packageCode == null) {
      return true;
    }
    JPSecurityPackage sett = getJPPackageByCode(packageCode);
    return sett != null && sett.checkDelete(roles);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkUpdate(String packageCode, Collection<String> roles) {
    if (packageCode == null) {
      return true;
    }
    JPSecurityPackage sett = getJPPackageByCode(packageCode);
    return sett != null && sett.checkUpdate(roles);
  }

  /**
   * Проверка доступа на создание
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkCreate(String packageCode, Collection<String> roles) {
    if (packageCode == null) {
      return true;
    }
    JPSecurityPackage sett = getJPPackageByCode(packageCode);
    return sett != null && sett.checkCreate(roles);
  }

  /**
   * Проверка доступа (CREATE/UPDATE/DELETE)
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkModify(String packageCode, Collection<String> roles) {
    if (packageCode == null) {
      return true;
    }
    JPSecurityPackage sett = getJPPackageByCode(packageCode);
    return sett != null && (
        sett.checkCreate(roles) && sett.checkDelete(roles) && sett.checkUpdate(roles)
    );
  }

  /**
   * Код события SecurityChangeEvent
   *
   * @return SecurityChangeEvent
   */
  public String getChangeEventCode() {
    return SecurityChangeEvent.CODE;
  }

  private static class Cache {
    private static final Integer IMMUTABLE_SOURCE_CODE = 0;
    private final UUID uuid = UUID.randomUUID();
    // Описания настроек безопасности
    private final Map<String, JPSecurityPackage> setts = new ConcurrentHashMap<>();
    // Список по источникам
    private final Map<Integer, Collection<JPSecurityPackage>> sourceMaps = new ConcurrentHashMap<>();

    private Cache() {

    }


    private Cache(Cache oldCache, Integer sourceNum, Collection<JPSecurityPackage> list) {
      // Добавляем постоянные настройки
      Collection<JPSecurityPackage> immutableMaps = oldCache.sourceMaps.get(IMMUTABLE_SOURCE_CODE);
      if (immutableMaps != null) {
        for (JPSecurityPackage sett : immutableMaps) {
          if (sett.isImmutable()) {
            add(IMMUTABLE_SOURCE_CODE, sett);
          }
        }
      }
      // Добавляем динамические настройки
      for (Map.Entry<Integer, Collection<JPSecurityPackage>> entry : oldCache.sourceMaps.entrySet()) {
        Integer key = entry.getKey();
        Collection<JPSecurityPackage> value = entry.getValue();
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


    private void add(Integer sourceCode, JPSecurityPackage sett) {
      if (sett.getCode() == null || setts.containsKey(sett.getCode())) {
        return;
      }
      setts.put(sett.getCode(), sett);
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
}
