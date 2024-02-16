package mp.jprime.meta.services;

import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.log.AppLogger;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMetaDynamicLoader;
import mp.jprime.meta.annotations.services.JPMetaAnnoLoader;
import mp.jprime.meta.events.JPMetaLoadFinishEvent;
import mp.jprime.meta.log.Event;
import mp.jprime.meta.xmlloader.services.JPMetaXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Хранилище метаинформации
 */
@Service
@Lazy(value = false)
public final class JPMetaMemoryStorage implements JPMetaStorage {
  /**
   * Системный журнал
   */
  private final AppLogger appLogger;

  private final AtomicReference<Cache> cacheRef = new AtomicReference<>() {{
    set(new Cache());
  }};

  /**
   * Публикация событий
   */
  private ApplicationEventPublisher eventPublisher;

  /**
   * Размещает метаописание в хранилище
   */
  private JPMetaMemoryStorage(@Autowired AppLogger appLogger,
                              @Autowired ApplicationEventPublisher eventPublisher,
                              @Autowired JPMetaAnnoLoader annoLoader,
                              @Autowired JPMetaXmlLoader xmlLoader) {
    this.appLogger = appLogger;
    this.eventPublisher = eventPublisher;

    Collection<Flux<Collection<JPClass>>> p = new ArrayList<>();
    p.add(annoLoader.load());
    p.add(xmlLoader.load());
    Flux.concat(p)
        .filter(Objects::nonNull)
        .subscribe(this::applyJPClasses);
  }

  @Autowired(required = false)
  private void setDynamicLoader(Collection<JPMetaDynamicLoader> dynamicLoaders) {
    if (dynamicLoaders == null) {
      return;
    }
    AtomicInteger i = new AtomicInteger(1);
    dynamicLoaders.forEach(x -> x.load().forEach(flux -> {
      int sourceNum = i.getAndIncrement();
      flux.subscribe(list -> applyDynamicJPClasses(sourceNum, list));
    }));
  }

  /**
   * Загружает список классов
   *
   * @param list Список классов
   */
  private void applyJPClasses(Collection<JPClass> list) {
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
   * Сохраняет указанный список классов
   *
   * @param list Список классов
   */
  private void applyDynamicJPClasses(Integer sourceNum, Collection<JPClass> list) {
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
    eventPublisher.publishEvent(JPMetaLoadFinishEvent.newEvent());
  }


  /**
   * Возвращает метаописания классов
   *
   * @return метаописания классов
   */
  @Override
  public Collection<JPClass> getJPClasses() {
    return cacheRef.get().umClasses;
  }

  /**
   * Возвращает метаописание класса по коду
   *
   * @param code Код класса
   * @return Метаописание класса
   */
  @Override
  public JPClass getJPClassByCode(String code) {
    return code == null ? null : cacheRef.get().codeJpClassMap.get(code);
  }

  @Override
  public JPClass getJPClassByCodeOrThrow(String code) {
    if (!StringUtils.hasText(code)) {
      throw new JPClassNotFoundException();
    }
    JPClass jpClass = getJPClassByCode(code);
    if (jpClass == null) {
      throw new JPClassNotFoundException(code);
    }
    return jpClass;
  }

  private class Cache {
    private final static Integer IMMUTABLE_SOURCE_CODE = 0;

    private final UUID uuid = UUID.randomUUID();
    // Список всей меты
    private final Collection<JPClass> classes = ConcurrentHashMap.newKeySet();
    private final Collection<JPClass> umClasses = Collections.unmodifiableCollection(classes);
    // Список по источникам
    private final Map<Integer, Collection<JPClass>> sourceJpClassMap = new ConcurrentHashMap<>();
    // Код класса - класс
    private final Map<String, JPClass> codeJpClassMap = new ConcurrentHashMap<>();

    private Cache() {

    }

    private Cache(Cache oldCache, Integer sourceNum, Collection<JPClass> list) {
      // Добавляем постоянные настройки
      Collection<JPClass> immutableClasses = oldCache.sourceJpClassMap.get(IMMUTABLE_SOURCE_CODE);
      if (immutableClasses != null) {
        for (JPClass cls : immutableClasses) {
          if (cls.isImmutable()) {
            add(IMMUTABLE_SOURCE_CODE, cls);
          }
        }
      }
      // Добавляем динамические настройки
      for (Map.Entry<Integer, Collection<JPClass>> entry : oldCache.sourceJpClassMap.entrySet()) {
        Integer key = entry.getKey();
        Collection<JPClass> value = entry.getValue();
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

    private void add(Integer sourceCode, JPClass cls) {
      if (codeJpClassMap.containsKey(cls.getCode())) {
        return;
      }
      if (cls.getPrimaryKeyAttr() == null) {
        appLogger.error(Event.PRIMARY_KEY_NOT_FOUND, "JPClass \"" + cls.getCode() + "\" not loaded. Primary key is absent.");
        return;
      }
      sourceJpClassMap.computeIfAbsent(sourceCode, x -> ConcurrentHashMap.newKeySet()).add(cls);
      classes.add(cls);
      codeJpClassMap.put(cls.getCode(), cls);
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
